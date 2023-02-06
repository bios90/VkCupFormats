package com.example.vkcupformats.screens.act_format_details.subscreens.ratings

import com.example.vkcupformats.base.getRoundedByRating
import com.example.vkcupformats.base.randomInt
import com.example.vkcupformats.base.replace
import com.example.vkcupformats.base.vm.BaseViewModel
import com.example.vkcupformats.data.elements.ModelRatingItem
import com.example.vkcupformats.data.utils.MockHelper
import com.example.vkcupformats.screens.act_format_details.subscreens.ratings.SubScreenRatingsVm.Effect
import com.example.vkcupformats.screens.act_format_details.subscreens.ratings.SubScreenRatingsVm.State

class SubScreenRatingsVm : BaseViewModel<State, Effect>() {

    data class State(
        val items: List<ModelRatingItem>
    )

    sealed class Effect

    override val initialState: State = State(
        items = getRatingItems()
    )

    inner class UiListener {

        fun onItemRatingChanged(item: ModelRatingItem, rating: Float) {
            val itemIndex = currentState.items.indexOf(item)
            if (itemIndex == -1) {
                return
            }
            if(item.rating == rating){
                return
            }
            val fixedRating = if (rating < 1f) {
                1f
            } else if (rating > 5f) {
                5f
            } else {
                rating.getRoundedByRating()
            }
            val updatedItem = item.copy(rating = fixedRating)
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
                .plus(getRatingItems().shuffled())
                .distinctBy(ModelRatingItem::id)
            setStateResult(
                state = currentState.copy(items = newItems)
            )
        }
    }

    private fun getRatingItems() = MockHelper.getRatingQuestions().map { text ->
        ModelRatingItem(
            id = randomInt.toLong(),
            text = text,
            rating = 1f,
        )
    }.distinctBy(ModelRatingItem::id)
}