//package com.example.bookreadingapp
//
//import androidx.compose.ui.test.assertHasClickAction
//import androidx.compose.ui.test.assertIsDisplayed
//import androidx.compose.ui.test.junit4.createAndroidComposeRule
//import androidx.compose.ui.test.onNodeWithText
//import androidx.compose.ui.test.performClick
//import androidx.navigation.compose.rememberNavController
//import com.example.bookreadingapp.ui.screens.Home
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
//class HomeScreenTest {
//
//    @get:Rule
//    val composeTestRule = createAndroidComposeRule<MainActivity>()
//
//    @Before
//    fun setup() {
//        composeTestRule.setContent {
//            BookReadingAppTheme {
//                val navController = rememberNavController()
//                Home(navController = navController)
//            }
//        }
//    }
//
//    @Test
//    fun homeScreenDisplaysWelcomeMessage() {
//        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.home_welcome_message))
//            .assertIsDisplayed()
//    }
//
//    @Test
//    fun homeScreenDisplaysGoToLibraryButton() {
//        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.go_to_library))
//            .assertIsDisplayed()
//            .assertHasClickAction()
//    }
//
//    @Test
//    fun clickingGoToLibraryButtonNavigates() {
//        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.go_to_library))
//            .performClick()
//        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.your_bookshelf))
//            .assertIsDisplayed()
//    }
//
//    @Test
//    fun homeScreenDisplaysQuote() {
//        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.home_welcome_quote))
//            .assertIsDisplayed()
//        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.john_locke))
//            .assertIsDisplayed()
//    }
//
//    @Test
//    fun homeScreenElementsAreProperlyArranged() {
//        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.home_welcome_message))
//            .assertIsDisplayed()
//        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.go_to_library))
//            .assertIsDisplayed()
//        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.home_welcome_quote))
//            .assertIsDisplayed()
//        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.john_locke))
//            .assertIsDisplayed()
//    }
//}
