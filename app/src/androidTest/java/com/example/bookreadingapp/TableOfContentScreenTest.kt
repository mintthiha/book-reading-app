//package com.example.bookreadingapp
//
//import androidx.compose.ui.test.assertIsDisplayed
//import androidx.compose.ui.test.junit4.createComposeRule
//import androidx.compose.ui.test.onNodeWithContentDescription
//import androidx.compose.ui.test.onNodeWithTag
//import androidx.compose.ui.test.onNodeWithText
//import androidx.compose.ui.test.performScrollTo
//import com.example.bookreadingapp.ui.viewmodels.BookReadingAppViewModel
//import com.example.bookreadingapp.ui.screens.TableOfContent
//import com.example.bookreadingapp.ui.theme.BookReadingAppTheme
//import androidx.compose.ui.test.performClick
//import androidx.navigation.compose.rememberNavController
//import org.junit.Assert.assertFalse
//import org.junit.Assert.assertTrue
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
//class TableOfContentScreenTest {
//
//    @get:Rule
//    val composeTestRule = createComposeRule()
//    private lateinit var viewModel: BookReadingAppViewModel
//
//
//    @Before
//    fun setUp() {
//        viewModel = BookReadingAppViewModel()
//
//        composeTestRule.setContent {
//            BookReadingAppTheme {
//                val navController = rememberNavController()
//                TableOfContent(navController = navController, viewModel = viewModel)
//            }
//        }
//    }
//    @Test
//    fun tableOfContentDisplaysTitleAndChapters() {
//        composeTestRule
//            .onNodeWithText(composeTestRule.activity.getString(R.string.table_of_content))
//            .assertIsDisplayed()
//
//        val chapters = listOf(
//            "Chapter 1: Introduction",
//            "Chapter 2: How to Get Better",
//            "Chapter 3: Advanced Techniques",
//            "Chapter 4: Where to Start",
//            "Chapter 5: Conclusion"
//        )
//
//        chapters.forEach { chapter ->
//            composeTestRule.onNodeWithText(chapter).performScrollTo().assertIsDisplayed()
//        }
//    }
//
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
//            .onNodeWithContentDescription(composeTestRule.activity.getString(R.string.toggle_reading_mode))
//            .performClick()
//        assertFalse(viewModel.isReadingMode)
//
//        composeTestRule
//            .onNodeWithContentDescription(composeTestRule.activity.getString(R.string.toggle_reading_mode))
//            .performClick()
//        assertTrue(viewModel.isReadingMode)
//    }
//
//
//
//}
