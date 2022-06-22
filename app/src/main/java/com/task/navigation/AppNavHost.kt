/**
 * Name: AppNavHost.kt
 * Created by: Nitin 17 Jun 2022
 * Copyright © 2022 GWL INC. All rights reserved.
 * Purpose: The navigation graph defined in this file defines the different top level routes. Navigation
 * within each route is handled using state and Back Handlers.
 */

package com.task.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.task.ui.component.dashboard.home.navigation.HomeDestination
import com.task.ui.component.dashboard.home.navigation.homeGraph
import com.task.ui.component.dashboard.navigation.DashboardDestination
import com.task.ui.component.dashboard.navigation.dashboardGraph
import com.task.ui.component.dashboard.search.navigation.SearchDestination
import com.task.ui.component.dashboard.search.navigation.searchGraph
import com.task.ui.component.login.navigation.LoginDestination
import com.task.ui.component.login.navigation.loginGraph

/**
 * The navigation graph defined in this file defines the different top level routes. Navigation
 * within each route is handled using state and Back Handlers.
 */
@Composable
fun AppNavHost(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = LoginDestination.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        loginGraph(
            navigateToDashboard = { navController.navigate(DashboardDestination.route) },
            windowSizeClass = windowSizeClass
        )
        dashboardGraph(
            navigateToHome = { navController.navigate("${HomeDestination.route}") },
            navigateToSearch = { navController.navigate("${SearchDestination.route}") },
            nestedGraphs = {
                homeGraph()
                searchGraph()
            }
        )
    }
}
