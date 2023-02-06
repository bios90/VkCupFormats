package com.example.vkcupformats.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import com.example.vkcupformats.ui.common.color
import com.example.vkcupformats.ui.common.withAlpha

class Colors {
    val Transparent by mutableStateOf("#00000000".color)
    val Background by mutableStateOf("#000000".color)
    val Surface by mutableStateOf("#FFFFFF".color)
    val SurfaceTernary by mutableStateOf("#E4EAF1".color)
    val TextPrimary by mutableStateOf("#100C08".color)
    val TextSecondary by mutableStateOf("#100C08".color.withAlpha(0.48f))
    val Accent by mutableStateOf("#FF5317".color)
    val Success by mutableStateOf("#A6EAB7".color)
    val SuccessDark by mutableStateOf("#4BB34B".color)
    val Error by mutableStateOf("#FB706E".color)
    val ErrorDark by mutableStateOf("#EB2F2D".color)
}
