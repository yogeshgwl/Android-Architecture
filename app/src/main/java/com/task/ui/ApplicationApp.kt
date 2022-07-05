/**
 * Name: ApplicationApp.kt
 * Created by: Nitin 21 Jun 2022
 * Copyright © 2022 GWL INC. All rights reserved.
 * Purpose: A class content few custom component and set initial app theme.
 */

package com.task.ui

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumedWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.rememberNavController
import com.task.navigation.AppNavHost
import com.task.navigation.TOP_LEVEL_DESTINATIONS
import com.task.navigation.TopLevelDestination
import com.task.ui.component.custom.AppBackground
import com.task.ui.theme.AppTheme
import com.task.ui.theme.size_0

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ApplicationApp(windowSizeClass: WindowSizeClass) {
    AppTheme {
        val navController = rememberNavController()
        AppBackground {
            Scaffold(
                modifier = Modifier,
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground,

                ) { padding ->
                Row(
                    Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(
                            WindowInsets.safeDrawing.only(
                                WindowInsetsSides.Horizontal
                            )
                        )
                ) {
                    AppNavHost(
                        windowSizeClass = windowSizeClass,
                        navController = navController,
                        modifier = Modifier
                            .padding(padding)
                            .consumedWindowInsets(padding)
                    )
                }
            }
        }
    }
}

/**
 * This compose function create the side bar with icon.
 */
@Composable
private fun AppNavRail(
    onNavigateToTopLevelDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
) {
    NavigationRail(modifier = modifier) {
        TOP_LEVEL_DESTINATIONS.forEach { destination ->
            val selected =
                currentDestination?.hierarchy?.any { it.route == destination.route } == true
            NavigationRailItem(
                selected = selected,
                onClick = { onNavigateToTopLevelDestination(destination) },
                icon = {
                    Icon(
                        if (selected) destination.selectedIcon else destination.unselectedIcon,
                        contentDescription = null
                    )
                },
                label = { Text(stringResource(destination.iconTextId)) }
            )
        }
    }
}

/**
 * This compose function to create bottom bar.
 */
@Composable
fun AppBottomBar(
    onNavigateToTopLevelDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?
) {
    // Wrap the navigation bar in a surface so the color behind the system
    // navigation is equal to the container color of the navigation bar.
    Surface(color = MaterialTheme.colorScheme.surface) {
        NavigationBar(
            modifier = Modifier.windowInsetsPadding(
                WindowInsets.safeDrawing.only(
                    WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
                )
            ),
            tonalElevation = size_0
        ) {

            TOP_LEVEL_DESTINATIONS.forEach { destination ->
                val selected =
                    currentDestination?.hierarchy?.any { it.route == destination.route } == true
                NavigationBarItem(
                    selected = selected,
                    onClick = { onNavigateToTopLevelDestination(destination) },
                    icon = {
                        Icon(
                            if (selected) {
                                destination.selectedIcon
                            } else {
                                destination.unselectedIcon
                            },
                            contentDescription = null
                        )
                    },
                    label = { Text(stringResource(destination.iconTextId)) }
                )
            }
        }
    }
}
