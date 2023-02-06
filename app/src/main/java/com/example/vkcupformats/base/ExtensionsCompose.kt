package com.example.vkcupformats.ui.common

import android.content.Context
import android.os.SystemClock
import android.view.ViewGroup
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import com.example.vkcupformats.ui.theme.AppTheme

@Composable
inline fun debounced(crossinline onClick: () -> Unit, debounceTime: Long = 1000L): () -> Unit {
    var lastTimeClicked by remember { mutableStateOf(0L) }
    val onClickLambda: () -> Unit = {
        val now = SystemClock.uptimeMillis()
        if (now - lastTimeClicked > debounceTime) {
            onClick()
        }
        lastTimeClicked = now
    }
    return onClickLambda
}

val String.color
    get() = Color(android.graphics.Color.parseColor(this))

fun getComposeRootView(context: Context) = ComposeView(context)
    .apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

fun Color.withAlpha(alpha: Float) = copy(alpha = alpha)
fun TextStyle.alignStart() = this.copy(textAlign = TextAlign.Start)
fun TextStyle.primaryTextColor() = this.copy(color = AppTheme.color.TextPrimary)
fun TextStyle.secondaryTextColor() = this.copy(color = AppTheme.color.TextSecondary)

@Composable
fun Modifier.appClickable(withRipple: Boolean = false, onClick: () -> Unit) = this.then(
    clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = rememberRipple(bounded = true).takeIf { withRipple },
        onClick = onClick
    )
)

@Composable
fun Modifier.appShadow(corner: Dp = AppTheme.dimens.x3) = this.then(
    shadow(
        elevation = AppTheme.dimens.x2,
        shape = RoundedCornerShape(corner),
    )
)

fun Modifier.modifyIf(condition: Boolean, modifier: Modifier.() -> Modifier): Modifier {
    return if (condition) {
        then(modifier(Modifier))
    } else {
        this
    }
}

@Composable
fun AnimatedVisibilityFade(
    visible: Boolean,
    modifier: Modifier = Modifier,
    durationEnter: Int = 300,
    durationExit: Int = 300,
    content: @Composable AnimatedVisibilityScope.() -> Unit,
) = AnimatedVisibility(
    modifier = modifier,
    visible = visible,
    content = content,
    enter = fadeIn(
        animationSpec = keyframes {
            this.durationMillis = durationEnter
        }
    ),
    exit = fadeOut(
        animationSpec = keyframes {
            this.durationMillis = durationExit
        }
    )
)

private val HorizontalScrollConsumer = object : NestedScrollConnection {
    override fun onPreScroll(available: Offset, source: NestedScrollSource) =
        available.copy(y = 0f)

    override suspend fun onPreFling(available: Velocity) = available.copy(y = 0f)
}

fun Modifier.disabledHorizontalPointerInputScroll(disabled: Boolean = true) =
    if (disabled) this.nestedScroll(HorizontalScrollConsumer) else this

fun LazyListState.isScrolledToEnd() =
    layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1
