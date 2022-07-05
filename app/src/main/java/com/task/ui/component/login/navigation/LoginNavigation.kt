/**
 * Name: LoginDestination.kt
 * Created by: Nitin 17 Jun 2022
 * Copyright © 2022 GWL INC. All rights reserved.
 * Purpose: Interface for describing the Now in Android navigation destinations.
 */

package com.task.ui.component.login.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.task.navigation.NavigationDestination
import com.task.ui.component.login.Login

object LoginDestination : NavigationDestination {
    override val route = "login_route"
    override val destination = "login_destination"
}

fun NavGraphBuilder.loginGraph(
    navigateToDashboard: () -> Unit,
    windowSizeClass: WindowSizeClass
) {
    composable(route = LoginDestination.route) {
        Login(
            navigateToDashboard = navigateToDashboard,
            windowSizeClass = windowSizeClass
        )
    }
}
