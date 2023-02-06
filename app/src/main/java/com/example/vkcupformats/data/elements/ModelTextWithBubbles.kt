package com.example.vkcupformats.data.elements

import com.example.vkcupformats.ui.subviews.BubbleInfo
import com.example.vkcupformats.ui.subviews.WordPlacementInfo
import com.example.vkcupformats.ui.subviews.isCorrect

data class ModelTextWithBubblesItem(
    val id: Long,
    val text: ModelTextWithMissingWords,
    val bubbles: List<BubbleInfo>,
    val selectedBubble: BubbleInfo?,
    val wordPlacings: List<WordPlacementInfo>
)

fun ModelTextWithBubblesItem.isCorrectAnswer() =
    text.getMissingWords().size == wordPlacings.size && wordPlacings.all(WordPlacementInfo::isCorrect)