package com.example.vkcupformats.screens.act_format_details.subscreens.questions

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.vkcupformats.data.elements.ModelAnswer
import com.example.vkcupformats.data.elements.ModelQuestionItem
import com.example.vkcupformats.data.elements.isCorrectAnswer
import com.example.vkcupformats.ui.common.appShadow
import com.example.vkcupformats.ui.common.isScrolledToEnd
import com.example.vkcupformats.ui.common.modifyIf
import com.example.vkcupformats.ui.subviews.AppSpacerItem
import com.example.vkcupformats.ui.subviews.ElementQuestion
import com.example.vkcupformats.ui.theme.AppTheme

@Composable
fun SubScreenQuestions(
    items: List<ModelQuestionItem>,
    onAnswerSelected: (ModelQuestionItem, ModelAnswer) -> Unit,
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
                        QuestionItem(
                            questionItem = item,
                            overAllCount = items.size,
                            onAnswerSelected = { answer ->
                                onAnswerSelected.invoke(item, answer)
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
private fun QuestionItem(
    questionItem: ModelQuestionItem,
    overAllCount: Int,
    onAnswerSelected: (ModelAnswer) -> Unit
) {
    var answer: ModelAnswer? by remember {
        mutableStateOf(null)
    }
    answer = questionItem.selectedAnswer
    var answerSelected: ModelAnswer? by remember {
        mutableStateOf(null)
    }
    LaunchedEffect(key1 = answerSelected, block = {
        answerSelected?.let(onAnswerSelected)
    })
    val shape = RoundedCornerShape(AppTheme.dimens.x3)
    val text = "Вопрос ${questionItem.question.position}/$overAllCount"
    val borderWidth = animateDpAsState(
        targetValue = if (questionItem.isCorrectAnswer()) AppTheme.dimens.x05 else 0.dp,
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
                condition = questionItem.isCorrectAnswer(),
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
        ElementQuestion(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.dimens.x4),
            question = questionItem.question,
            titleText = text,
            selectedAnswer = answer,
            onAnswerClicked = { answerSelected = it }
        )
    }
}