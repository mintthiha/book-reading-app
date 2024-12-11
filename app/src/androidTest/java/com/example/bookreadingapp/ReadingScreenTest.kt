//package com.example.bookreadingapp
//
//import androidx.compose.ui.test.assertIsDisplayed
//import androidx.compose.ui.test.junit4.createComposeRule
//import androidx.compose.ui.test.onNodeWithText
//import androidx.compose.ui.test.onNodeWithContentDescription
//import androidx.compose.ui.test.performClick
//import androidx.navigation.compose.rememberNavController
//import com.example.bookreadingapp.ui.screens.Reading
//import com.example.bookreadingapp.ui.theme.BookReadingAppTheme
//import com.example.bookreadingapp.ui.viewmodels.BookReadingAppViewModel
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.Assert.assertFalse
//import org.junit.Assert.assertTrue
//
///**
// * Tests and test structure heavily rely on the class lecture 8, codelab testing and Crdavis gitlab coding example.
// * References:
// * https://gitlab.com/crdavis/testingexamplecode
// * https://github.com/google-developer-training/basic-android-kotlin-compose-training-tip-calculator/tree/test_solution
// */
//
//class ReadingScreenTest {
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
//                Reading(navController = navController, viewModel = viewModel)
//            }
//        }
//    }
//
//    @Test
//    fun buttonTogglesReadingMode() {
//        composeTestRule
//            .onNodeWithText(composeTestRule.activity.getString(R.string.reading_mode))
//            .assertIsDisplayed()
//
//        assertTrue(viewModel.isReadingMode)
//
//        composeTestRule
//            .onNodeWithText(composeTestRule.activity.getString(R.string.toggle_reading_mode))
//            .performClick()
//        assertFalse(viewModel.isReadingMode)
//
//        composeTestRule
//            ..onNodeWithText(composeTestRule.activity.getString(R.string.toggle_reading_mode))
//            .performClick()
//        assertTrue(viewModel.isReadingMode)
//    }
//}
