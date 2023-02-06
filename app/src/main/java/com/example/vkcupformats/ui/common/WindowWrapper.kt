package com.bios.yacupsurpise.ui.common.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.luminance
import com.example.vkcupformats.ui.common.ScreenWindowData
import com.example.vkcupformats.ui.theme.AppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

interface WindowWrapper {
    val screenWindowData: ScreenWindowData
        get() = ScreenWindowData(
            colorStatusBar = AppTheme.color.Background,
            colorNavBar = AppTheme.color.Background,
            isUnderNavBar = false,
            isUnderStatusBar = false,
        )

    @Composable
    fun ApplyScreenWindowData() {
        val systemUiController = rememberSystemUiController()
        val screenData = remember { screenWindowData }
        val darkStatusBarIcons = screenData.isLightStatusBarIcons?.not()
            ?: (screenData.colorStatusBar.luminance() > 0.5f)
        val darkNavBarIcons = screenData.isLightNavBarIcons?.not()
            ?: (screenData.colorNavBar.luminance() > 0.5f)
        SideEffect {
            systemUiController.setStatusBarColor(
                screenData.colorStatusBar,
                darkStatusBarIcons
            )
            systemUiController.setNavigationBarColor(
                screenData.colorNavBar,
                darkNavBarIcons
            )
        }
    }
}
