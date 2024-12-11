package com.example.bookreadingapp.ui

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Compact
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Medium
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bookreadingapp.R
import com.example.bookreadingapp.ui.navigation.NavBarItems
import com.example.bookreadingapp.ui.navigation.NavRoutes
import com.example.bookreadingapp.ui.screens.Home
import com.example.bookreadingapp.ui.screens.Library
import com.example.bookreadingapp.ui.screens.Reading
import com.example.bookreadingapp.ui.screens.Search
import com.example.bookreadingapp.ui.screens.TableOfContent
import com.example.bookreadingapp.ui.utils.AdaptiveNavigationType
import com.example.bookreadingapp.ui.viewmodels.BookProcessingViewModel
import com.example.bookreadingapp.ui.viewmodels.BookReadingAppViewModel
import com.example.bookreadingapp.ui.viewmodels.BookViewModel


/**
 * The main composable function that defines the structure of the Book Reading app.
 * It adapts navigation based on the window size class (compact, medium, or large screens).
 *
 * @param windowSizeClass Determines the window size class for adaptive navigation.
 * @param modifier Modifier to apply to the root composable.
 * @param navController The navigation controller managing screen navigation.
 * @param viewModel The main ViewModel that holds app-wide data.
 * @param bookViewModel A ViewModel that manages book-related data.
 * @param bookProcessingViewModel A ViewModel that handles book processing (e.g., progress).
 * @param startDestination The initial screen to navigate to on app launch.
 */
@Composable
fun BookReadingApp(
    windowSizeClass: WindowWidthSizeClass,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: BookReadingAppViewModel,
    bookViewModel: BookViewModel,
    bookProcessingViewModel: BookProcessingViewModel,
    startDestination: String

) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val context = LocalContext.current
    // save user last visited screen in SharedPreferences everytime nav change (cause user could exit any time)
    LaunchedEffect(currentRoute) {
        currentRoute?.let { saveLastLocation(it, context) }
    }


    val adaptiveNavigationType = when (windowSizeClass) {
        Compact -> AdaptiveNavigationType.BOTTOM_NAVIGATION
        Medium -> AdaptiveNavigationType.NAVIGATION_RAIL
        else -> AdaptiveNavigationType.PERMANENT_NAVIGATION_DRAWER
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            TopBar(currentRoute)
        },
        content = { paddingValues ->
            Content(
                navController = navController,
                viewModel = viewModel,
                bookProcessingViewModel = bookProcessingViewModel,
                bookViewModel = bookViewModel,
                adaptiveNavigationType = adaptiveNavigationType,
                startDestination = startDestination,
                modifier = Modifier.padding(paddingValues)
            )
        },
        bottomBar = {
            BottomBar(
                navController = navController,
                viewModel = viewModel,
                adaptiveNavigationType = adaptiveNavigationType,
                currentRoute = currentRoute
            )
        }
    )
}
fun saveLastLocation(route: String, context: Context) {
    val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    sharedPreferences.edit().putString("last_route", route).apply()
}




/**
 * Composable for the app's top bar. Displays the app name and logo.
 * This is the teacher's code.
 *
 * @param currentRoute The current route of the navigation to determine visibility.
 * @param modifier Modifier to apply to the top bar.
 */
@Composable
fun TopBar(
    currentRoute: String?,
    modifier: Modifier = Modifier,
) {
    if (currentRoute != NavRoutes.Reading.route && currentRoute != NavRoutes.TableOfContent.route) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = dimensionResource(R.dimen.small_padding)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.displayLarge
            )
            Image(
                painter = painterResource(R.drawable.logo_no_bg),
                contentDescription = stringResource(R.string.logo),
                modifier = Modifier
                    .size(dimensionResource(R.dimen.logo_size))
                    .clip(RoundedCornerShape(dimensionResource(R.dimen.round_edge)))
            )
        }
    }
}


/**
 * Composable to manage the main content of the app based on navigation and adaptive navigation type.
 * This is the teacher's code.
 *
 * @param navController Navigation controller for managing screen navigation.
 * @param viewModel The ViewModel managing app-wide state and data.
 * @param adaptiveNavigationType Determines the type of navigation (bottom bar, rail, or drawer).
 * @param currentRoute The current route of navigation to determine displayed content.
 * @param modifier Modifier to apply to the content container.
 */
@Composable
fun Content(
    navController: NavHostController,
    viewModel: BookReadingAppViewModel,
    bookProcessingViewModel: BookProcessingViewModel,
    bookViewModel: BookViewModel,
    adaptiveNavigationType: AdaptiveNavigationType,
    startDestination: String,
    modifier: Modifier = Modifier
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    var displayNavigation = false
    if (currentRoute != NavRoutes.TableOfContent.route && currentRoute != NavRoutes.Reading.route) {
        displayNavigation = true
    }
    if ((currentRoute == NavRoutes.Reading.route || currentRoute == NavRoutes.TableOfContent.route)
        && !viewModel.isReadingMode) {
        displayNavigation = true
    }

    Row(modifier = modifier.padding(start = dimensionResource(R.dimen.small_padding))) {
        if (displayNavigation) {
            // Handle the navigation type
            when (adaptiveNavigationType) {
                AdaptiveNavigationType.PERMANENT_NAVIGATION_DRAWER -> {
                    PermanentNavigationDrawerComponent(
                        navController = navController,
                        viewModel = viewModel,
                        bookProcessingViewModel = bookProcessingViewModel,
                        bookViewModel = bookViewModel,
                        currentRoute = currentRoute,
                        adaptiveNavigationType = adaptiveNavigationType,
                        startDestination = startDestination
                    )
                }
                AdaptiveNavigationType.NAVIGATION_RAIL -> {
                    NavigationRailComponent(
                        navController = navController,
                        currentRoute = currentRoute
                    )
                }
                AdaptiveNavigationType.BOTTOM_NAVIGATION -> {
                }
            }
        }

        if(adaptiveNavigationType != AdaptiveNavigationType.PERMANENT_NAVIGATION_DRAWER || !displayNavigation){
            Box(modifier = Modifier.fillMaxSize()) {
                NavigationHost(
                    navController = navController,
                    viewModel = viewModel,
                    bookProcessingViewModel = bookProcessingViewModel,
                    bookViewModel = bookViewModel,
                    adaptiveNavigationType = adaptiveNavigationType,
                    startDestination = startDestination
                )
            }
        }
    }
}

/**
 * Composable for managing navigation between screens via a navigation host.
 * This is the teacher's code
 *
 * @param navController Navigation controller for managing navigation actions.
 * @param viewModel The ViewModel managing app-wide data and state.
 */
@Composable
fun NavigationHost(
    navController: NavHostController,
    viewModel: BookReadingAppViewModel,
    bookViewModel: BookViewModel,
    bookProcessingViewModel: BookProcessingViewModel,
    adaptiveNavigationType: AdaptiveNavigationType,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination =startDestination // The initial screen
    ) {
        composable(NavRoutes.Home.route) {
            Home(navController = navController)
        }
        composable(NavRoutes.Library.route) {
            Library(
                navController = navController, viewModel = viewModel,
                bookProcessingViewModel = bookProcessingViewModel, bookViewModel = bookViewModel)
        }
        composable(NavRoutes.Reading.route) {
            Reading(viewModel = viewModel, bookViewModel = bookViewModel,
                adaptiveNavigationType, navController)
        }
        composable(NavRoutes.Search.route) {
            Search(viewModel = viewModel, bookViewModel = bookViewModel, navController = navController)
        }
        composable(NavRoutes.TableOfContent.route) {
            TableOfContent(navController = navController, viewModel = viewModel,
                bookViewModel = bookViewModel)
        }
    }
}

/**
 * Composable for rendering the navigation rail.
 * This is the teacher's code.
 *
 * @param navController Navigation controller for managing navigation actions.
 * @param currentRoute The current route of navigation to highlight the selected item.
 */
@Composable
fun NavigationRailComponent(
    navController: NavHostController,
    currentRoute: String?
) {
    NavigationRail {
        NavBarItems.BarItems.forEach { navItem ->
            NavigationRailItem(
                selected = currentRoute == navItem.route,
                onClick = { navController.navigate(navItem.route) },
                icon = {
                    Icon(navItem.image, contentDescription = navItem.route)
                },
                label = {
                    Text(
                        text = stringResource(navItem.titleRes),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
            )
        }
    }
}

/**
 * Composable for rendering the permanent navigation drawer.
 * This is the teacher's code.
 *
 * @param navController Navigation controller for managing navigation actions.
 * @param viewModel The ViewModel managing app-wide state and data.
 * @param currentRoute The current route of navigation to highlight the selected item.
 */
@Composable
fun PermanentNavigationDrawerComponent(
    navController: NavHostController,
    viewModel: BookReadingAppViewModel,
    bookProcessingViewModel: BookProcessingViewModel,
    bookViewModel: BookViewModel,
    currentRoute: String?,
    adaptiveNavigationType: AdaptiveNavigationType,
    startDestination: String
) {
    PermanentNavigationDrawer(
        drawerContent = {
            PermanentDrawerSheet {
                Column {
                    Spacer(Modifier.height(dimensionResource(R.dimen.small_height)))
                    NavBarItems.BarItems.forEach { navItem ->
                        NavigationDrawerItem(
                            selected = currentRoute == navItem.route,
                            onClick = {
                                navController.navigate(navItem.route)
                            },
                            icon = {
                                Icon(navItem.image, contentDescription = navItem.route)
                            },
                            label = { Text(
                                text = stringResource(navItem.titleRes),
                                style = MaterialTheme.typography.bodyMedium
                            ) }
                        )
                    }
                }
            } },
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                //The call to NavigationHost is necessary to display the screen based on the route
                NavigationHost(
                    navController = navController,
                    viewModel = viewModel,
                    bookProcessingViewModel = bookProcessingViewModel,
                    bookViewModel = bookViewModel,
                    adaptiveNavigationType = adaptiveNavigationType,
                    startDestination = startDestination
                )
            }
        }
    )
}

/**
 * Composable for rendering the bottom navigation bar.
 * This is the teacher's code.
 *
 * @param navController Navigation controller for managing navigation actions.
 * @param viewModel The ViewModel managing app-wide state and data.
 * @param adaptiveNavigationType Determines the type of navigation (bottom bar, rail, or drawer).
 * @param currentRoute The current route of navigation to highlight the selected item.
 */
@Composable
fun BottomBar(
    navController: NavHostController,
    viewModel: BookReadingAppViewModel,
    adaptiveNavigationType: AdaptiveNavigationType,
    currentRoute: String?

) {
    if (adaptiveNavigationType == AdaptiveNavigationType.BOTTOM_NAVIGATION) {
        if (currentRoute != NavRoutes.TableOfContent.route && currentRoute != NavRoutes.Reading.route) {
            BottomNavigationBar(navController = navController, currentRoute = currentRoute)
        }
        if ((currentRoute == NavRoutes.Reading.route || currentRoute == NavRoutes.TableOfContent.route)
            && !viewModel.isReadingMode) {
            BottomNavigationBar(navController = navController, currentRoute = currentRoute)
        }
    }
}

/**
 * Composable for rendering the bottom navigation bar.
 * This is the teacher's code
 *
 * @param navController Navigation controller for managing navigation actions.
 * @param currentRoute The current route of navigation to highlight the selected item.
 */
@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    currentRoute: String?,
) {
    NavigationBar {
        NavBarItems.BarItems.forEach { navItem ->
            NavigationBarItem(
                selected = currentRoute == navItem.route,
                onClick = {
                    navController.navigate(navItem.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = navItem.image,
                        contentDescription = stringResource(navItem.titleRes)
                    )
                },
                label = {
                    Text(
                        text = stringResource(navItem.titleRes),
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
            )
        }
    }
}