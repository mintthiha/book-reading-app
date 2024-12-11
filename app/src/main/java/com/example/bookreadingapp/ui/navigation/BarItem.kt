package com.example.bookreadingapp.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector


/**
 * A data class representing an item in a navigation bar.
 * Each bar item contains a title, an icon, and a navigation route.
 * This is the teacher's code.
 *
 * @property titleRes The string resource ID for the item's title.
 *                    This allows the title to be localized using Android's resource system.
 * @property image The icon to be displayed for the navigation bar item, represented as an `ImageVector`.
 * @property route The navigation route associated with this item. This route is used for navigation in the app.
 */
data class BarItem(
    @StringRes val titleRes: Int,  // Store the string resource ID
    val image: ImageVector, // The vector image representing the item's icon.
    val route: String // The navigation route corresponding to this item.
)