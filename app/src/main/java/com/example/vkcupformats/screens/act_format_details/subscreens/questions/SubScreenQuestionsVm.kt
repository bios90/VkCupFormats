package com.example.vkcupformats.screens.act_format_details.subscreens.questions

import com.example.vkcupformats.base.randomInt
import com.example.vkcupformats.base.replace
import com.example.vkcupformats.base.vm.BaseViewModel
import com.example.vkcupformats.data.elements.ModelAnswer
import com.example.vkcupformats.data.elements.ModelQuestionItem
import com.example.vkcupformats.data.utils.MockHelper
import com.example.vkcupformats.screens.act_format_details.subscreens.questions.SubScreenQuestionsVm.Effect
import com.example.vkcupformats.screens.act_format_details.subscreens.questions.SubScreenQuestionsVm.State

class SubScreenQuestionsVm : BaseViewModel<State, Effect>() {

    data class State(
        val items: List<ModelQuestionItem>
    )

    sealed class Effect

    override val initialState: State = State(
        items = getQuestionItems()
            .mapIndexed { index, item ->
                item.copy(
                    question = item.question.copy(
                        position = index + 1
                    )
                )
            }
    )

    inner class UiListener {
        fun onQuestionAnswerClicked(
            item: ModelQuestionItem,
            answer: ModelAnswer
        ) {
            val itemIndex = currentState.items.indexOf(item)
            if (itemIndex < -1) {
                return
            }
            val updatedItem = item.copy(selectedAnswer = answer)
            val fixedItems = currentState.items.replace(
                item = updatedItem,
                index = itemIndex
            )
            setStateResult(
                state = currentState.copy(
                    items = fixedItems
                )
            )
        }

        fun onScrolledToBottom() {
            val newItems = currentState.items
                .plus(getQuestionItems().shuffled())
                .distinctBy(ModelQuestionItem::id)
                .mapIndexed { index, item ->
                    item.copy(
                        question = item.question.copy(
                            position = index + 1
                        )
                    )
                }
            setStateResult(
                state = currentState.copy(items = newItems)
            )
        }
    }

    private fun getQuestionItems() = MockHelper.getQuestionsMock()
        .map {
            ModelQuestionItem(
                id = randomInt.toLong(),
                question = it.copy(
                    answers = it.answers.shuffled()
                ),
                selectedAnswer = null
            )
        }
        .distinctBy { it.id }
}