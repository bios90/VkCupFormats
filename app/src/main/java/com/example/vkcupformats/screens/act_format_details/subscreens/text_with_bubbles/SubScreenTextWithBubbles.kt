package com.example.vkcupformats.screens.act_format_details.subscreens.text_with_bubbles

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.vkcupformats.data.elements.MissingTextElement
import com.example.vkcupformats.data.elements.ModelTextWithBubblesItem
import com.example.vkcupformats.data.elements.isCorrectAnswer
import com.example.vkcupformats.ui.common.appShadow
import com.example.vkcupformats.ui.common.isScrolledToEnd
import com.example.vkcupformats.ui.common.modifyIf
import com.example.vkcupformats.ui.subviews.AppSpacerItem
import com.example.vkcupformats.ui.subviews.BubbleInfo
import com.example.vkcupformats.ui.subviews.ElementTextWithBubbles
import com.example.vkcupformats.ui.theme.AppTheme

@Composable
fun SubScreenTextWithBubbles(
    items: List<ModelTextWithBubblesItem>,
    onBubbleClicked: (ModelTextWithBubblesItem, BubbleInfo) -> Unit,
    onMissingWordClicked: (ModelTextWithBubblesItem, MissingTextElement.MissingWord) -> Unit,
    onScrolledToBottom: () -> Unit
) {
    val scrollState = rememberLazyListState()
    val endOfListReached by remember {
        derivedStateOf {
            scrollState.isScrolledToEnd()
        }
    }
    LaunchedEffect(endOfListReached) {
        if (endOfListReached) {
            onScrolledToBottom.invoke()
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = scrollState,
            content = {
                for (item in items) {
                    AppSpacerItem(height = AppTheme.dimens.x2)
                    item(
                        key = item.id
                    ) {
                        TextWithBubblesItem(
                            item = item,
                            onBubbleClicked = {
                                onBubbleClicked.invoke(item, it)
                            },
                            onMissingWordClicked = {
                                onMissingWordClicked.invoke(item, it)
                            }
                        )
                    }
                }
                AppSpacerItem(height = AppTheme.dimens.x2)
            }
        )
    }
}

@Composable
private fun TextWithBubblesItem(
    item: ModelTextWithBubblesItem,
    onBubbleClicked: (BubbleInfo) -> Unit,
    onMissingWordClicked: (MissingTextElement.MissingWord) -> Unit
) {
    val shape = RoundedCornerShape(AppTheme.dimens.x3)
    val borderWidth = animateDpAsState(
        targetValue = if (item.isCorrectAnswer()) AppTheme.dimens.x05 else 0.dp,
        animationSpec = tween(
            500,
            easing = LinearEasing
        )
    )
    Box(
        modifier = Modifier
            .appShadow()
            .clip(shape)
            .background(AppTheme.color.Surface)
            .modifyIf(
                condition = item.isCorrectAnswer(),
                modifier = {
                    border(
                        width = borderWidth.value,
                        color = AppTheme.color.SuccessDark,
                        shape = shape
                    )
                }
            )
            .padding(AppTheme.dimens.x4)
    ) {
        ElementTextWithBubbles(
            text = item.text,
            selectedBubble = item.selectedBubble,
            missingWordBubbles = item.bubbles,
            wordPlacings = item.wordPlacings,
            onBubbleClicked = onBubbleClicked,
            onMissingWordPlaceholderClicked = onMissingWordClicked
        )
    }
}