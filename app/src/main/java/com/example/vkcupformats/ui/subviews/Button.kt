package com.example.vkcupformats.ui.subviews

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.vkcupformats.ui.common.debounced
import com.example.vkcupformats.ui.theme.AppTheme
import com.example.vkcupformats.ui.utils.AppRippleTheme

@Composable
fun ButtonEmpty(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle = AppTheme.typography.SemiBoldL,
    onClick: () -> Unit,
    isEnabled: Boolean = true,
    corners: CornerBasedShape = RoundedCornerShape(50),
    elevation: ButtonElevation? = null,
    minWidth: Dp = ButtonDefaults.MinWidth,
    minHeight: Dp = ButtonDefaults.MinHeight,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = AppTheme.dimens.x3,
        vertical = AppTheme.dimens.x2
    ),
    imageStart: @Composable RowScope.() -> Unit = {},
    textColor: Color = AppTheme.color.TextPrimary,
    borderColor: Color = AppTheme.color.TextPrimary,
    showProgress: Boolean = false,
) {
    BaseButton(
        text = text,
        textStyle = textStyle,
        modifier = modifier
            .clip(corners)
            .border(
                width = AppTheme.dimens.x05,
                color = borderColor,
                shape = corners
            ),
        onClick = onClick,
        colorBg = AppTheme.color.Transparent,
        colorRipple = AppTheme.color.TextPrimary,
        isEnabled = isEnabled,
        elevation = elevation,
        minWidth = minWidth,
        minHeight = minHeight,
        contentPadding = contentPadding,
        imageStart = imageStart,
        textColor = textColor,
        showProgress = showProgress,
        disabledAlpha = 1f,
    )
}

@Composable
fun ButtonAccent(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle = AppTheme.typography.SemiBoldL,
    onClick: () -> Unit,
    isEnabled: Boolean = true,
    corners: CornerBasedShape = RoundedCornerShape(50),
    elevation: ButtonElevation? = null,
    minWidth: Dp = ButtonDefaults.MinWidth,
    minHeight: Dp = ButtonDefaults.MinHeight,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = AppTheme.dimens.x3,
        vertical = AppTheme.dimens.x2
    ),
    imageStart: @Composable RowScope.() -> Unit = {},
    textColor: Color = AppTheme.color.TextPrimary,
    showProgress: Boolean = false,
) {
    BaseButton(
        text = text,
        textStyle = textStyle,
        modifier = modifier
            .clip(corners),
        onClick = onClick,
        colorBg = AppTheme.color.Accent,
        colorRipple = AppTheme.color.Accent,
        isEnabled = isEnabled,
        elevation = elevation,
        minWidth = minWidth,
        minHeight = minHeight,
        contentPadding = contentPadding,
        imageStart = imageStart,
        textColor = textColor,
        showProgress = showProgress
    )
}

@Composable
fun ButtonPrimary(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle = AppTheme.typography.SemiBoldL,
    onClick: () -> Unit,
    isEnabled: Boolean = true,
    corners: CornerBasedShape = RoundedCornerShape(50),
    elevation: ButtonElevation? = null,
    minWidth: Dp = ButtonDefaults.MinWidth,
    minHeight: Dp = ButtonDefaults.MinHeight,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = AppTheme.dimens.x3,
        vertical = AppTheme.dimens.x2
    ),
    imageStart: @Composable RowScope.() -> Unit = {},
    textColor: Color = AppTheme.color.TextPrimary,
    showProgress: Boolean = false,
) {
    BaseButton(
        text = text,
        textStyle = textStyle,
        modifier = modifier
            .clip(corners),
        onClick = onClick,
        colorBg = AppTheme.color.Surface,
        colorRipple = AppTheme.color.Accent,
        isEnabled = isEnabled,
        elevation = elevation,
        minWidth = minWidth,
        minHeight = minHeight,
        contentPadding = contentPadding,
        imageStart = imageStart,
        textColor = textColor,
        showProgress = showProgress
    )
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BaseButton(
    modifier: Modifier = Modifier,
    text: Any,
    textStyle: TextStyle,
    onClick: () -> Unit,
    colorBg: Color,
    colorBgDisabled: Color = colorBg,
    colorRipple: Color,
    elevation: ButtonElevation?,
    isEnabled: Boolean = true,
    contentPadding: PaddingValues,
    minWidth: Dp,
    minHeight: Dp,
    imageStart: @Composable RowScope.() -> Unit,
    textColor: Color,
    content: (@Composable RowScope.() -> Unit)? = null,
    disabledAlpha: Float = 0.4f,
    showProgress: Boolean,
) {
    CompositionLocalProvider(
        LocalRippleTheme provides AppRippleTheme(colorRipple),
        LocalMinimumTouchTargetEnforcement provides false
    ) {
        val clickable = debounced(debounceTime = 500, onClick = onClick)
        Button(
            onClick = { clickable.invoke() },
            modifier = modifier
                .defaultMinSize(
                    minWidth = minWidth,
                    minHeight = minHeight
                )
                .alpha(if (isEnabled) 1f else disabledAlpha),
            enabled = isEnabled,
            elevation = elevation,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorBg,
                disabledBackgroundColor = colorBgDisabled
            ),
            minWidth = minWidth,
            minHeight = minHeight,
            contentPadding = contentPadding,
        ) {
            if (content != null) {
                content.invoke(this)
            } else if (showProgress) {
                CircularProgressIndicator(
                    color = textColor,
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.CenterVertically)
                )
            } else {
                imageStart()
                val textAsAnnotatedString = text as? AnnotatedString
                val textAsString = text as? String
                if (textAsAnnotatedString != null) {
                    Text(
                        text = textAsAnnotatedString,
                        style = textStyle,
                        color = textColor
                    )
                } else {
                    Text(
                        text = textAsString ?: "",
                        style = textStyle,
                        color = textColor
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = ButtonDefaults.elevation(),
    shape: Shape = MaterialTheme.shapes.small,
    border: BorderStroke? = null,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    minWidth: Dp,
    minHeight: Dp,
    content: @Composable RowScope.() -> Unit,
) {
    val contentColor by colors.contentColor(enabled)
    Surface(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        color = colors.backgroundColor(enabled).value,
        contentColor = contentColor.copy(alpha = 1f),
        border = border,
        elevation = elevation?.elevation(enabled, interactionSource)?.value ?: 0.dp,
        interactionSource = interactionSource,
    ) {
        CompositionLocalProvider(LocalContentAlpha provides contentColor.alpha) {
            ProvideTextStyle(
                value = AppTheme.typography.RegL
            ) {
                Row(
                    Modifier
                        .defaultMinSize(
                            minWidth = minWidth,
                            minHeight = minHeight
                        )
                        .padding(contentPadding),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    content = content
                )
            }
        }
    }
}