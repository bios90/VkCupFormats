package com.example.vkcupformats.ui.subviews

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.key.*
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.vkcupformats.data.elements.MissingTextElement
import com.example.vkcupformats.data.elements.ModelTextWithMissingWords
import com.example.vkcupformats.data.utils.MockHelper
import com.example.vkcupformats.ui.common.primaryTextColor
import com.example.vkcupformats.ui.common.withAlpha
import com.example.vkcupformats.ui.theme.AppTheme
import com.example.vkcupformats.ui.utils.PathMeasureHelper
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import kotlinx.coroutines.launch

data class InputResult(
    val text: String,
    val isCorrect: Boolean
)

@Composable
fun ElementTextWithInputs(
    modifier: Modifier = Modifier.padding(horizontal = 6.dp),
    text: ModelTextWithMissingWords,
    wordToAnimate: MissingTextElement.MissingWord?,
    inputs: Map<MissingTextElement.MissingWord, InputResult>,
    onInputChanged: (String, MissingTextElement.MissingWord) -> Unit,
    onItemAnimationShown: (MissingTextElement.MissingWord) -> Unit
) {
    FlowRow(
        modifier = modifier.animateContentSize(),
        crossAxisAlignment = FlowCrossAxisAlignment.End
    ) {
        for (element in text.elements) {
            when (element) {
                is MissingTextElement.Word -> {
                    val nextElement = text.elements.getOrNull(text.elements.indexOf(element) + 1)
                    val text = buildString {
                        append(element.word)
                        if ((nextElement is MissingTextElement.MissingWord).not()) {
                            append(" ")
                        }
                    }
                    Text(
                        style = AppTheme.typography.RegXl.primaryTextColor(),
                        text = text
                    )
                }
                is MissingTextElement.MissingWord -> {
                    val inputData = inputs.get(element)
                    if (inputData?.isCorrect == true) {
                        Text(
                            style = AppTheme.typography.SemiBoldXl,
                            color = AppTheme.color.SuccessDark,
                            text = " " + inputData.text + " "
                        )
                    } else {
                        PlaceHolderWithAndroidView(
                            inputData = inputs.get(element),
                            onValueChange = { onInputChanged.invoke(it, element) },
                            showAnimation = wordToAnimate == element,
                            isEditable = (inputData?.isCorrect == true || wordToAnimate != null).not(),
                            onItemAnimationShown = { onItemAnimationShown.invoke(element) },
                        )
                    }
                }
                is MissingTextElement.ForceNewLine -> {
                    Box(modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PlaceHolderWithAndroidView(
    modifier: Modifier = Modifier,
    inputData: InputResult?,
    showAnimation: Boolean,
    isEditable: Boolean,
    onValueChange: (String) -> Unit,
    onItemAnimationShown: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val inputText = inputData?.text ?: ""
    val isCorrect = inputData?.isCorrect == true

    var animate: Boolean by remember {
        mutableStateOf(false)
    }
    animate = showAnimation
    val animProgress = remember {
        Animatable(0f)
    }
    val animColor = remember {
        Animatable(1f)
    }
    val textColor = if (isCorrect) {
        AppTheme.color.SuccessDark
    } else {
        lerp(AppTheme.color.SuccessDark, AppTheme.color.TextPrimary, animColor.value)
    }
    var pathMeasureHelper: PathMeasureHelper by remember {
        mutableStateOf(PathMeasureHelper(Path()))
    }
    var pathToDraw by remember {
        mutableStateOf(Path())
    }
    var pathForAnimation: Path? by remember {
        mutableStateOf(null)
    }
    val strokeWidth = with(LocalDensity.current) {
        1.4.dp.toPx()
    }
    var size by remember { mutableStateOf(IntSize.Zero) }

    LaunchedEffect(key1 = animate, block = {
        if (animate) {
            if (focusManager.moveFocus(FocusDirection.Down).not()) {
                focusManager.clearFocus(true)
                keyboardController?.hide()
            }
            pathToDraw = getPathForAnimation(
                width = size.width.toFloat(),
                height = size.height.toFloat(),
                strokeWidth = strokeWidth
            )
            pathMeasureHelper = PathMeasureHelper(pathToDraw)
            val startPoint = getStartPoint(
                width = size.width.toFloat(),
                height = size.height.toFloat(),
                strokeWidth = strokeWidth
            )
            pathForAnimation = Path().apply { moveTo(startPoint.first, startPoint.second) }
            animProgress.snapTo(0f)
            animColor.snapTo(1f)
            launch {
                animProgress.animateTo(
                    targetValue = 1f, animationSpec = tween(
                        1500,
                        easing = LinearEasing
                    )
                )
                animColor.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(
                        800
                    )
                )
                pathForAnimation = null
                onItemAnimationShown.invoke()
            }
        }
    })

    if (pathForAnimation != null) {
        val poses = pathMeasureHelper.getProgressPoint(animProgress.value)
        pathForAnimation?.lineTo(poses.first, poses.second)
    }

    PlaceHolderEditText(
        modifier = modifier
            .onGloballyPositioned { size = it.size }
            .wrapContentWidth()
            .defaultMinSize(
                minWidth = if (isCorrect) 0.dp else AppTheme.dimens.x10,
                minHeight = 0.dp
            )
            .drawBehind {
                if (showAnimation) {
                    pathForAnimation?.let {
                        drawPath(
                            path = it,
                            color = AppTheme.color.SuccessDark.withAlpha(animColor.value),
                            style = Stroke(strokeWidth)
                        )
                    }
                } else if (isCorrect.not()) {
                    val y = this.size.height - strokeWidth - 4.dp.toPx()

                    drawLine(
                        AppTheme.color.TextPrimary,
                        Offset(0f, y),
                        Offset(this.size.width, y),
                        strokeWidth
                    )
                }
            },
        isEditable = isEditable,
        inputText = inputText,
        textColor = textColor,
        onTextChanged = onValueChange
    )
}

private fun getStartPoint(width: Float, height: Float, strokeWidth: Float) =
    Pair(width / 2, height - strokeWidth)

private fun getPathForAnimation(width: Float, height: Float, strokeWidth: Float): Path {
    return Path()
        .apply {
            moveTo(width / 2, height - strokeWidth)
            lineTo(0 + (height - strokeWidth) / 2, height - strokeWidth)
            arcTo(
                rect = Rect(
                    0f + strokeWidth,
                    0f + strokeWidth,
                    height - strokeWidth,
                    height - strokeWidth
                ),
                90f,
                180f,
                true
            )
            lineTo(width - (height - strokeWidth) / 2, 0f + strokeWidth)
            arcTo(
                rect = Rect(
                    width - (height - strokeWidth),
                    0f + strokeWidth,
                    width,
                    height - strokeWidth
                ),
                270f,
                180f,
                true
            )
            lineTo(width / 2, height - strokeWidth)
        }
}


@Preview(showBackground = true)
@Composable
private fun Preview() {
    ElementTextWithInputs(
        text = MockHelper.getTextWithMissingWordsMock1(),
        inputs = emptyMap(),
        wordToAnimate = null,
        onInputChanged = { _, _ -> },
        onItemAnimationShown = {}
    )
}