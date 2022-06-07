package com.task.navigation

/**
 * This represent the identifier name for navigation.
 */
sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object Login : Screen("login_screen")
}