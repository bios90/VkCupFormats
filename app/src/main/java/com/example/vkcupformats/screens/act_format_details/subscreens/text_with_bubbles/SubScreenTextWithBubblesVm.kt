package com.example.vkcupformats.screens.act_format_details.subscreens.text_with_bubbles

import com.example.vkcupformats.base.randomInt
import com.example.vkcupformats.base.replace
import com.example.vkcupformats.base.vm.BaseViewModel
import com.example.vkcupformats.data.elements.MissingTextElement
import com.example.vkcupformats.data.elements.ModelTextWithBubblesItem
import com.example.vkcupformats.data.elements.getMissingWords
import com.example.vkcupformats.data.utils.MockHelper
import com.example.vkcupformats.screens.act_format_details.subscreens.text_with_bubbles.SubScreenTextWithBubblesVm.Effect
import com.example.vkcupformats.screens.act_format_details.subscreens.text_with_bubbles.SubScreenTextWithBubblesVm.State
import com.example.vkcupformats.ui.subviews.BubbleInfo
import com.example.vkcupformats.ui.subviews.WordPlacementInfo

class SubScreenTextWithBubblesVm : BaseViewModel<State, Effect>() {
    data class State(
        val items: List<ModelTextWithBubblesItem>
    )

    sealed class Effect

    override val initialState: State = State(
        items = getTextsWithBubbles()
    )

    private fun updateItem(
        itemOriginal: ModelTextWithBubblesItem,
        itemUpdated: ModelTextWithBubblesItem
    ) {
        val itemIndex = currentState.items.indexOf(itemOriginal)
        if (itemIndex < -1) {
            return
        }
        val fixedItems = currentState.items.replace(
            index = itemIndex,
            item = itemUpdated,
        )
            .distinctBy(ModelTextWithBubblesItem::id)
        setStateResult(
            state = currentState.copy(
                items = fixedItems
            )
        )
    }

    inner class UiListener {
        fun onBubbleClicked(item: ModelTextWithBubblesItem, bubble: BubbleInfo) {
            val updatedItem = if (item.selectedBubble == bubble) {
                item.copy(selectedBubble = null)
            } else {
                item.copy(selectedBubble = bubble)
            }
            updateItem(
                itemOriginal = item,
                itemUpdated = updatedItem
            )
        }

        fun onMissingWordClicked(
            item: ModelTextWithBubblesItem,
            word: MissingTextElement.MissingWord
        ) {
            item.selectedBubble?.let { selected ->
                val placing = WordPlacementInfo(
                    expectedWord = word,
                    filledWord = selected.missingWord
                )
                val updatedItem = item.copy(
                    selectedBubble = null,
                    wordPlacings = item.wordPlacings.plus(placing)
                )
                updateItem(
                    itemOriginal = item,
                    itemUpdated = updatedItem
                )
            }
        }

        fun onScrolledToBottom() {
            val newItems = currentState.items
                .plus(getTextsWithBubbles().shuffled())
                .distinctBy(ModelTextWithBubblesItem::id)
            setStateResult(
                state = currentState.copy(items = newItems)
            )
        }
    }

    private fun getTextsWithBubbles() = MockHelper.getTextsWithMissingWords()
        .map {
            ModelTextWithBubblesItem(
                id = randomInt.toLong(),
                text = it,
                bubbles = it.getMissingWords()
                    .map { BubbleInfo(it) }
                    .plus(
                        MockHelper.getMissingWords()
                            .take((2..10).random())
                            .map {
                                BubbleInfo(
                                    missingWord = MissingTextElement.MissingWord(
                                        id = randomInt.toLong(),
                                        word = it
                                    )
                                )
                            }
                            .distinctBy { it.missingWord.id }
                    )
                    .distinctBy { it.missingWord.id }
                    .shuffled(),
                selectedBubble = null,
                wordPlacings = emptyList(),
            )
        }
}