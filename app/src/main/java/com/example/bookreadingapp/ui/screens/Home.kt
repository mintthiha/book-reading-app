package com.example.bookreadingapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.bookreadingapp.R
import com.example.bookreadingapp.ui.navigation.NavRoutes
import com.example.bookreadingapp.ui.theme.quote

/**
 * Composable that displays the Home screen of the app.
 * Provides a welcome message, navigation to the Library screen, and a motivational quote.
 *
 * @param navController Controller for navigating between different screens.
 * @param modifier Modifier to apply to the root container of the composable.
 */
@Composable
fun Home(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.medium_padding)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Title of the page
        Text (
            text = stringResource(R.string.home_welcome_message),
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.large_height)))

        // Button to go to library
        Button(
            onClick = { navController.navigate(NavRoutes.Library.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.go_to_library),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.large_height)))

        // Quote
        Column(
            modifier = Modifier
                .fillMaxWidth(0.5f)
        ) {
            Text(
                text = stringResource(R.string.home_welcome_quote),
                style = MaterialTheme.typography.quote,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = stringResource(R.string.john_locke),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}
