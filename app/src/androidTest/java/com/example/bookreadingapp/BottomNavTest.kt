package com.example.bookreadingapp

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

/**
 * Tests and test structure heavily rely on the class lecture 8, codelab testing and Crdavis gitlab coding example.
 * References:
 * https://gitlab.com/crdavis/testingexamplecode
 * https://github.com/google-developer-training/basic-android-kotlin-compose-training-tip-calculator/tree/test_solution
 */

class BottomNavTest {
//we use <MainActivity> because we are relying on the entire activity not just one composable
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun checkBottomNavigationWorks() {
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.home_title))
            .assertExists()
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.library_title))
            .assertExists()
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.search_title))
            .assertExists()
    }

    @Test
    fun navigateToHomeScreen() {
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.home_title))
            .performClick()
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.home_welcome_message))
            .assertExists()
    }

    @Test
    fun navigateToLibraryScreen() {
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.library_title))
            .performClick()
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.your_bookshelf))
            .assertExists()
    }

    @Test
    fun navigateToSearchScreen() {
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.search_title))
            .performClick()
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.search_within_book))
            .assertExists()
    }
}
