package com.example.vkcupformats.ui.subviews

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class NonScrollableViewPager @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ViewPager(context, attrs) {

    var scrollEnabled = false

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        if (this.scrollEnabled) {
            return super.onTouchEvent(ev)
        }
        return false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (this.scrollEnabled) {
            return super.onInterceptTouchEvent(ev)
        }

        return false
    }


}