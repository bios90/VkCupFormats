package com.example.vkcupformats.screens.act_format_details.subscreens.ratings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.vkcupformats.data.elements.ModelRatingItem
import com.example.vkcupformats.ui.common.appShadow
import com.example.vkcupformats.ui.common.color
import com.example.vkcupformats.ui.common.isScrolledToEnd
import com.example.vkcupformats.ui.common.primaryTextColor
import com.example.vkcupformats.ui.subviews.AppSpacer
import com.example.vkcupformats.ui.subviews.AppSpacerHorizontal
import com.example.vkcupformats.ui.subviews.AppSpacerItem
import com.example.vkcupformats.ui.subviews.ElementRatingBar
import com.example.vkcupformats.ui.theme.AppTheme

@Composable
fun SubScreenRatingsCompose(
    items: List<ModelRatingItem>,
    onRatingChanged: (ModelRatingItem, Float) -> Unit,
    onScrolledToBottom: () -> Unit,
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
                        RatingItem(
                            ratingItem = item,
                            onRatingChanged = { onRatingChanged.invoke(item, it) }
                        )
                    }
                }
                AppSpacerItem(height = AppTheme.dimens.x2)
            }
        )
    }
}

@Composable
private fun RatingItem(
    ratingItem: ModelRatingItem,
    onRatingChanged: (Float) -> Unit
) {
    var inputRating by remember {
        mutableStateOf(-1f)
    }
    LaunchedEffect(key1 = inputRating, block = {
        if (inputRating > 0) {
            onRatingChanged.invoke(inputRating)
        }
    })
    val shape = RoundedCornerShape(AppTheme.dimens.x3)

    Column(
        Modifier
            .appShadow()
            .clip(shape)
            .background(AppTheme.color.Surface)
            .padding(AppTheme.dimens.x4)
    ) {
        Text(
            style = AppTheme.typography.RegXl,
            text = ratingItem.text
        )
        AppSpacer(height = AppTheme.dimens.x2)
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ElementRatingBar(
                modifier = Modifier.weight(1f),
                starsCount = 5,
                colorEmpty = Color.LightGray,
                colorFilledFrom = "#F37335".color,
                colorFilledTo = "#FDC830".color,
                currentRating = ratingItem.rating,
                starSize = 36.dp,
                onRatingScrolled = { inputRating = it },
                onRatingClicked = { inputRating = it }
            )
            AppSpacerHorizontal(width = AppTheme.dimens.x3)
            Text(
                modifier = Modifier.width(AppTheme.dimens.x8),
                text = "${ratingItem.rating}",
                style = AppTheme.typography.SemiBoldL.primaryTextColor()
            )
        }
    }

}