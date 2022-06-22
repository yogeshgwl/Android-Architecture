/**
 * Name: SearchDestination.kt
 * Created by: Nitin 21 Jun 2022
 * Copyright © 2022 GWL INC. All rights reserved.
 * Purpose: Interface for describing the Now in Android navigation destinations.
 */

package com.task.ui.component.dashboard.search.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.task.navigation.NavigationDestination
import com.task.ui.component.dashboard.search.SearchScreen

object SearchDestination : NavigationDestination {
    override val route = "search_route"
    override val destination = "search_destination"
}

fun NavGraphBuilder.searchGraph(
) {
    composable(route = SearchDestination.route) {
        SearchScreen(
        )
    }
}