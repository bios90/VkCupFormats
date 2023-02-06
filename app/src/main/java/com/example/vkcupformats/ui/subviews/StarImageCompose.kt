package com.example.vkcupformats.ui.subviews

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.Brush.Companion.horizontalGradient
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import com.example.vkcupformats.R

@Composable
fun StarImageCompose(
    modifier: Modifier = Modifier,
    size: Dp,
    colorEmpty: Color,
    colorFilledStart: Color,
    colorFilledEnd: Color,
    progress: Float,
) {
    var value by remember {
        mutableStateOf(1f)
    }
    value = if (progress < 0f) {
        0f
    } else if (progress > 1f) {
        1f
    } else {
        progress
    }

    Box(
        modifier = modifier.size(size)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_star_faw_empty),
            contentDescription = "ic_star_empty",
            colorFilter = ColorFilter.tint(colorEmpty)
        )
        val outline = StarClipShape(
            sizeDp = size,
            progress = value
        )
        Image(
            modifier = Modifier
                .clip(outline)
                .graphicsLayer(alpha = 0.99f)
                .drawWithCache {
                    onDrawWithContent {
                        val brush = horizontalGradient(listOf(colorFilledStart, colorFilledEnd))
                        drawContent()
                        drawRect(brush, blendMode = BlendMode.SrcAtop)
                    }
                },
            painter = painterResource(id = R.drawable.ic_star_faw_filled),
            contentDescription = "ic_star_filled",
        )
    }
}

private class StarClipShape(
    private val sizeDp: Dp,
    private val progress: Float,
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val sizePx = with(density) {
            sizeDp.toPx()
        }
        return Outline.Rectangle(
            Rect(
                Offset.Zero,
                Size(
                    width = sizePx * progress,
                    height = sizePx
                )
            )
        )
    }
}
