/**
 * Name: BottomTab.kt
 * Created by: Nitin 15 Jun 2022
 * Copyright © 2022 GWL INC. All rights reserved.
 * Purpose: This file defines the bottom tab items.
 */
package com.task.ui.component.custom

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.task.R

enum class BottomTab(
    @StringRes val title: Int,
    val icon: ImageVector
) {
    Home(R.string.bottom_tab_home, Icons.Outlined.Home),
    Search(R.string.bottom_tab_search, Icons.Outlined.Search);

    companion object {
        fun getTabFromResource(@StringRes resource: Int): BottomTab {
            return when (resource) {
                R.string.bottom_tab_search -> Search
                else -> Home
            }
        }
    }
}