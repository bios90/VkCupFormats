package com.example.vkcupformats.screens.act_format_details.subscreens.text_with_inputs

import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.example.vkcupformats.base.randomInt
import com.example.vkcupformats.base.replace
import com.example.vkcupformats.base.vm.BaseViewModel
import com.example.vkcupformats.data.elements.MissingTextElement
import com.example.vkcupformats.data.elements.ModelTextWithInputsItem
import com.example.vkcupformats.data.elements.getMissingWords
import com.example.vkcupformats.data.utils.MockHelper
import com.example.vkcupformats.screens.act_format_details.subscreens.text_with_inputs.SubScreenTextWithInputsVm.Effect
import com.example.vkcupformats.screens.act_format_details.subscreens.text_with_inputs.SubScreenTextWithInputsVm.State
import com.example.vkcupformats.ui.subviews.InputResult

class SubScreenTextWithInputsVm : BaseViewModel<State, Effect>() {
    data class State(
        val items: List<ModelTextWithInputsItem>
    )

    sealed class Effect

    override val initialState: State = State(
        items = getTextsWithInputs()
    )

    private fun updateItem(
        itemOriginal: ModelTextWithInputsItem,
        itemUpdated: ModelTextWithInputsItem
    ) {
        val itemIndex = currentState.items.indexOfFirst { it.id == itemOriginal.id }
        if (itemIndex == -1) {
            return
        }

        val fixedItems = currentState.items.replace(
            index = itemIndex,
            item = itemUpdated,
        )
            .distinctBy(ModelTextWithInputsItem::id)
        setStateResult(
            state = currentState.copy(
                items = fixedItems
            )
        )
    }


    inner class UiListener {

        fun onInputTextChanged(
            item: ModelTextWithInputsItem,
            text: String,
            word: MissingTextElement.MissingWord
        ) {
            if (item.wordToAnimate != null) {
                return
            }

            val expectedText = item.text.getMissingWords()
                .firstOrNull { it.id == word.id }
                ?.word
            val isCorrect = expectedText.equals(text, true)
            val updatedItem = item.copy(
                inputs = item.inputs.apply {
                    put(word, InputResult(text, false))
                },
                wordToAnimate = word.takeIf { isCorrect }
            )
            updateItem(
                itemOriginal = item,
                itemUpdated = updatedItem
            )
        }

        fun onInputAnimationShown(
            item: ModelTextWithInputsItem,
            word: MissingTextElement.MissingWord
        ) {
            val text = item.inputs.get(word)?.text ?: word.word
            val updatedItem = item.copy(
                wordToAnimate = null,
                inputs = item.inputs.apply {
                    put(word, InputResult(text, true))
                },
            )
            updateItem(
                itemOriginal = item,
                itemUpdated = updatedItem,
            )
        }

        fun onScrolledToBottom() {
            val newItems = currentState.items
                .plus(getTextsWithInputs().shuffled())
                .distinctBy(ModelTextWithInputsItem::id)
            setStateResult(
                state = currentState.copy(items = newItems)
            )
        }
    }

    private fun getTextsWithInputs() = MockHelper.getTextsWithMissingWords()
        .map {
            ModelTextWithInputsItem(
                id = randomInt.toLong(),
                text = it,
                inputs = SnapshotStateMap(),
                wordToAnimate = null
            )
        }
        .distinctBy(ModelTextWithInputsItem::id)
}