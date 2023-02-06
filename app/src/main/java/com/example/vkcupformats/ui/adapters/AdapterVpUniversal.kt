package com.example.vkcupformats.ui.adapters

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

class AdapterVpUniversal : PagerAdapter() {
    private var views: List<View> = arrayListOf()

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = views.get(position).rootView
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun getCount(): Int {
        return views.size
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    fun setViews(views: List<View>) {
        this.views = views
        notifyDataSetChanged()
    }
}