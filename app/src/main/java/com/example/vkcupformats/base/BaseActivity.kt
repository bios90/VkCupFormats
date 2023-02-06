package com.example.vkcupformats.base

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import com.bios.yacupsurpise.ui.common.utils.WindowWrapper
import com.example.vkcupformats.ui.common.getComposeRootView
import com.example.vkcupformats.ui.theme.AppTheme

abstract class BaseActivity : AppCompatActivity(), WindowWrapper {

    private val rootView: ComposeView by lazy { getComposeRootView(this) }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        setContentView(rootView)
    }

    protected fun setContent(content: @Composable () -> Unit) {
        rootView.setContent {
            AppTheme {
                ApplyScreenWindowData()
                content.invoke()
            }
        }
    }
}