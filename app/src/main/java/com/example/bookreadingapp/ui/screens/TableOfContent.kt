package com.example.bookreadingapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.bookreadingapp.R
import com.example.bookreadingapp.ui.navigation.NavRoutes
import com.example.bookreadingapp.ui.viewmodels.BookReadingAppViewModel
import com.example.bookreadingapp.ui.viewmodels.BookViewModel

/**
 * Composable that displays the Table of Content screen for the current book.
 * Shows a list of chapters and allows users to navigate to a specific chapter in the reading screen.
 * Includes a reading mode toggle switch at the top of the screen.
 *
 * @param navController Controller for navigating between different screens.
 * @param viewModel The ViewModel for managing app-level state, including reading mode.
 * @param bookViewModel The ViewModel for managing the current book and its chapters.
 */
@Composable
fun TableOfContent(
    navController: NavController,
    viewModel: BookReadingAppViewModel,
    bookViewModel: BookViewModel
) {
    val chapters by bookViewModel.getCurrentBookChapters().observeAsState(emptyList())

    Column(
        modifier = Modifier
            .padding(dimensionResource(R.dimen.small_padding))
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        ReadingModeSwitch(
            checked = viewModel.isReadingMode,
            onCheckedChange = { viewModel.toggleIsReadingMode() }
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.large_height)))
        // Page title
        Text(
            text = stringResource(R.string.table_of_content),
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.large_height)))
        // List of chapters
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(chapters.size) { index ->
                ChapterItem(
                    title = chapters[index].title,
                    onClick = {
                        bookViewModel.updateCurrentChapter(chapters[index].orderIndex)
                        bookViewModel.setCurrentChapterElementOrderIndex(1)
                        navController.navigate(NavRoutes.Reading.route)
                    }
                )
            }
        }
    }
}

/**
 * Composable that displays an individual chapter item in the Table of Content.
 *
 * @param title The title of the chapter to display.
 * @param onClick The action to perform when the chapter item is clicked, which is to go to
 * the reading screen.
 */
@Composable
fun ChapterItem(title: String, onClick: () -> Unit) {
    Text(
        text = title,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(dimensionResource(R.dimen.small_padding))
    )
}