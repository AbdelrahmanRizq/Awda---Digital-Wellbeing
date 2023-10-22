package com.awda.app.presentation.home.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.awda.app.presentation.challenge.ChallengeScreen
import com.awda.app.presentation.challenge.ChallengeViewModel
import com.awda.app.presentation.home.HomeScreen
import com.awda.app.presentation.home.HomeViewModel
import com.awda.app.presentation.navigation.Screen
import com.awda.app.presentation.settings.SettingsScreen
import com.awda.app.presentation.settings.SettingsViewModel

/**
 * Created by Abdelrahman Rizq
 */

@Composable
fun HomeNavigation(
    modifier: Modifier,
    navController: NavHostController,
    startDestination: String,
    homeViewModel: HomeViewModel,
    settingsViewModel: SettingsViewModel,
    challengeViewModel: ChallengeViewModel,
    enableLockerService: (Boolean) -> Unit,
    navigateTo: (route: String) -> Unit
) {
    NavHost(
        navController = navController, startDestination = startDestination, modifier = modifier
    ) {
        composable(
            route = Screen.Home.route,
        ) {
            HomeScreen(
                viewModel = homeViewModel,
                navigateTo = navigateTo
            )
        }
        composable(
            route = Screen.Settings.route,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                    animationSpec = tween(700)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                    animationSpec = tween(700)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                    animationSpec = tween(700)
                )
            }
        ) {
            SettingsScreen(
                viewModel = settingsViewModel,
                enableLockerService = enableLockerService
            )
        }
        composable(
            route = Screen.Challenge.route,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                    animationSpec = tween(700)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                    animationSpec = tween(700)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                    animationSpec = tween(700)
                )
            }
        ) {
            ChallengeScreen(
                viewModel = challengeViewModel,
                navigateTo = navigateTo
            )
        }
    }
}