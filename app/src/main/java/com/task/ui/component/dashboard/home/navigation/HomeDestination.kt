/**
 * Name: HomeDestination.kt
 * Created by: Nitin 20 Jun 2022
 * Copyright © 2022 GWL INC. All rights reserved.
 * Purpose: Interface for describing the Now in Android navigation destinations.
 */

package com.task.ui.component.dashboard.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.task.navigation.NavigationDestination
import com.task.ui.component.dashboard.home.HomeScreen

object HomeDestination : NavigationDestination {
    override val route = "home_route"
    override val destination = "home_destination"
}

fun NavGraphBuilder.homeGraph(

) {
    composable(route = HomeDestination.route) {
        HomeScreen()
    }
}
