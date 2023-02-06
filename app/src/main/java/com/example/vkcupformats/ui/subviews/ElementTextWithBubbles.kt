package com.example.vkcupformats.ui.subviews

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vkcupformats.data.elements.MissingTextElement
import com.example.vkcupformats.data.elements.ModelTextWithMissingWords
import com.example.vkcupformats.data.utils.MockHelper
import com.example.vkcupformats.ui.common.appClickable
import com.example.vkcupformats.ui.common.primaryTextColor
import com.example.vkcupformats.ui.common.withAlpha
import com.example.vkcupformats.ui.theme.AppTheme
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer

data class BubbleInfo(
    val missingWord: MissingTextElement.MissingWord,
)

data class WordPlacementInfo(
    val expectedWord: MissingTextElement.MissingWord,
    val filledWord: MissingTextElement.MissingWord,
)

fun WordPlacementInfo.isCorrect() = expectedWord.id == filledWord.id

fun WordPlacementInfo.getColor() = when (isCorrect()) {
    true -> AppTheme.color.SuccessDark
    false -> AppTheme.color.ErrorDark
}

@Composable
fun ElementTextWithBubbles(
    modifier: Modifier = Modifier,
    text: ModelTextWithMissingWords,
    selectedBubble: BubbleInfo?,
    missingWordBubbles: List<BubbleInfo>,
    wordPlacings: List<WordPlacementInfo>,
    onBubbleClicked: (BubbleInfo) -> Unit,
    onMissingWordPlaceholderClicked: (MissingTextElement.MissingWord) -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        FlowRow(
            modifier = Modifier.animateContentSize(),
            crossAxisAlignment = FlowCrossAxisAlignment.End
        ) {
            for (element in text.elements) {
                if (element is MissingTextElement.Word) {
                    Text(
                        style = AppTheme.typography.RegXl.primaryTextColor(),
                        text = element.word.plus(" ")
                    )
                } else if (element is MissingTextElement.MissingWord) {
                    val placement = wordPlacings.firstOrNull { it.expectedWord.id == element.id }
                    if (placement != null) {
                        val animatedTextColor = animateColorAsState(
                            targetValue = placement.getColor(),
                            animationSpec = tween(500)
                        )
                        Text(
                            color = animatedTextColor.value,
                            style = AppTheme.typography.BoldXl,
                            text = placement.filledWord.word.plus(" ")
                        )
                    } else {
                        MissingWordBox(
                            isSelectionMode = selectedBubble != null,
                            onClick = { onMissingWordPlaceholderClicked.invoke(element) }
                        )
                        Text(
                            style = AppTheme.typography.RegXl,
                            text = " "
                        )
                    }
                } else if (element is MissingTextElement.ForceNewLine) {
                    Box(modifier = Modifier.fillMaxWidth())
                }
            }
        }
        AppSpacer(height = AppTheme.dimens.x8)
        FlowRow(
            mainAxisSpacing = AppTheme.dimens.x2,
            crossAxisSpacing = AppTheme.dimens.x2
        ) {
            for (bubble in missingWordBubbles) {
                val alreadyUsed = wordPlacings.any { it.filledWord.id == bubble.missingWord.id }
                val color = when {
                    selectedBubble == bubble -> AppTheme.color.Accent
                    alreadyUsed -> AppTheme.color.TextPrimary.withAlpha(0.2f)
                    else -> AppTheme.color.TextPrimary
                }
                val animatedButtonColor = animateColorAsState(
                    targetValue = color,
                    animationSpec = tween(500)
                )
                ButtonEmpty(
                    isEnabled = alreadyUsed.not(),
                    textColor = animatedButtonColor.value,
                    borderColor = animatedButtonColor.value,
                    text = bubble.missingWord.word,
                    onClick = { onBubbleClicked.invoke(bubble) }
                )
            }
        }
    }
}

@Composable
fun MissingWordBox(
    isSelectionMode: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(AppTheme.dimens.x10)
            .height(AppTheme.dimens.x6)
            .padding(bottom = AppTheme.dimens.x1)
            .placeholder(
                shape = CircleShape,
                visible = isSelectionMode,
                color = AppTheme.color.TextSecondary.withAlpha(0.2f),
                highlight = PlaceholderHighlight.shimmer(
                    highlightColor = AppTheme.color.Surface.withAlpha(0.5f)
                )
            )
            .appClickable {
                onClick.invoke()
            }
    ) {
        if (isSelectionMode.not()) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(AppTheme.color.TextPrimary)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    ElementTextWithBubbles(
        modifier = Modifier.padding(AppTheme.dimens.x4),
        text = MockHelper.getTextWithMissingWordsMock1(),
        selectedBubble = null,
        missingWordBubbles = emptyList(),
        wordPlacings = emptyList(),
        onBubbleClicked = {},
        onMissingWordPlaceholderClicked = {}
    )
}
