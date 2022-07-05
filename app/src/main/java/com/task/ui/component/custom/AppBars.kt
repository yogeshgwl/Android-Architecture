/**
 * Name: AppBars.kt
 * Created by: Nitin 15 Jun 2022
 * Copyright © 2022 GWL INC. All rights reserved.
 * Purpose: This file handles the application action bar and bottom bor.
 */

package com.task.ui.component.custom

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.task.ui.theme.Purple40

@Composable
fun TopBar(
    title: String,
    color: Color = Purple40,
    modifier: Modifier = Modifier,

    ) {
    SmallTopAppBar(
        modifier = modifier,
        title = {
            Text(text = title)
        },
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = color
        ),
    )
}

@Composable
fun BottomBar(
    selectedTab: BottomTab,
    tabs: Array<BottomTab>,
    itemClick: (BottomTab) -> Unit
) {
    NavigationBar {
        tabs.forEach { item ->
            NavigationBarItem(
                label = { Text(text = stringResource(id = item.title)) },
                selected = item == selectedTab,
                onClick = { itemClick(item) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = stringResource(id = item.title)
                    )
                }
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun AppBarPreview() {
    TopBar(title = "Testing")
}