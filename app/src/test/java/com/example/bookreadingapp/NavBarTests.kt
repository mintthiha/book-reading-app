package com.example.bookreadingapp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import com.example.bookreadingapp.ui.navigation.NavBarItems.BarItems
import com.example.bookreadingapp.ui.navigation.NavRoutes
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Tests and test structure heavily rely on the class lecture 8, codelab testing and Crdavis gitlab coding example.
 * References:
 * https://gitlab.com/crdavis/testingexamplecode
 * https://github.com/google-developer-training/basic-android-kotlin-compose-training-tip-calculator/tree/test_solution
 */

class NavBarTests {

    @Test
    fun barItemsHaveCorrectNumberOfItems() {
        assertEquals(3, BarItems.size)
    }

    @Test
    fun barItemsHaveCorrectRoutes() {
        val expectedRoutes = listOf(NavRoutes.Home.route, NavRoutes.Library.route, NavRoutes.Search.route)
        val actualRoutes = BarItems.map { it.route }

        assertEquals(expectedRoutes, actualRoutes)
    }

    @Test
    fun barItemsHaveCorrectIcons() {
        assertEquals(Icons.Filled.Home, BarItems[0].image)
        assertEquals(Icons.Filled.Menu, BarItems[1].image)
        assertEquals(Icons.Filled.Search, BarItems[2].image)
    }
    

}
