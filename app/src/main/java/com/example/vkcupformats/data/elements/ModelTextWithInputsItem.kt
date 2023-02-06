package com.example.vkcupformats.data.elements

import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.example.vkcupformats.ui.subviews.InputResult

data class ModelTextWithInputsItem(
    val id: Long,
    val text: ModelTextWithMissingWords,
    val inputs: SnapshotStateMap<MissingTextElement.MissingWord, InputResult>,
    val wordToAnimate: MissingTextElement.MissingWord?
)

fun ModelTextWithInputsItem.isCorrectAnswer() =
    text.getMissingWords().size == inputs.size && inputs.all { it.value.isCorrect }
