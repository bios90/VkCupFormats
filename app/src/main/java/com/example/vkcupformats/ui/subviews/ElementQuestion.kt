package com.example.vkcupformats.ui.subviews

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.vkcupformats.base.pxToDp
import com.example.vkcupformats.data.elements.ModelAnswer
import com.example.vkcupformats.data.elements.ModelQuestion
import com.example.vkcupformats.data.elements.getAnswerPercent
import com.example.vkcupformats.data.utils.MockHelper
import com.example.vkcupformats.ui.common.*
import com.example.vkcupformats.ui.theme.AppTheme

@Composable
fun ElementQuestion(
    modifier: Modifier = Modifier,
    question: ModelQuestion,
    selectedAnswer: ModelAnswer?,
    titleText: String,
    onAnswerClicked: (ModelAnswer) -> Unit
) {
    Column(modifier = modifier) {
        Text(
            style = AppTheme.typography.RegM.secondaryTextColor().alignStart(),
            text = titleText
        )
        AppSpacer(height = AppTheme.dimens.x4)
        Text(
            style = AppTheme.typography.SemiBoldXl.primaryTextColor().alignStart(),
            text = question.text
        )
        for (answer in question.answers) {
            AppSpacer(height = AppTheme.dimens.x2)
            Answer(
                modelAnswer = answer,
                isSelected = answer == selectedAnswer,
                percent = question.getAnswerPercent(answer),
                onClick = {
                    if (selectedAnswer == null) {
                        onAnswerClicked.invoke(answer)
                    }
                },
                isShowResult = selectedAnswer != null
            )
        }
    }
}

@Composable
private fun Answer(
    modelAnswer: ModelAnswer,
    isSelected: Boolean,
    percent: Int,
    isShowResult: Boolean,
    onClick: () -> Unit
) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    var showResult by remember {
        mutableStateOf(false)
    }
    showResult = isShowResult
    var answerClicked by remember {
        mutableStateOf(false)
    }
    val overlayProgress = remember {
        Animatable(0f)
    }
    LaunchedEffect(key1 = answerClicked, key2 = isSelected, block = {
        if (answerClicked) {
            overlayProgress.animateTo(1f)
            onClick.invoke()
            answerClicked = false
        } else if (isSelected.not()) {
            overlayProgress.snapTo(0f)
        }
    })
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .appClickable {
                if (showResult.not()) {
                    answerClicked = true
                }
            }
            .clip(RoundedCornerShape(AppTheme.dimens.x1))
            .background(AppTheme.color.SurfaceTernary)
    ) {
        val overlayColor = if (modelAnswer.isRight) {
            AppTheme.color.Success
        } else {
            AppTheme.color.Error
        }
        val width = if (isSelected) {
            size.width.pxToDp()
        } else {
            size.width.pxToDp() * overlayProgress.value
        }
        Box(
            modifier = Modifier
                .height(size.height.pxToDp().dp)
                .width(width.dp)
                .background(overlayColor)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged { size = it }
                .padding(horizontal = AppTheme.dimens.x4, vertical = AppTheme.dimens.x3),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                style = AppTheme.typography.SemiBoldM.primaryTextColor().alignStart(),
                text = modelAnswer.text
            )
            AnimatedVisibilityFade(visible = isSelected) {
                val (vector, iconColor) = if (modelAnswer.isRight) {
                    Pair(Icons.Default.Check, AppTheme.color.SuccessDark)
                } else {
                    Pair(Icons.Default.Close, AppTheme.color.ErrorDark)
                }
                Image(
                    modifier = Modifier.size(AppTheme.dimens.x5),
                    imageVector = vector,
                    colorFilter = ColorFilter.tint(iconColor),
                    contentDescription = "ic_is_right"
                )
            }
            AnimatedVisibilityFade(visible = showResult) {
                Row {
                    AppSpacerHorizontal(width = AppTheme.dimens.x2)
                    Text(
                        style = AppTheme.typography.SemiBoldM.primaryTextColor(),
                        text = "$percent%"
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
)
@Composable
private fun Preview() {
    val question = MockHelper.getQuestionMock1()
    ElementQuestion(
        question = question,
        selectedAnswer = question.answers.get(0),
        onAnswerClicked = {},
        titleText = ""
    )
}
