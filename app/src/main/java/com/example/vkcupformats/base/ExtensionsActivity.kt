package com.example.vkcupformats.base

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.vkcupformats.base.vm.BaseViewModel

fun AppCompatActivity.addLifeCycleObserver(
    onCreate: (LifecycleOwner?) -> Unit = { },
    onStart: (LifecycleOwner?) -> Unit = { },
    onResume: (LifecycleOwner?) -> Unit = { },
    onPause: (LifecycleOwner?) -> Unit = { },
    onStop: (LifecycleOwner?) -> Unit = { },
    onDestroy: (LifecycleOwner?) -> Unit = { },
) = lifecycle.addObserver(
    object : DefaultLifecycleObserver {
        override fun onCreate(owner: LifecycleOwner) = onCreate.invoke(owner)
        override fun onStart(owner: LifecycleOwner) = onStart.invoke(owner)
        override fun onResume(owner: LifecycleOwner) = onResume.invoke(owner)
        override fun onPause(owner: LifecycleOwner) = onPause.invoke(owner)
        override fun onStop(owner: LifecycleOwner) = onStop.invoke(owner)
        override fun onDestroy(owner: LifecycleOwner) = onDestroy.invoke(owner)
    }
)

fun <State, Effects> subscribeState(
    act: BaseActivity,
    vm: BaseViewModel<State, Effects>,
    stateConsumer: (State) -> Unit,
    effectsConsumer: (Set<Effects>) -> Unit = {},
) {
    vm.stateResult.observe(act) { resultEvent ->
        val stateResult = resultEvent.getIfNotHandled()
        stateConsumer.invoke(stateResult.first)
        effectsConsumer.invoke(stateResult.second)
    }
    act.addLifeCycleObserver(
        onCreate = { vm.onCreate(act) },
        onResume = { vm.onResume(act) },
        onStart = { vm.onStart(act) },
        onPause = { vm.onPause(act) },
        onStop = { vm.onStop(act) },
        onDestroy = { vm.onDestroy(act) },
    )
}


fun Window.setStatusLightDark(isLight: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        decorView.windowInsetsController?.apply {
            if (isLight) {
                setSystemBarsAppearance(APPEARANCE_LIGHT_STATUS_BARS, APPEARANCE_LIGHT_STATUS_BARS)
            } else {
                setSystemBarsAppearance(0, APPEARANCE_LIGHT_STATUS_BARS)
            }
        }
    } else {
        var flags = this.decorView.systemUiVisibility
        if (isLight) {
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
        this.decorView.systemUiVisibility = flags
    }
}

fun Window.setNavBarLightDark(isLight: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        decorView.windowInsetsController?.apply {
            if (isLight) {
                setSystemBarsAppearance(
                    APPEARANCE_LIGHT_NAVIGATION_BARS,
                    APPEARANCE_LIGHT_NAVIGATION_BARS
                )
            } else {
                setSystemBarsAppearance(0, APPEARANCE_LIGHT_NAVIGATION_BARS)
            }
        }
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        var flags = this.decorView.systemUiVisibility
        if (isLight) {
            flags = flags and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
        } else {
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
        this.decorView.systemUiVisibility = flags
    }
}
