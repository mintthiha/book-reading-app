package com.example.bookreadingapp.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.bookreadingapp.R
import com.example.bookreadingapp.domain.ChapterElement
import com.example.bookreadingapp.ui.navigation.NavRoutes
import com.example.bookreadingapp.ui.utils.AdaptiveNavigationType
import com.example.bookreadingapp.ui.viewmodels.BookReadingAppViewModel
import com.example.bookreadingapp.ui.viewmodels.BookViewModel
import org.jsoup.Jsoup

/**
 * Composable for displaying the reading screen.
 * Includes chapter content, navigation controls, and a switch for toggling reading mode.
 *
 * @param viewModel Manages app state including reading mode.
 * @param bookViewModel Manages book-specific details like chapter content and navigation.
 * @param adaptiveNavigationType Determines screen layout based on navigation type.
 * @param navController Handles navigation between screens.
 */
@Composable
fun Reading(
    viewModel: BookReadingAppViewModel,
    bookViewModel: BookViewModel,
    adaptiveNavigationType: AdaptiveNavigationType,
    navController: NavHostController
) {
    val currentChapter by bookViewModel.currentChapter.observeAsState(null)
    if (currentChapter != null) {
        bookViewModel.updateCurrentChapterContent(currentChapter!!.chapterId)
    }
    var columnWidth by remember { mutableIntStateOf(0) }
    // Use Box to position the chapter navigation buttons at the bottom
    Box(
        modifier = Modifier
            .padding(dimensionResource(R.dimen.small_padding))
            .fillMaxSize() // Take full available size
    ) {
        // Column containing the chapter content and other UI elements
        Column(
            modifier = Modifier
                .fillMaxSize()
                .testTag("ReadingScreen")
                .onSizeChanged { size ->
                    // Track the width of the Column
                    columnWidth = size.width
                }
        ) {
            ReadingModeSwitch(
                checked = viewModel.isReadingMode,
                onCheckedChange = { viewModel.toggleIsReadingMode() }
            )
            Text(
                text = currentChapter?.title ?: stringResource(R.string.loading),
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.extra_large_height)))
            // The actual chapter's content
            ChapterContent(bookViewModel, adaptiveNavigationType, columnWidth)
        }
        // To go to previous or next chapter, if it exists
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter) // Align at the bottom of the Box
        ) {
            ChapterNavigation(bookViewModel, navController)
        }
    }
}

// Inspired from https://medium.com/@timacosta06/smoothly-scrolling-a-lazycolumn-to-a-specific-position-with-animatescrolltoitem-cf07b208f746
/**
 * Composable to display the content of a chapter, split into chunks based on the screen layout.
 * Automatically scrolls to the relevant section based on the current element's order index.
 *
 * @param bookViewModel Manages chapter content and current element's position.
 * @param adaptiveNavigationType Determines chunk size for adaptive layout.
 * @param columnWidth Width of the content column, used for setting chunk sizes.
 */
@Composable
fun ChapterContent(bookViewModel: BookViewModel, adaptiveNavigationType: AdaptiveNavigationType, columnWidth: Int) {
    val chapterContent by bookViewModel.chapterContentLive.observeAsState(null)
    val listState = rememberLazyListState()
    val currentOrderIndex = bookViewModel.currentChapterElementOrderIndex
    // Only show content if chapterContent is not null
    if (chapterContent != null) {
        val chunkSize = calculateChunkSize(adaptiveNavigationType)
        val chunkedContent = chapterContent!!.chunked(chunkSize)
        // Trigger scroll when currentOrderIndex changes
        LaunchedEffect(currentOrderIndex) {
            // Find which chunk contains the element with the currentOrderIndex
            val targetColumn = chunkedContent.indexOfFirst { chunk ->
                chunk.any { element ->
                    when (element) {
                        is ChapterElement.ParagraphElement -> element.paragraph.orderIndex == currentOrderIndex
                        is ChapterElement.HeadingElement -> element.heading.orderIndex == currentOrderIndex
                        is ChapterElement.ImageElement -> element.image.orderIndex == currentOrderIndex
                        is ChapterElement.TableElement -> element.table.orderIndex == currentOrderIndex
                    }
                }
            }
            // The delay is necessary for the scroll to work
            if (targetColumn != -1) {
                listState.animateScrollToItem(targetColumn)
            }
        }
        // Keep track of where the user is scrolling to
        LaunchedEffect(listState) {
            snapshotFlow { listState.firstVisibleItemIndex }
                .collect { visibleIndex ->
                    // Find the orderIndex of the first element in the visible chunk
                    val visibleChunk = chunkedContent.getOrNull(visibleIndex)
                    val firstElementOrderIndex = visibleChunk?.firstOrNull()?.let { element ->
                        when (element) {
                            is ChapterElement.ParagraphElement -> element.paragraph.orderIndex
                            is ChapterElement.HeadingElement -> element.heading.orderIndex
                            is ChapterElement.ImageElement -> element.image.orderIndex
                            is ChapterElement.TableElement -> element.table.orderIndex
                        }
                    }
                    // Update the ViewModel's current order index
                    firstElementOrderIndex?.let { bookViewModel.setScrollCurrentChapterElementOrderIndex(it) }
                }
        }
        // The pages displaying the chapter's content
        Pages(listState, chunkedContent, columnWidth)
    }
}

/**
 * Displays individual pages of the chapter, each containing a chunk of elements.
 *
 * @param listState State of the LazyRow for horizontal scrolling.
 * @param chunkedContent List of chunks, where each chunk contains elements to be displayed on one page.
 * @param columnWidth Width of each column, used to determine the size of pages.
 */
@Composable
fun Pages(
    listState: LazyListState,
    chunkedContent: List<List<ChapterElement>>,
    columnWidth: Int
) {
    LazyRow(
        modifier = Modifier.fillMaxSize(),
        state = listState
    ) {
        items(chunkedContent) { chunk ->
            // One column == one page
            Column(
                modifier = Modifier
                    .width(with(LocalDensity.current) { columnWidth.toDp() })
                    .padding(dimensionResource(R.dimen.small_padding))
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center
            ){
                chunk.forEach { element ->
                    // One chunk == one element
                    Chunk(element)
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.small_height)))
                }
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.extra_large_height)))
            }
        }
    }
}

/**
 * Displays individual chapter elements such as paragraphs, headings, images, and tables.
 *
 * @param element The element to be displayed.
 */
@Composable
fun Chunk(element: ChapterElement) {
    when(element) {
        // Paragraph
        is ChapterElement.ParagraphElement -> {
            Text(text = element.paragraph.text)
        }
        // Heading
        is ChapterElement.HeadingElement -> {
            Text(text = element.heading.text,
                style = MaterialTheme.typography.headlineSmall
            )
        }
        // Image
        is ChapterElement.ImageElement -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center // Center image within the Box
            ) {
                AsyncImage(
                    model = element.image.imagePath,
                    contentDescription = element.image.altText,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        // Table
        is ChapterElement.TableElement -> {
            Table(element)
        }
    }
}

/**
 * Composable for rendering a table.
 * Converts the provided table HTML into a 2D list and displays the content in a grid-like structure.
 *
 * @param table The `ChapterElement.TableElement` containing the table's HTML representation.
 */
@Composable
fun Table(table: ChapterElement.TableElement) {
    val twoDimList = convertTableHtmlTo2DList(table.table.tableHtml)
    Column {
        twoDimList.forEach { row ->
            Row {
                row.forEach { cell ->
                    Text(
                        text = cell,
                        modifier = Modifier
                            .weight(1f)
                            .border(dimensionResource(R.dimen.small_border), Color.Black)
                            .padding(dimensionResource(R.dimen.small_padding))
                    )
                }
            }
        }
    }
}

/**
 * Composable for navigating between chapters.
 * Displays "Previous" and "Next" buttons to move across chapters based on the current chapter state.
 *
 * @param bookViewModel The `BookViewModel` managing the current chapter and navigation state.
 * @param navController The `NavHostController` used for navigation actions.
 */
@Composable
fun ChapterNavigation(bookViewModel: BookViewModel, navController: NavHostController) {
    val chapterCount by bookViewModel.getCurrentBookChapterCount().observeAsState()
    val currentChapter by bookViewModel.currentChapter.observeAsState()
    if (chapterCount != null && currentChapter != null) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.small_padding)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Previous chapter
            Button(
                // changing chapter requires updating a few variables
                onClick = { bookViewModel.updateCurrentChapter(currentChapter!!.orderIndex - 1)
                            bookViewModel.setCurrentChapterElementOrderIndex(1)
                            navController.navigate(NavRoutes.Reading.route)
                          },
                enabled = currentChapter!!.orderIndex > 1
            ) {
                Text(text = stringResource(R.string.previous_chapter))
            }
            // Next chapter
            Button(
                // changing chapter requires updating a few variables
                onClick = { bookViewModel.updateCurrentChapter(currentChapter!!.orderIndex + 1)
                            bookViewModel.setCurrentChapterElementOrderIndex(1)
                            navController.navigate(NavRoutes.Reading.route)
                          },
                enabled = currentChapter!!.orderIndex < chapterCount!!
            ) {
                Text(text = stringResource(R.string.next_chapter))
            }
        }
    }
}

/**
 * Composable for displaying a switch to toggle the reading mode.
 * Includes a label and a switch button.
 *
 * @param checked The current state of the switch (true for enabled, false for disabled).
 * @param onCheckedChange Callback invoked when the switch state is changed.
 * @param modifier Modifier to apply to the container.
 */
@Composable
fun ReadingModeSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = stringResource(R.string.reading_mode),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.small_width)))

        val newContentDescription = stringResource(R.string.toggle_reading_mode)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.semantics { contentDescription = newContentDescription }
        )
    }
}

// Helper function to parse HTML into a 2D List Array
private fun convertTableHtmlTo2DList(html: String): List<List<String>> {
    val twoDimList = mutableListOf<List<String>>()
    val table = Jsoup.parse(html)
    val rows = table.select("tr")
    for (row in rows) {
        val oneDimList = mutableListOf<String>()
        val tds = row.select("td")
        for (td in tds) {
            oneDimList.add(td.text())
        }
        twoDimList.add(oneDimList)
    }
    return twoDimList
}

// Returns the correct amount of chunk depending on screen size
private fun calculateChunkSize(adaptiveNavigationType: AdaptiveNavigationType): Int {
    val chunkSize = when (adaptiveNavigationType) {
        AdaptiveNavigationType.PERMANENT_NAVIGATION_DRAWER -> 3
        AdaptiveNavigationType.NAVIGATION_RAIL -> 2
        else -> 1 // Default chunk size for normal navigation
    }
    return chunkSize
}