/**
 * Name: SearchScreen.kt
 * Created by: Nitin 21 Jun 2022
 * Copyright © 2022 GWL INC. All rights reserved.
 * Purpose: UI representation of the Search screen.
 */

package com.task.ui.component.dashboard.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.task.R

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier.fillMaxSize()

    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            Text(
                text = stringResource(id = R.string.bottom_tab_search),
            )
        }
}
