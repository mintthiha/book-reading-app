package com.example.bookreadingapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bookreadingapp.R
import com.example.bookreadingapp.domain.ChapterElement
import com.example.bookreadingapp.ui.navigation.NavRoutes
import com.example.bookreadingapp.ui.viewmodels.BookReadingAppViewModel
import com.example.bookreadingapp.ui.viewmodels.BookViewModel

/**
 * Composable for performing a search within a book.
 * Displays the current book information, a search input field, a search button, and search results.
 *
 * @param navController The NavController to handle navigation.
 * @param viewModel The ViewModel for managing search state and query.
 * @param bookViewModel The ViewModel for managing book and chapter-related data.
 */
@Composable
fun Search(
    navController: NavController,
    viewModel: BookReadingAppViewModel,
    bookViewModel: BookViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.medium_padding))
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        CurrentBook(bookViewModel)

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.large_height)))

        Text(
            text = stringResource(R.string.search_within_book),
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.medium_height)))

        // Search field to look for occurences
        TextField(
            value = viewModel.query,
            onValueChange = { viewModel.updateQuery(it)
                            },
            placeholder = {
                Text(
                    text = stringResource(R.string.search),
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(R.dimen.medium_padding))
                .testTag("SearchField")
        )
        // Button to perform search
        SearchButton(bookViewModel, viewModel)
        // List of occurences
        Results(bookViewModel, viewModel, navController)
    }
}

/**
 * Composable for the search button and error handling.
 * Checks the query length and updates the search results in the ViewModel.
 *
 * @param bookViewModel The ViewModel for managing book and chapter-related data.
 * @param viewModel The ViewModel for managing search query and state.
 */
@Composable
fun SearchButton(
    bookViewModel: BookViewModel,
    viewModel: BookReadingAppViewModel
) {
    val error = remember { mutableStateOf<Int?>(null) }
    if (error.value != null) {
        Text(
            text = stringResource(error.value!!),
            color = MaterialTheme.colorScheme.error
        )
    }
    Button(
        onClick = {
            // Query must be at least three characters otherwise UI thread gets blocked
            if (bookViewModel.currentBook != null && viewModel.query.isNotEmpty()) {
                if (viewModel.query.length < 3) {
                    error.value = R.string.search_error_three_char_min
                    return@Button
                }
                error.value = null
                viewModel.updateSavedQuery()
                bookViewModel.updateCurrentBookChapterElementsWithOccurrence(viewModel.savedQuery)
            }
        },
        modifier = Modifier.padding(bottom = dimensionResource(R.dimen.medium_padding))) {
        Text(text = stringResource(R.string.search_within_book))
    }
}

/**
 * Composable to display details of the current book.
 * If no book is selected, prompts the user to select one.
 *
 * @param bookViewModel The ViewModel providing the current book data.
 */
@Composable
fun CurrentBook(bookViewModel: BookViewModel) {
    val book = bookViewModel.currentBook
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.medium_padding)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (book != null){
            // Book cover images
            AsyncImage(
                model  = book.coverImgPath,
                contentDescription = book.title,
                modifier = Modifier
                    .size(dimensionResource(R.dimen.book_image_size))
            )
            // Book title
            Text(
                text = book.title,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(top = dimensionResource(R.dimen.medium_padding))
            )
        } else {
            // Message that a book must be selected to perform search
            Text(
                text = stringResource(R.string.select_book),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = dimensionResource(R.dimen.medium_padding))
            )
        }
    }
}

/**
 * Displays multiple search result items for a given text and query.
 * Highlights all occurrences of the query within the text.
 *
 * @param text The text to search within.
 * @param query The search query.
 * @param onClick Callback triggered when a result is clicked.
 */
@Composable
fun SearchResultItems(text: String, query: String, onClick: () -> Unit) {
    var startIndex = 0
    while (startIndex < text.length) {
        // Find the next occurrence of the query in the text
        val queryIndex = text.indexOf(query, startIndex)
        if (queryIndex == -1) break
        // Calculate the beginning and the end of the occurrence snippet to display
        val snippetStart = maxOf(0, queryIndex - 50)
        val snippetEnd = minOf(text.length, queryIndex + query.length + 50)

        val before = stringResource(R.string.dots) + text.substring(snippetStart, queryIndex)
        val after = text.substring(queryIndex + query.length, snippetEnd) + stringResource(R.string.dots)
        SearchResultItem(onClick, before, query, after)
        // Update startIndex to the index right after the query occurrence's last index
        startIndex = queryIndex + query.length
    }
}

/**
 * Displays an individual search result with the query highlighted.
 *
 * @param onClick Callback triggered when the result is clicked.
 * @param before Text preceding the query in the snippet.
 * @param query The query to highlight.
 * @param after Text following the query in the snippet.
 */
@Composable
fun SearchResultItem(
    onClick: () -> Unit,
    before: String,
    query: String,
    after: String
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        )
    ) {
        // Inspired from https://developer.android.com/develop/ui/compose/text/style-text
        Text(buildAnnotatedString {
            // Text before the occurence
            withStyle(style = SpanStyle(
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize)
            ) {
                append(before)
            }
            // The occurence
            withStyle(style = SpanStyle(
                color = MaterialTheme.colorScheme.onTertiary,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                background = MaterialTheme.colorScheme.tertiary)
            ) {
                append(query)
            }
            // Text after the occurence
            withStyle(style = SpanStyle(
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize)
            ) {
                append(after)
            }
        })
    }
}

/**
 * Composable to display search results.
 * Renders a message if no results are found.
 *
 * @param bookViewModel The ViewModel providing chapter and element data.
 * @param viewModel The ViewModel managing the search query and state.
 * @param navController The NavController for navigation to the result location.
 */
@Composable
fun Results(bookViewModel: BookViewModel, viewModel: BookReadingAppViewModel, navController: NavController){
    val chapterToElementsMapLive by bookViewModel.chapterToElementsMapLive.observeAsState(null)
    if (chapterToElementsMapLive != null) {
        // If there are no key value pair in the Map, display no results found
        if (chapterToElementsMapLive!!.isEmpty()) {
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.medium_height)))
            Text(
                text = stringResource(R.string.search_not_found),
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Column{
            // For each chapter
            for ((chapter, chapterElements) in chapterToElementsMapLive!!) {
                // For each element in this chapter
                for (element in chapterElements) {
                    // Display result if it's paragraph
                    if (element is ChapterElement.ParagraphElement) {
                        SearchResultItems(element.paragraph.text, viewModel.savedQuery) {
                            bookViewModel.updateCurrentChapter(chapter.orderIndex)
                            bookViewModel.setCurrentChapterElementOrderIndex(element.paragraph.orderIndex)
                            navController.navigate(NavRoutes.Reading.route)
                        }
                    }
                    // Display result if it's a heading
                    if (element is ChapterElement.HeadingElement) {
                        SearchResultItems(element.heading.text, viewModel.savedQuery) {
                            bookViewModel.updateCurrentChapter(chapter.orderIndex)
                            bookViewModel.setCurrentChapterElementOrderIndex(element.heading.orderIndex)
                            navController.navigate(NavRoutes.Reading.route)
                        }
                    }
                }
            }
        }
    } else {
        // Display the following if no query search has been performed
        Text(
            text = stringResource(R.string.enter_query)
        )
    }
}