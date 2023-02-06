package com.example.vkcupformats.base.vm

import androidx.lifecycle.MutableLiveData

class StateResultLiveData<State, Effect> : MutableLiveData<StateResultEvent<State, Effect>>()
