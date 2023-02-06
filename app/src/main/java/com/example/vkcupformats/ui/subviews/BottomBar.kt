package com.example.vkcupformats.ui.subviews

import android.content.Context
import android.util.AttributeSet
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vkcupformats.data.TypeTab
import com.example.vkcupformats.data.getIconResId
import com.example.vkcupformats.data.getTabTitleResId
import com.example.vkcupformats.ui.common.appClickable
import com.example.vkcupformats.ui.common.appShadow
import com.example.vkcupformats.ui.common.withAlpha
import com.example.vkcupformats.ui.theme.AppTheme
import com.example.vkcupformats.ui.utils.AppRippleTheme

class BottomBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AbstractComposeView(context, attrs, defStyle) {

    var selectedTab: TypeTab by mutableStateOf(TypeTab.values().get(0))
    var onTabSelected: (TypeTab) -> Unit = {}

    @Composable
    override fun Content() {
        BottomBar(
            selectedTab = selectedTab,
            onTabClicked = onTabSelected
        )
    }
}


@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    selectedTab: TypeTab,
    onTabClicked: (TypeTab) -> Unit
) {
    Row(
        modifier = modifier
            .appShadow(corner = 0.dp)
            .fillMaxWidth()
            .height(AppTheme.dimens.x14)
            .background(AppTheme.color.Surface)
    ) {
        for (tab in TypeTab.values()) {
            BottomBarButton(
                modifier = Modifier.weight(1f),
                tab = tab,
                isSelected = selectedTab == tab,
                onClick = { onTabClicked.invoke(tab) }
            )
        }
    }
}

@Composable
fun BottomBarButton(
    modifier: Modifier = Modifier,
    tab: TypeTab,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val color = if (isSelected) {
        AppTheme.color.Accent
    } else {
        AppTheme.color.TextSecondary
    }
    CompositionLocalProvider(
        LocalRippleTheme provides AppRippleTheme(AppTheme.color.Accent.withAlpha(0.15f)),
    ) {
        Column(
            modifier = modifier
                .fillMaxHeight()
                .appClickable(withRipple = true, onClick = onClick),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier.size(AppTheme.dimens.x6),
                painter = painterResource(id = tab.getIconResId()),
                contentDescription = "${tab.name}_icon",
                colorFilter = ColorFilter.tint(
                    color = color
                )
            )
            AppSpacer(height = AppTheme.dimens.x05)
            Text(
                text = stringResource(id = tab.getTabTitleResId()),
                style = AppTheme.typography.RegS,
                color = color
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    BottomBar(
        selectedTab = TypeTab.BUBBLES,
        onTabClicked = {}
    )
}