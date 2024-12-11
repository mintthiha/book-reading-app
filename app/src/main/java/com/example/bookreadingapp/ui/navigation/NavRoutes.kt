package com.example.bookreadingapp.ui.navigation

/**
 * Sealed class representing the navigation routes for the application.
 * Each route corresponds to a screen in the app, and its associated navigation path is defined as a string.
 *
 * Sealed classes allow us to create a restricted hierarchy of routes, ensuring all possible routes are explicitly defined.
 */
sealed class NavRoutes(val route: String) {

    /**
     * Navigation route for the Home screen.
     * - `route`: The string identifier for the Home screen ("home").
     */
    data object Home : NavRoutes("home")

    /**
     * Navigation route for the Library screen.
     * - `route`: The string identifier for the Library screen ("library").
     */
    data object Library : NavRoutes("library")

    /**
     * Navigation route for the Reading screen.
     * - `route`: The string identifier for the Reading screen ("reading").
     */
    data object Reading : NavRoutes("reading")

    /**
     * Navigation route for the Search screen.
     * - `route`: The string identifier for the Search screen ("search").
     */
    data object Search : NavRoutes("search")

    /**
     * Navigation route for the Table of Content screen.
     * - `route`: The string identifier for the Table of Content screen ("tableOfContent").
     */
    data object TableOfContent : NavRoutes("tableOfContent")
}
