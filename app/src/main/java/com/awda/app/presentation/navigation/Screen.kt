package com.awda.app.presentation.navigation

/**
 * Created by Abdelrahman Rizq
 */

internal sealed class Screen(val route: String) {
    object HomeContainer : Screen("home_container_screen")
    object Home : Screen("home_screen")
    object Settings : Screen("settings_screen")
    object Challenge : Screen("challenge_screen")
    object AppUsage : Screen("usage_screen")
    object Timeline : Screen("timeline_screen")
    object Addiction : Screen("addiction_screen")
    object AddChallenge : Screen("add_challenge_screen")

    fun withArgs(vararg args: Any): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("$arg")
            }
        }
    }
}