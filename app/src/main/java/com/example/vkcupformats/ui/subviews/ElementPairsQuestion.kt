package com.example.vkcupformats.ui.subviews

import android.graphics.PathMeasure
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.vkcupformats.base.let2
import com.example.vkcupformats.data.elements.ModelPairConnection
import com.example.vkcupformats.data.elements.ModelPairElement
import com.example.vkcupformats.data.elements.ModelPairsQuestion
import com.example.vkcupformats.data.utils.MockHelper
import com.example.vkcupformats.ui.theme.AppTheme
import kotlinx.coroutines.launch

data class PathDrawInfo(
    val path: Path,
    val color: Color
) {
    companion object {
        val Default = PathDrawInfo(
            path = Path(),
            color = Color.Transparent
        )
    }
}

data class PointInfo(
    val x: Float,
    val y: Float
)

@Composable
fun ElementPairsQuestion(
    modelPairsQuestion: ModelPairsQuestion,
    leftSelected: ModelPairElement?,
    rightSelected: ModelPairElement?,
    onLeftClicked: (ModelPairElement) -> Unit,
    onRightClicked: (ModelPairElement) -> Unit,
    connectedPairs: List<ModelPairConnection>,
    newPairConnection: ModelPairConnection?,
    onPairAnimationDrawn: (ModelPairConnection) -> Unit
) {
    val density = LocalDensity.current
    var leftCoordinates: Map<ModelPairElement, PointInfo> by remember {
        mutableStateOf(mapOf())
    }
    var rightCoordinates: Map<ModelPairElement, PointInfo> by remember {
        mutableStateOf(mapOf())
    }

    val getElementCoordinates: (ModelPairElement) -> PointInfo? = { element ->
        leftCoordinates.get(element) ?: rightCoordinates.get(element)
    }

    val animProgress = remember {
        Animatable(0f)
    }
    var pairToAnimate: ModelPairConnection? by remember {
        mutableStateOf(null)
    }
    pairToAnimate = newPairConnection
    var pathForAnimation: PathDrawInfo by remember {
        mutableStateOf(PathDrawInfo.Default)
    }

    var pathHistory: List<PathDrawInfo> by remember {
        mutableStateOf(emptyList())
    }
    pathHistory = connectedPairs.mapNotNull { pair ->
        val start = getElementCoordinates(pair.start)
        val end = getElementCoordinates(pair.end)
        let2(start, end) { start, end ->
            val path = getConnectionPath(start, end, density)
            val color = pair.isCorrect.isRightToColor()
            PathDrawInfo(path, color)
        }
    }
    var pathMeasure: PathMeasure by remember {
        mutableStateOf(PathMeasure())
    }
    val poses = floatArrayOf(0f, 0f)

    LaunchedEffect(
        key1 = newPairConnection,
        block = {
            pairToAnimate?.let { pair ->
                val start = getElementCoordinates(pair.start)
                val end = getElementCoordinates(pair.end)
                if (start != null && end != null) {
                    val pathFullConnection = getConnectionPath(start, end, density)
                    pathMeasure = PathMeasure(pathFullConnection.asAndroidPath(), false)
                    pathForAnimation = PathDrawInfo(
                        path = Path().apply { moveTo(start.x, start.y) },
                        color = pair.isCorrect.isRightToColor()
                    )
                    animProgress.snapTo(0f)
                    launch {
                        animProgress.animateTo(targetValue = 1f, animationSpec = tween(1000))
                        onPairAnimationDrawn.invoke(pair)
                        pathForAnimation = PathDrawInfo.Default
                    }
                }
            }
        }
    )

    if (pairToAnimate != null) {
        pathMeasure.getPosTan(animProgress.value * pathMeasure.length, poses, null)
        pathForAnimation.path.lineTo(poses[0], poses[1])
    }

    Box {
        val strokeWidth = with(density) {
            AppTheme.dimens.x05.toPx()
        }
        Canvas(modifier = Modifier.fillMaxWidth()) {
            with(pathHistory) {
                for (pathInfo in pathHistory) {
                    drawPath(
                        path = pathInfo.path,
                        color = pathInfo.color,
                        style = Stroke(width = strokeWidth)
                    )
                }
            }
            with(animProgress) {
                drawPath(
                    path = pathForAnimation.path,
                    color = pathForAnimation.color,
                    style = Stroke(width = strokeWidth)
                )
            }
        }
        var rightColumnSize by remember {
            mutableStateOf(IntSize.Zero)
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                placeButtons(
                    elements = modelPairsQuestion.left,
                    colorProvider = {
                        getBtnColor(
                            element = it,
                            connectedPairs = connectedPairs,
                            selected = leftSelected
                        )
                    },
                    onGloballyPositioned = { element, coordinates ->
                        val point = PointInfo(
                            x = coordinates.positionInParent().x + coordinates.size.width,
                            y = coordinates.positionInParent().y + coordinates.size.height / 2
                        )
                        leftCoordinates = leftCoordinates.plus(element to point)
                    },
                    onClick = { onLeftClicked.invoke(it) }
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .onSizeChanged { rightColumnSize = it },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                placeButtons(
                    elements = modelPairsQuestion.right,
                    colorProvider = {
                        getBtnColor(
                            element = it,
                            connectedPairs = connectedPairs,
                            selected = rightSelected
                        )
                    },
                    onGloballyPositioned = { element, coordinates ->
                        val point = PointInfo(
                            x = rightColumnSize.width + coordinates.positionInParent().x,
                            y = coordinates.positionInParent().y + coordinates.size.height / 2
                        )
                        rightCoordinates = rightCoordinates.plus(element to point)
                    },
                    onClick = { onRightClicked.invoke(it) }
                )
            }
        }
    }
}

@Composable
private fun placeButtons(
    elements: List<ModelPairElement>,
    colorProvider: (ModelPairElement) -> Color,
    onGloballyPositioned: (ModelPairElement, LayoutCoordinates) -> Unit,
    onClick: (ModelPairElement) -> Unit
) {
    for (element in elements) {
        val color = colorProvider.invoke(element)
        val animatedButtonColor = animateColorAsState(
            targetValue = color,
            animationSpec = tween(500)
        )
        ButtonEmpty(
            modifier = Modifier.onGloballyPositioned {
                onGloballyPositioned.invoke(element, it)
            },
            text = element.text,
            textColor = animatedButtonColor.value,
            borderColor = animatedButtonColor.value,
            onClick = { onClick.invoke(element) }
        )
        if (elements.last() != element) {
            AppSpacer(height = AppTheme.dimens.x2)
        }
    }
}

private fun getConnectionPath(
    start: PointInfo,
    end: PointInfo,
    density: Density
): Path {
    val offset = with(density) {
        val multiplier = if (start.x <= end.x) {
            1
        } else {
            -1
        }
        60.dp.toPx() * multiplier
    }
    return Path().apply {
        moveTo(start.x, start.y)
        cubicTo(
            start.x.plus(offset),
            start.y,
            end.x.minus(offset),
            end.y,
            end.x,
            end.y
        )
    }
}

private fun getBtnColor(
    element: ModelPairElement,
    connectedPairs: List<ModelPairConnection>,
    selected: ModelPairElement?,
): Color {
    val pair =
        connectedPairs.firstOrNull { it.start == element || it.end == element }
    return if (pair != null && pair.isCorrect) {
        AppTheme.color.SuccessDark
    } else if (pair != null && pair.isCorrect.not()) {
        AppTheme.color.ErrorDark
    } else if (selected == element) {
        AppTheme.color.Accent
    } else {
        AppTheme.color.TextPrimary
    }
}

private fun Boolean.isRightToColor() =
    if (this) AppTheme.color.SuccessDark else AppTheme.color.ErrorDark

@Preview(showBackground = true)
@Composable
private fun Preview() {
    val question = MockHelper.getPairsQuestionMock1()
    ElementPairsQuestion(
        modelPairsQuestion = MockHelper.getPairsQuestionMock1(),
        leftSelected = null,
        rightSelected = null,
        onLeftClicked = {},
        onRightClicked = {},
        connectedPairs = listOf(
            ModelPairConnection(
                start = question.left.get(0),
                end = question.right.get(2),
                isCorrect = true
            ),
            ModelPairConnection(
                start = question.right.get(3),
                end = question.left.get(1),
                isCorrect = false
            )
        ),
        newPairConnection = null,
        onPairAnimationDrawn = {}
    )
}