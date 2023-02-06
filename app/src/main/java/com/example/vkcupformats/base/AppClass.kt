package com.example.vkcupformats.base

import android.app.Application

class AppClass : Application() {

    companion object {
        private lateinit var _app: AppClass
        val app by lazy { _app }
    }

    override fun onCreate() {
        super.onCreate()
        _app = this
    }
}