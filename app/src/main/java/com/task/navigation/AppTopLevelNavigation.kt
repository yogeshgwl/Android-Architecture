/* Name: AppNavHost.kt
* Created by: Nitin 20 Jun 2022
* Copyright © 2022 GWL INC. All rights reserved.
*/
package com.task.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Upcoming
import androidx.compose.material.icons.outlined.Upcoming
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.integerResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.task.ui.component.login.navigation.LoginDestination
import com.task.R

/**
 * Routes for the different top level destinations in the application. Each of these destinations
 * can contain one or more screens (based on the window size). Navigation from one screen to the
 * next within a single destination will be handled directly in composables.
 */

/**
 * Models the navigation top level actions in the app.
 */
class AppTopLevelNavigation(private val navController: NavHostController) {

    fun navigateTo(destination: TopLevelDestination) {
        navController.navigate(destination.route) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // re-selecting the same item
            launchSingleTop = true
            // Restore state when re-selecting a previously selected item
            restoreState = true
        }
    }
}

data class TopLevelDestination(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int
)

val TOP_LEVEL_DESTINATIONS = listOf(
    TopLevelDestination(
        route = LoginDestination.route,
        selectedIcon = Icons.Filled.Upcoming,
        unselectedIcon = Icons.Outlined.Upcoming,
        iconTextId = R.string.title_activity_login,
    ),
)
