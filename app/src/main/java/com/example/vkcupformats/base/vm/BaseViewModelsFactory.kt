@file:Suppress("UNCHECKED_CAST")

package com.example.vkcupformats.base.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vkcupformats.screens.act_format_details.ActFormatDetailsVm
import com.example.vkcupformats.screens.act_format_details.subscreens.pair_questions.SubScreenPairsQuestionVm
import com.example.vkcupformats.screens.act_format_details.subscreens.questions.SubScreenQuestionsVm
import com.example.vkcupformats.screens.act_format_details.subscreens.ratings.SubScreenRatingsVm
import com.example.vkcupformats.screens.act_format_details.subscreens.text_with_bubbles.SubScreenTextWithBubblesVm
import com.example.vkcupformats.screens.act_format_details.subscreens.text_with_inputs.SubScreenTextWithInputsVm
import java.io.Serializable

class BaseViewModelsFactory<V : ViewModel, A : Serializable>(
    val viewModelClass: Class<V>,
    val args: A? = null,
) : ViewModelProvider.Factory {


    override fun <V : ViewModel> create(modelClass: Class<V>): V {
        return when (viewModelClass) {
            ActFormatDetailsVm::class.java -> ActFormatDetailsVm()
            SubScreenRatingsVm::class.java -> SubScreenRatingsVm()
            SubScreenQuestionsVm::class.java -> SubScreenQuestionsVm()
            SubScreenPairsQuestionVm::class.java -> SubScreenPairsQuestionVm()
            SubScreenTextWithBubblesVm::class.java -> SubScreenTextWithBubblesVm()
            SubScreenTextWithInputsVm::class.java -> SubScreenTextWithInputsVm()
            else -> throw IllegalStateException("Trying to create unknown ViewModel")
        } as V
    }
}

inline fun <reified V : ViewModel> createViewModelFactory(): BaseViewModelsFactory<V, Serializable> =
    createViewModelFactory(args = null)

inline fun <reified V : ViewModel, A : Serializable> createViewModelFactory(args: A? = null): BaseViewModelsFactory<V, A> =
    BaseViewModelsFactory(
        viewModelClass = V::class.java,
        args = args
    )
