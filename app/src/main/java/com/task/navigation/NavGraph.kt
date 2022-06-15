/**
 * Name: NavGraph.kt
 * Created by: Nitin 15 Jun 2022
 * Copyright © 2022 GWL INC. All rights reserved.
 * Purpose: This class to be responsible for handling screen navigation.
 */

package com.task.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.task.ui.component.dashboard.DashboardScreen
import com.task.ui.component.login.Login
import com.task.ui.component.splash.SplashScreen

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(route = Screen.Splash.route) {
            SplashScreen(navController = navController)
        }

        composable(route = Screen.Login.route) {
            Login(navController = navController, viewModel = hiltViewModel())
        }

        composable(route = Screen.Dashboard.route) {
            DashboardScreen(navController = navController, viewModel = hiltViewModel())
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SetupNavPreview() {
    SetupNavGraph(navController = rememberNavController())
}