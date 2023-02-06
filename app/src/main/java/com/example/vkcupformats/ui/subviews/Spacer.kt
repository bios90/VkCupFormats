package com.example.vkcupformats.ui.subviews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AppSpacer(
    height: Dp,
    background: Color = Color.Transparent,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    fillMaxWidth: Boolean = true,
) = Spacer(
    modifier = Modifier
        .fillMaxWidth()
        .padding(paddingValues)
        .background(background)
        .apply {
            if (fillMaxWidth) {
                then(fillMaxWidth())
            }
        }
        .height(height)
)

@Composable
fun AppSpacerHorizontal(
    width: Dp,
    background: Color = Color.Transparent,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    fillMaxHeight: Boolean = false,
) = Spacer(
    modifier = Modifier
        .padding(paddingValues)
        .background(background)
        .apply {
            if (fillMaxHeight) {
                then(fillMaxHeight())
            }
        }
        .width(width)
)

fun LazyListScope.AppSpacerItem(
    height: Dp,
    background: Color = Color.Transparent,
    paddingValues: PaddingValues = PaddingValues(0.dp),
) = item {
    AppSpacer(
        height = height,
        background = background,
        paddingValues = paddingValues
    )
}
