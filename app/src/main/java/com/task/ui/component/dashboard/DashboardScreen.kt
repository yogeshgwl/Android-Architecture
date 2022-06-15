/**
 * Name: DashboardScreen.kt
 * Created by: Nitin 15 Jun 2022
 * Copyright © 2022 GWL INC. All rights reserved.
 * Purpose: UI representation of the Dashboard screen.
 */

package com.task.ui.component.dashboard

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.task.ui.component.custom.BottomBar
import com.task.ui.component.custom.BottomTab
import com.task.ui.component.custom.TopBar
import com.task.ui.home.HomeScreen
import com.task.ui.theme.Purple500

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavHostController,
    viewModel: DashBoardViewModel,
) {

    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }
    val selectedTab = BottomTab.getTabFromResource(viewModel.selectedTab.value)
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopBar(
                title = "Dashboard",
                color = Purple500,
            )
        },
        bottomBar = {
            BottomBar(
                selectedTab = selectedTab,
                tabs = BottomTab.values(),
                itemClick = { tab ->
                    viewModel.selectedTab(tab.title)
                }
            )
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        Crossfade(targetState = selectedTab) { destination ->
            when (destination) {
                BottomTab.Home -> HomeScreen()
            }
        }
        Column(
            modifier = modifier
                .fillMaxSize()
        ) {
            Text(
                text = "This testing text  "
            )
        }
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun DashboardScreenPreview() {
    DashboardScreen(navController = rememberNavController(), viewModel = hiltViewModel())
}
