package com.example.bookreadingapp.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import com.example.bookreadingapp.R

/**
 * Object that defines the items for the navigation bar in the application.
 * It provides a list of bar items, each representing a different navigation route,
 * along with their associated icons and titles.
 */
object NavBarItems {
    // List of bar items for the bottom navigation bar
    val BarItems = listOf(
        BarItem(
            titleRes = R.string.home_title,  // String resource ID for the title
            image = Icons.Filled.Home,  // Icon for the bar item
            route = NavRoutes.Home.route // Navigation route for the bar item
        ),
        BarItem(
            titleRes = R.string.library_title,
            image = Icons.Filled.Menu,
            route = NavRoutes.Library.route
        ),
        BarItem(
            titleRes = R.string.search_title,
            image = Icons.Filled.Search,
            route = NavRoutes.Search.route
        )
    )
}