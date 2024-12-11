//package com.example.bookreadingapp
//
//import androidx.compose.ui.test.assertIsDisplayed
//import androidx.compose.ui.test.junit4.createComposeRule
//import androidx.compose.ui.test.onNodeWithText
//import androidx.compose.ui.test.onNodeWithTag
//import androidx.compose.ui.test.performTextInput
//import androidx.navigation.compose.rememberNavController
//import com.example.bookreadingapp.ui.screens.Search
//import com.example.bookreadingapp.ui.theme.BookReadingAppTheme
//import com.example.bookreadingapp.ui.viewmodels.BookReadingAppViewModel
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
//class SearchScreenTest {
//
//    @get:Rule
//    val composeTestRule = createComposeRule()
//
//    private lateinit var viewModel: BookReadingAppViewModel
//
//    @Before
//    fun setUp() {
//        viewModel = BookReadingAppViewModel()
//        composeTestRule.setContent {
//            BookReadingAppTheme {
//                val navController = rememberNavController()
//                Search(navController = navController, viewModel = viewModel)
//            }
//        }
//    }
//
//    @Test
//    fun searchScreenDisplaysSearchPrompt() {
//        composeTestRule
//            .onNodeWithText(composeTestRule.activity.getString(R.string.search_within_book))
//            .assertIsDisplayed()
//    }
//
//    @Test
//    fun typingInSearchFieldUpdatesQuery() {
//        composeTestRule
//            .onNodeWithTag("SearchField")
//            .performTextInput("Lorem")
//
//        composeTestRule
//            .onNodeWithText("Lorem")
//            .assertIsDisplayed()
//    }
//}
