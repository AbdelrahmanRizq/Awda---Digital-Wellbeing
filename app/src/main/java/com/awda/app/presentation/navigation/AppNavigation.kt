package com.awda.app.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.awda.app.presentation.addiction.AddictionLevelScreen
import com.awda.app.presentation.challenge.AddChallengeScreen
import com.awda.app.presentation.challenge.ChallengeViewModel
import com.awda.app.presentation.home.HomeContainer
import com.awda.app.presentation.home.HomeViewModel
import com.awda.app.presentation.settings.SettingsViewModel
import com.awda.app.presentation.timeline.TimelineScreen
import com.awda.app.presentation.usage.AppUsageScreens
import com.awda.app.presentation.usage.AppUsageViewModel

/**
 * Created by Abdelrahman Rizq
 */

@Composable
fun AppNavigation(
    homeViewModel: HomeViewModel,
    appUsageViewModel: AppUsageViewModel,
    settingsViewModel: SettingsViewModel,
    challengeViewModel: ChallengeViewModel,
    enableLockerService: (Boolean) -> Unit,
    enableChallengeService: (Boolean) -> Unit
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.HomeContainer.route) {
        composable(
            route = Screen.HomeContainer.route,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Up,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Up,
                    animationSpec = tween(700)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Down,
                    animationSpec = tween(700)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Down,
                    animationSpec = tween(700)
                )
            }
        ) {
            HomeContainer(
                homeViewModel = homeViewModel,
                settingsViewModel = settingsViewModel,
                challengeViewModel = challengeViewModel,
                enableLockerService = enableLockerService,
                navigateTo = {
                    navController.navigate(it)
                }
            )
        }

        composable(
            route = Screen.AppUsage.route,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Up,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Up,
                    animationSpec = tween(700)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Down,
                    animationSpec = tween(700)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Down,
                    animationSpec = tween(700)
                )
            }
        ) {
            AppUsageScreens(
                viewModel = appUsageViewModel,
                appUsage = homeViewModel.appUsageState.value
            )
        }

        composable(
            route = Screen.Timeline.route,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Up,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Up,
                    animationSpec = tween(700)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Down,
                    animationSpec = tween(700)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Down,
                    animationSpec = tween(700)
                )
            }
        ) {
            TimelineScreen(
                timeline = homeViewModel.timelineState.value
            )
        }

        composable(
            route = Screen.Addiction.route,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Up,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Up,
                    animationSpec = tween(700)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Down,
                    animationSpec = tween(700)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Down,
                    animationSpec = tween(700)
                )
            }
        ) {
            AddictionLevelScreen(homeViewModel.addictionLevelState.value)
        }

        composable(
            route = Screen.AddChallenge.route,
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
            AddChallengeScreen(
                challengeViewModel,
                navController,
                challengeViewModel.appsState.value,
                enableChallengeService
            )
        }
    }
}