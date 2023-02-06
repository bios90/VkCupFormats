package com.example.vkcupformats.base.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.vkcupformats.base.BaseActivity

typealias StateResult <State, Effect> = Pair<State, Set<Effect>>

abstract class BaseViewModel<State, Effect> : ViewModel() {
    abstract val initialState: State
    private val stateResultInner: StateResultLiveData<State, Effect> = StateResultLiveData()
    val stateResult: LiveData<StateResultEvent<State, Effect>> = stateResultInner
    val currentState: State
        get() = requireNotNull(stateResult.value?.peekData()?.first)


    open fun onCreate(act: BaseActivity) {
        if (stateResultInner.value == null) {
            stateResultInner.value = StateResultEvent(initialState to emptySet())
        }
    }

    fun setStateResult(state: State, effects: Set<Effect> = emptySet()) {
        stateResultInner.value = StateResultEvent(state to effects)
    }

    open fun onStart(act: BaseActivity) = Unit
    open fun onResume(act: BaseActivity) = Unit
    open fun onPause(act: BaseActivity) = Unit
    open fun onStop(act: BaseActivity) = Unit
    open fun onDestroy(act: BaseActivity) = Unit
}
