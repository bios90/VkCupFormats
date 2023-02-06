package com.example.vkcupformats.ui.subviews

import android.view.MotionEvent
import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils

data class RatingTouchDataResult(
    val rating: Float,
    val starIndex: Int?
)

private const val MAX_CLICK_TIME = 200
private const val MAX_CLICK_DISTANCE = 5

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ElementRatingBar(
    modifier: Modifier = Modifier,
    starsCount: Int,
    colorEmpty: Color,
    colorFilledFrom: Color,
    colorFilledTo: Color,
    currentRating: Float,
    starSize: Dp,
    onRatingScrolled: (Float) -> Unit,
    onRatingClicked: (Float) -> Unit,
) {
    var fullSize by remember { mutableStateOf(IntSize.Zero) }
    val eachStarSize = with(LocalDensity.current) {
        starSize.toPx()
    }
    var selectedStarIndex: Int? by remember {
        mutableStateOf(null)
    }
    val starAnimationProgress = (0 until starsCount).map {
        remember {
            Animatable(1f)
        }
    }
    LaunchedEffect(key1 = selectedStarIndex, block = {
        for (i in 0 until starsCount) {
            val animatble = starAnimationProgress.get(i)
            if (i == selectedStarIndex) {
                animatble.snapTo(1f)
                animatble.animateTo(
                    1.25f,
                    animationSpec = repeatable(
                        iterations = Int.MAX_VALUE,
                        repeatMode = RepeatMode.Reverse,
                        animation = tween(
                            1000,
                            easing = LinearEasing
                        )
                    )
                )
            } else {
                animatble.snapTo(1f)
            }
        }
    })
    Row(
        modifier = modifier
            .onGloballyPositioned { fullSize = it.size }
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val touchResult = handleTouch(
                        touchX = offset.x,
                        fullSize = fullSize.width.toFloat(),
                        eachStarSize = eachStarSize,
                        starsCount = starsCount
                    )
                    onRatingClicked.invoke(touchResult.rating)
                }
            }
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = { selectedStarIndex = null },
                    onHorizontalDrag = { change, dragAmount ->
                        val x = change.position.x
                        val touchResult = handleTouch(
                            touchX = x,
                            fullSize = fullSize.width.toFloat(),
                            eachStarSize = eachStarSize,
                            starsCount = starsCount
                        )
                        onRatingScrolled.invoke(touchResult.rating)
                        selectedStarIndex = touchResult.starIndex
                    }
                )
            },
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (i in 0 until starsCount) {
            val scale =
                if (selectedStarIndex == i) starAnimationProgress.get(i).value else 1f

            val colorRange = getColorRangeForPosition(
                colorFrom = colorFilledFrom.toArgb(),
                colorTo = colorFilledTo.toArgb(),
                index = i,
                overallCount = starsCount
            )
            StarImageCompose(
                modifier = Modifier.scale(scale),
                size = starSize,
                colorEmpty = colorEmpty,
                colorFilledStart = Color(colorRange.first),
                colorFilledEnd = Color(colorRange.second),
                progress = currentRating - i
            )
        }
    }
}

private fun getColorRangeForPosition(
    colorFrom: Int,
    colorTo: Int,
    index: Int,
    overallCount: Int
): Pair<Int, Int> {
    val eachProgress = 1f / overallCount

    val startIndex = eachProgress * index
    val endIndex = eachProgress * (index + 1)

    val colorStart = ColorUtils.blendARGB(colorFrom, colorTo, startIndex)
    val colorEnd = ColorUtils.blendARGB(colorFrom, colorTo, endIndex)

    return Pair(colorStart, colorEnd)
}

private fun handleTouch(
    touchX: Float,
    fullSize: Float,
    eachStarSize: Float,
    starsCount: Int
): RatingTouchDataResult {
    val spacingSize = (fullSize - (eachStarSize * starsCount)) / (starsCount + 1)
    val eachStarRanges = (0..starsCount - 1).map { index ->
        val start = (spacingSize * (index + 1)) + (index * eachStarSize)
        val end = start + eachStarSize
        Pair(start, end)
    }
    if (touchX < eachStarRanges.first().first) {
        return RatingTouchDataResult(0f, null)
    } else if (touchX > eachStarRanges.last().second) {
        return RatingTouchDataResult(starsCount.toFloat(), null)
    } else {
        for ((index, range) in eachStarRanges.withIndex()) {
            val currentRange = range
            val nextRange = eachStarRanges.getOrNull(index + 1)
            if (touchX > currentRange.first && touchX < currentRange.second) {
                val touchInStar = touchX - currentRange.first
                val progessInStar = touchInStar / (currentRange.second - currentRange.first)
                val rating = index + progessInStar
                return RatingTouchDataResult(rating, index)
            } else if (nextRange != null && touchX > currentRange.second && touchX < nextRange.first) {
                return RatingTouchDataResult((index + 1).toFloat(), null)
            }
        }
    }
    return RatingTouchDataResult(-1f, null)
}

private fun isClick(startX: Float, startY: Float, upEvent: MotionEvent): Boolean {
    val duration = upEvent.eventTime - upEvent.downTime
    if (duration > MAX_CLICK_TIME) {
        return false
    }
    val distanceX = Math.abs(startX - upEvent.x)
    val distanceY = Math.abs(startY - upEvent.y)
    return distanceX < MAX_CLICK_DISTANCE && distanceY < MAX_CLICK_DISTANCE
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    ElementRatingBar(
        starsCount = 5,
        colorEmpty = Color.LightGray,
        colorFilledFrom = Color.Green,
        colorFilledTo = Color.Blue,
        currentRating = 3.6f,
        starSize = 32.dp,
        onRatingScrolled = {},
        onRatingClicked = {}
    )
}