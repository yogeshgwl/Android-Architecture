/**
 * Name: DashboardScreen.kt
 * Created by: Nitin 15 Jun 2022
 * Copyright © 2022 GWL INC. All rights reserved.
 * Purpose: UI representation of the Dashboard screen.
 */

package com.task.ui.component.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.task.R
import com.task.ui.component.custom.AppTab
import com.task.ui.component.custom.AppTabRow
import com.task.ui.component.custom.TopBar
import com.task.ui.component.dashboard.home.HomeScreen
import com.task.ui.component.dashboard.search.SearchScreen
import com.task.ui.theme.Purple40

@Composable
fun DashboardScreen(
    navigateToHome: () -> Unit,  //TODO: This would be handled in Home screen.
    navigateToSearch: () -> Unit, //TODO: This would be handled in Search screen.
    viewModel: DashBoardViewModel = hiltViewModel(),
) {
    val tabState by viewModel.tabState.collectAsState()

    DashboardContent(tabState = tabState, switchTab = viewModel::switchTab)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardContent(
    tabState: DashboardTabState,
    switchTab: (Int) -> Unit,
) {
    Scaffold(
        modifier = Modifier,
        topBar = {
            TopBar(
                title = "Dashboard",
                color = Purple40,
            )
        },

        ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        Column(modifier) {
            AppTabRow(selectedTabIndex = tabState.currentIndex) {
                tabState.titles.forEachIndexed { index, titleId ->
                    AppTab(
                        selected = index == tabState.currentIndex,
                        onClick = { switchTab(index) },
                        text = { Text(text = stringResource(id = titleId)) }
                    )
                }
            }
            when (tabState.currentIndex) {
                0 -> {
                    HomeScreen(
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                1 -> {
                    SearchScreen(
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun DashboardScreenPreview() {
    DashboardContent(tabState = DashboardTabState(
        titles = listOf(R.string.bottom_tab_home, R.string.bottom_tab_search),
        currentIndex = 0
    ), switchTab = {})
}
