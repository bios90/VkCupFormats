package com.example.vkcupformats.screens.act_format_details.subscreens.pair_questions

import com.example.vkcupformats.base.randomInt
import com.example.vkcupformats.base.replace
import com.example.vkcupformats.base.vm.BaseViewModel
import com.example.vkcupformats.data.elements.ModelPairConnection
import com.example.vkcupformats.data.elements.ModelPairElement
import com.example.vkcupformats.data.elements.ModelPairsQuestionItem
import com.example.vkcupformats.data.utils.MockHelper
import com.example.vkcupformats.screens.act_format_details.subscreens.pair_questions.SubScreenPairsQuestionVm.Effect
import com.example.vkcupformats.screens.act_format_details.subscreens.pair_questions.SubScreenPairsQuestionVm.State

class SubScreenPairsQuestionVm : BaseViewModel<State, Effect>() {

    data class State(
        val items: List<ModelPairsQuestionItem>
    )

    sealed class Effect

    override val initialState: State = State(
        items = getPairsQuestionItems()
    )

    private fun updateItem(
        itemOriginal: ModelPairsQuestionItem,
        itemUpdated: ModelPairsQuestionItem
    ) {
        val itemIndex = currentState.items.indexOf(itemOriginal)
        if (itemIndex < -1) {
            return
        }
        val fixedItems = currentState.items.replace(
            index = itemIndex,
            item = itemUpdated,
        ).distinctBy(ModelPairsQuestionItem::id)
        setStateResult(
            state = currentState.copy(
                items = fixedItems
            )
        )
    }

    inner class UiListener {

        fun onPairsElementClicked(item: ModelPairsQuestionItem, element: ModelPairElement) {
            val isLeft = item.pairsQuestion.left.contains(element)
            val isRight = item.pairsQuestion.right.contains(element)
            if (isLeft.not() && isRight.not()) {
                return
            }
            if (item.connectedPairs.any { it.start == element || it.end == element }) {
                return
            }
            if (item.pairToConnect != null) {
                return
            }
            val opposite = if (isLeft) item.rightSelected else item.leftSelected
            val updatedItem = if (item.leftSelected == element) {
                item.copy(leftSelected = null)
            } else if (item.rightSelected == element) {
                item.copy(rightSelected = null)
            } else if (opposite != null) {
                val correctOpposite = if (isLeft) {
                    item.pairsQuestion.getRightPairElement(left = element)
                } else {
                    item.pairsQuestion.getLeftPairElement(right = element)
                }
                val isCorrect = correctOpposite == opposite
                val connection = ModelPairConnection(
                    start = opposite,
                    end = element,
                    isCorrect = isCorrect
                )
                item.copy(
                    pairToConnect = connection,
                    leftSelected = null,
                    rightSelected = null
                )
            } else {
                item.copy(
                    leftSelected = if (isLeft) element else item.leftSelected,
                    rightSelected = if (isRight) element else item.rightSelected
                )
            }
            updateItem(item, updatedItem)
        }

        fun onPairsAnimationDrawn(item: ModelPairsQuestionItem, connection: ModelPairConnection) {
            val itemUpdated = item.copy(
                connectedPairs = item.connectedPairs.plus(connection),
                pairToConnect = null
            )
            updateItem(item, itemUpdated)
        }

        fun onScrolledToBottom() {
            val newItems = currentState.items
                .plus(getPairsQuestionItems().shuffled())
                .distinctBy(ModelPairsQuestionItem::id)
            setStateResult(
                state = currentState.copy(
                    items = newItems
                )
            )
        }
    }

    fun getPairsQuestionItems() = MockHelper.getPairsQuestionsMock()
        .map {
            ModelPairsQuestionItem(
                id = randomInt.toLong(),
                pairsQuestion = it,
                connectedPairs = emptyList(),
                leftSelected = null,
                rightSelected = null,
                pairToConnect = null
            )
        }
        .distinctBy(ModelPairsQuestionItem::id)
}