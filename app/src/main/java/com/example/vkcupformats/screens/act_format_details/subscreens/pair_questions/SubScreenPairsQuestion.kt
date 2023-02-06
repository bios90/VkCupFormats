package com.example.vkcupformats.screens.act_format_details.subscreens.pair_questions

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
import com.example.vkcupformats.data.elements.ModelPairConnection
import com.example.vkcupformats.data.elements.ModelPairElement
import com.example.vkcupformats.data.elements.ModelPairsQuestionItem
import com.example.vkcupformats.data.elements.isCorrectAnswer
import com.example.vkcupformats.ui.common.appShadow
import com.example.vkcupformats.ui.common.isScrolledToEnd
import com.example.vkcupformats.ui.common.modifyIf
import com.example.vkcupformats.ui.subviews.AppSpacerItem
import com.example.vkcupformats.ui.subviews.ElementPairsQuestion
import com.example.vkcupformats.ui.theme.AppTheme

@Composable
fun SubScreenPairsQuestion(
    items: List<ModelPairsQuestionItem>,
    onElementClick: (ModelPairsQuestionItem, ModelPairElement) -> Unit,
    onConnectionAnimationDrawn: (ModelPairsQuestionItem, ModelPairConnection) -> Unit,
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
                    item(key = item.id) {
                        val onClick: (ModelPairElement) -> Unit =
                            { onElementClick.invoke(item, it) }
                        PairsQuestionItem(
                            item = item,
                            onLeftClicked = onClick,
                            onRightClicked = onClick,
                            onConnectionAnimationDrawn = {
                                onConnectionAnimationDrawn.invoke(item, it)
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
private fun PairsQuestionItem(
    item: ModelPairsQuestionItem,
    onLeftClicked: (ModelPairElement) -> Unit,
    onRightClicked: (ModelPairElement) -> Unit,
    onConnectionAnimationDrawn: (ModelPairConnection) -> Unit
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
            .padding(vertical = AppTheme.dimens.x3)
    ) {
        ElementPairsQuestion(
            modelPairsQuestion = item.pairsQuestion,
            leftSelected = item.leftSelected,
            rightSelected = item.rightSelected,
            onLeftClicked = {
                onLeftClicked.invoke(it)
            },
            onRightClicked = {
                onRightClicked.invoke(it)
            },
            connectedPairs = item.connectedPairs,
            newPairConnection = item.pairToConnect,
            onPairAnimationDrawn = onConnectionAnimationDrawn
        )
    }
}