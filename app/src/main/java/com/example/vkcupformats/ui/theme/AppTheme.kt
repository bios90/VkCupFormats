package com.example.vkcupformats.ui.theme

import androidx.compose.runtime.Composable

object AppTheme {
    val color = Colors()
    val dimens = Dimens()
    val typography = Typography()
}

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    content.invoke()
}
