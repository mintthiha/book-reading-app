package com.example.bookreadingapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.navigation.compose.rememberNavController
import com.example.bookreadingapp.data.entities.Book
import com.example.bookreadingapp.ui.BookReadingApp
import com.example.bookreadingapp.ui.navigation.NavRoutes
import com.example.bookreadingapp.ui.theme.BookReadingAppTheme
import com.example.bookreadingapp.ui.viewmodels.BookProcessingViewModel
import com.example.bookreadingapp.ui.viewmodels.BookReadingAppViewModel
import com.example.bookreadingapp.ui.viewmodels.BookViewModel
import com.example.bookreadingapp.ui.viewmodels.ViewModelFactory
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass as calculateWindowSizeClass1

/**
 * The main entry point of the application.
 * This activity is responsible for initializing the app and setting up the Compose UI with a theme, navigation controller,
 * and view model.
 */
/**
 * Reference to save last screen : https://developer.android.com/training/data-storage/shared-preferences
 * */

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        // Lazily initializes the view models using a factory pattern to pass required dependencies.
        // This is the teacher's code.
        val bookViewModel: BookViewModel by viewModels {
            ViewModelFactory(this.application, sharedPreferences)
        }
        val bookProcessingViewModel: BookProcessingViewModel by viewModels {
            ViewModelFactory(this.application, sharedPreferences)
        }
        val viewModel: BookReadingAppViewModel by viewModels {
            ViewModelFactory(this.application, sharedPreferences)
        }

        restoreBookViewModelPartially(sharedPreferences, bookViewModel)
        val lastScreen = getLastScreen(sharedPreferences)
        setContent {
            BookReadingAppTheme {
                val windowSize = calculateWindowSizeClass1(this)
                val navController = rememberNavController()


                // use the last saved screen as the start destination
                val startDestination = lastScreen
                BookReadingApp(
                    windowSizeClass = windowSize.widthSizeClass,
                    viewModel = viewModel,
                    bookViewModel = bookViewModel,
                    bookProcessingViewModel = bookProcessingViewModel,
                    navController = navController,
                    startDestination = startDestination,
                )
            }
        }
    }

    override fun onPause() {
        super.onPause()
        saveState()
    }

    override fun onStop() {
        super.onStop()
        saveState()
    }

    override fun onDestroy() {
        super.onDestroy()
        saveState()
    }

    private fun saveState() {
        val sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val savedOrderIndex = sharedPreferences.getInt("current_scroll_chapter_element_order_index", 1)
        sharedPreferences.edit().putInt("current_chapter_element_order_index", savedOrderIndex).apply()
    }

    // Restores certain values such as the current book, current chapter, etc.
    private fun restoreBookViewModelPartially(
        sharedPreferences: SharedPreferences, bookViewModel: BookViewModel
    ) {
        // Set the current book if there was one previously
        val currentBook = sharedPreferences.getString("current_book", "")
        if (currentBook.isNullOrEmpty()) return
        val currentBookList = currentBook.split("###")
        if (currentBookList.size != 4) {
            throw IllegalArgumentException("current_book in Shared Preferences must be a list of four elements.")
        }
        val currentBookEntity = Book(currentBookList[0].toInt(), currentBookList[1], currentBookList[2], currentBookList[3])
        bookViewModel.setCurrentBook(currentBookEntity)
        // Set the current chapter if there was one previously
        val currentChapterOrderIndex = sharedPreferences.getInt("current_chapter_order_index", -1)
        if (currentChapterOrderIndex == -1) return
        bookViewModel.updateCurrentChapter(currentChapterOrderIndex)
        // Set the current chapter element if there was one previously
        val currentChapterElementOrderIndex = sharedPreferences.getInt("current_chapter_element_order_index", -1)
        if (currentChapterElementOrderIndex == -1) return
        bookViewModel.setCurrentChapterElementOrderIndex(currentChapterElementOrderIndex)
    }

    // get the last active screen route saved in SharedPreferences
    private fun getLastScreen(sharedPreferences: SharedPreferences): String {
        val savedRoute = sharedPreferences.getString("last_route", NavRoutes.Home.route) ?: NavRoutes.Home.route
        val validRoutes = listOf(
            NavRoutes.Home.route,
            NavRoutes.Library.route,
            NavRoutes.Reading.route,
            NavRoutes.Search.route,
            NavRoutes.TableOfContent.route
        )
        return if (savedRoute in validRoutes) savedRoute else NavRoutes.Home.route
    }
}