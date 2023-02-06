package com.example.vkcupformats.screens.act_format_details

import com.example.vkcupformats.base.vm.BaseViewModel
import com.example.vkcupformats.data.TypeTab
import com.example.vkcupformats.screens.act_format_details.ActFormatDetailsVm.Effect
import com.example.vkcupformats.screens.act_format_details.ActFormatDetailsVm.State

class ActFormatDetailsVm : BaseViewModel<State, Effect>() {
    data class State(
        val selectedTab: TypeTab
    )

    sealed class Effect

    override val initialState: State = State(
        selectedTab = TypeTab.values().first()
    )

    inner class UiListener {
        fun onTabClicked(tab: TypeTab) {
            setStateResult(
                state = currentState.copy(
                    selectedTab = tab
                )
            )
        }
    }
}