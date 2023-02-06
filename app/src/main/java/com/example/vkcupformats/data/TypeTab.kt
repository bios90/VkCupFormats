package com.example.vkcupformats.data

import com.example.vkcupformats.R

enum class TypeTab {
    RATINGS,
    QUESTION,
    PAIRS,
    BUBBLES,
    INPUTS
}

fun TypeTab.getIconResId(): Int = when (this) {
    TypeTab.RATINGS -> R.drawable.ic_star_faw_empty
    TypeTab.QUESTION -> R.drawable.ic_question
    TypeTab.PAIRS -> R.drawable.ic_half_circle
    TypeTab.BUBBLES -> R.drawable.ic_comment
    TypeTab.INPUTS -> R.drawable.ic_keyboard
}

fun TypeTab.getTabTitleResId(): Int = when (this) {
    TypeTab.RATINGS -> R.string.rating
    TypeTab.QUESTION -> R.string.question
    TypeTab.PAIRS -> R.string.pairs
    TypeTab.BUBBLES -> R.string.select
    TypeTab.INPUTS -> R.string.input
}