package com.example.bookreadingapp.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bookreadingapp.R
import com.example.bookreadingapp.data.entities.Book
import com.example.bookreadingapp.ui.navigation.NavRoutes
import com.example.bookreadingapp.ui.viewmodels.BookProcessingViewModel
import com.example.bookreadingapp.ui.viewmodels.BookReadingAppViewModel
import com.example.bookreadingapp.ui.viewmodels.BookViewModel

/**
 * Composable that displays the library screen of the app.
 * Automatically downloads books from provided URLs and displays them in a LazyRow.
 *
 * @param navController Controller for navigating between different screens.
 * @param viewModel The ViewModel managing the state of downloaded books and directory contents.
 * @param bookProcessingViewModel ViewModel responsible for downloading and processing books.
 * @param bookViewModel ViewModel for managing book-related data, such as the list of books.
 */
@Composable
fun Library(
    navController: NavController,
    viewModel: BookReadingAppViewModel,
    bookProcessingViewModel: BookProcessingViewModel,
    bookViewModel: BookViewModel,
) {
    val downloadProgress by bookProcessingViewModel.downloadProgress.observeAsState(0)
    val urlsToProcess = viewModel.urlsToDownload.toList()
    for (url in urlsToProcess) {
        bookProcessingViewModel.downloadBook(url)
        viewModel.downloadedUrl(url)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.medium_padding))
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Screen title
        Text(
            text = stringResource(R.string.your_bookshelf),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.large_height)))
        BookCards(bookViewModel, navController)
        if (downloadProgress < 100 && downloadProgress != 0) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(stringResource(R.string.downloading) + downloadProgress + stringResource(R.string.percentage), style = MaterialTheme.typography.bodyLarge)
            }
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.large_height)))
        // Available for download
        Text(
            text = stringResource(R.string.available_for_download),
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.medium_height)))
        // Books available for download
        for (url in viewModel.urlsAvailableForDownload) {
            ClickableUrl(url, viewModel, true, R.string.download)
        }
    }
}

/**
 * Displays a horizontal list of book cards.
 *
 * @param bookViewModel ViewModel providing the list of books.
 * @param navController Controller for navigating to other screens.
 */
@Composable
fun BookCards(
    bookViewModel: BookViewModel,
    navController: NavController
) {
    val books by bookViewModel.allBooks.observeAsState(emptyList())
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        items(books.size) { index ->
            BookCard(
                navController = navController,
                bookViewModel = bookViewModel,
                book = books[index]
            )
        }
    }
}

/**
 * Displays a card for an individual book.
 * Each card is clickable and navigates to the Table of Contents screen when clicked.
 *
 * @param navController Controller for navigation.
 * @param bookViewModel ViewModel for managing book state.
 * @param book Data object representing the book to display.
 */
@Composable
fun BookCard(
    navController: NavController,
    bookViewModel: BookViewModel,
    book: Book
) {
    Card (
        modifier = Modifier.padding(dimensionResource(R.dimen.small_padding)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .clickable {
                    bookViewModel.setCurrentBook(book)
                    navController.navigate(NavRoutes.TableOfContent.route)
                }
                .width(dimensionResource(R.dimen.book_card_width))
                .padding(dimensionResource(R.dimen.small_padding)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Book Image Cover
            AsyncImage(
                model = book.coverImgPath,
                contentDescription = book.title,
                modifier = Modifier
                    .width(dimensionResource(R.dimen.book_image_size)),
                contentScale = ContentScale.FillHeight
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.small_height)))
            // Book Title
            Text(
                text = book.title,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.tertiary,
                textAlign = TextAlign.Center,
            )
            // Book Author
            Text(
                text = book.author,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                textAlign = TextAlign.Center,
            )
        }
    }
}

/**
 * Displays a clickable URL to download a book.
 *
 * @param url The URL to display.
 * @param viewModel ViewModel managing download actions.
 * @param enabled Whether the button should be enabled.
 * @param actionText Resource ID for the action button text.
 * @param modifier Modifier for customizing the appearance of the row.
 */
@Composable
fun ClickableUrl(
    url: String,
    viewModel: BookReadingAppViewModel,
    enabled: Boolean,
    @StringRes actionText: Int,
    modifier: Modifier = Modifier
) {
    Row (
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = dimensionResource(R.dimen.small_padding)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Link of book
        Text(
            text = url,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyLarge
        )
        // Button to download book
        Button(
            onClick = { viewModel.downloadUrl(url) },
            enabled = enabled
        ) {
            Text(
                text = stringResource(actionText),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}