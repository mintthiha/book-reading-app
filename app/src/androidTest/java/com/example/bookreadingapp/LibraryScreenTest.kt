//package com.example.bookreadingapp
//
//import androidx.compose.ui.test.assertIsDisplayed
//import androidx.compose.ui.test.junit4.createComposeRule
//import androidx.compose.ui.test.onNodeWithText
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.compose.rememberNavController
//import com.example.bookreadingapp.ui.screens.Library
//import com.example.bookreadingapp.ui.theme.BookReadingAppTheme
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//
///**
// * Tests and test structure heavily rely on the class lecture 8, codelab testing and Crdavis gitlab coding example.
// * References:
// * https://gitlab.com/crdavis/testingexamplecode
// * https://github.com/google-developer-training/basic-android-kotlin-compose-training-tip-calculator/tree/test_solution
// */
//
//class LibraryScreenTest {
//
//    @get:Rule
//    val composeTestRule = createComposeRule()
//
//    @Before
//    fun setUp() {
//        composeTestRule.setContent {
//            BookReadingAppTheme {
//                val navController = rememberNavController()
//                Library(navController = navController)
//            }
//        }
//    }
//
//    @Test
//    fun libraryScreenDisplaysTitle() {
//        composeTestRule
//            .onNodeWithText(composeTestRule.activity.getString(R.string.your_bookshelf))
//            .assertIsDisplayed()
//    }
//
//    @Test
//    fun libraryScreenDisplaysBookTitles() {
//        // Change the following test to check that the count of book displayed
//        // is the same as the length of the string resource array book_urls
//        // The test might fail due to the books being downloaded asynchronously and
//        // therefore not appearing right away
//        for (i in 1..5) {
//            composeTestRule
//                .onNodeWithText(composeTestRule.activity.getString(R.string.book_template_name, i))
//                .assertIsDisplayed()
//        }
//    }
//}
