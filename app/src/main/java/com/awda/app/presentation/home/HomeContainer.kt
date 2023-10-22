package com.awda.app.presentation.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.awda.app.presentation.challenge.ChallengeViewModel
import com.awda.app.presentation.home.components.BottomBar
import com.awda.app.presentation.home.navigation.HomeNavigation
import com.awda.app.presentation.navigation.Screen
import com.awda.app.presentation.settings.SettingsViewModel

/**
 * Created by Abdelrahman Rizq
 */

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeContainer(
    homeViewModel: HomeViewModel,
    settingsViewModel: SettingsViewModel,
    challengeViewModel: ChallengeViewModel,
    enableLockerService: (Boolean) -> Unit,
    navigateTo: (route: String) -> Unit
) {
    val navController = rememberNavController()

    val destinations = listOf(
        Destination(route = Screen.Challenge.route, icon = Icons.Default.List),
        Destination(route = Screen.Home.route, icon = Icons.Default.Home),
        Destination(route = Screen.Settings.route, icon = Icons.Default.Settings),
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            BottomBar(
                destinations = destinations,
                currentDestination = navController.currentBackStackEntryAsState().value?.destination,
                onNavigateToDestination = {
                    navController.navigate(it) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        },
        content = {
            Surface(
                modifier = Modifier.padding(all = 0.dp)
            ) {
                HomeNavigation(
                    modifier = Modifier
                        .fillMaxSize(),
                    navController = navController,
                    startDestination = Screen.Home.route,
                    homeViewModel = homeViewModel,
                    settingsViewModel = settingsViewModel,
                    challengeViewModel = challengeViewModel,
                    enableLockerService = enableLockerService,
                    navigateTo = navigateTo
                )
            }
        }
    )
}

data class Destination(
    val route: String,
    val icon: ImageVector
)