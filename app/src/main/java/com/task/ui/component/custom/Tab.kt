/**
 * Name: Tab.kt
 * Created by: Nitin 21 Jun 2022
 * Copyright © 2022 GWL INC. All rights reserved.
 * Purpose: A class responsible to handle the custom tabs.
 */

package com.task.ui.component.custom

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.task.ui.theme.size_2
import com.task.ui.theme.size_6

/**
 * App tab. Wraps Material 3 [Tab] and shifts text label down.
 *
 * @param selected Whether this tab is selected or not.
 * @param onClick The callback to be invoked when this tab is selected.
 * @param modifier Modifier to be applied to the tab.
 * @param enabled Controls the enabled state of the tab. When `false`, this tab will not be
 * clickable and will appear disabled to accessibility services.
 * @param text The text label content.
 */
@Composable
fun AppTab(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: @Composable () -> Unit
) {
    Tab(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        text = {
            val style = MaterialTheme.typography.labelLarge.copy(textAlign = TextAlign.Center)
            ProvideTextStyle(
                value = style,
                content = {
                    Box(modifier = Modifier.padding(top = AppTabDefaults.TabTopPadding)) {
                        text()
                    }
                }
            )
        }
    )
}

/**
 * App tab row. Wraps Material 3 [TabRow].
 *
 * @param selectedTabIndex The index of the currently selected tab.
 * @param modifier Modifier to be applied to the tab row.
 * @param tabs The tabs inside this tab row. Typically this will be multiple [AppTab]s. Each element
 * inside this lambda will be measured and placed evenly across the row, each taking up equal space.
 */
@Composable
fun AppTabRow(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    tabs: @Composable () -> Unit
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = modifier,
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onSurface,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                height = size_2,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        tabs = tabs
    )
}

object AppTabDefaults {
    val TabTopPadding = size_6
}
