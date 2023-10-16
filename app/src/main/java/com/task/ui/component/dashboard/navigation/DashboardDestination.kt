/**
 * Name: DashboardDestination.kt
 * Created by: Nitin 20 Jun 2022
 * Copyright © 2022 GWL INC. All rights reserved.
 * Purpose: Interface for describing the Now in Android navigation destinations.
 */

package com.task.ui.component.dashboard.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.task.navigation.NavigationDestination
import com.task.ui.component.dashboard.DashboardScreen

object DashboardDestination : NavigationDestination {
    override val route = "dashboard_route"
    override val destination = "dashboard_destination"
}

fun NavGraphBuilder.dashboardGraph(
    navigateToHome: () -> Unit,
    navigateToSearch: () -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit
) {
    navigation(
        route = DashboardDestination.route,
        startDestination = DashboardDestination.destination
    ) {
        nestedGraphs()
        composable(route = DashboardDestination.destination) {
            DashboardScreen(
                navigateToHome = navigateToHome,
                navigateToSearch = navigateToSearch
            )
        }
    }
}
