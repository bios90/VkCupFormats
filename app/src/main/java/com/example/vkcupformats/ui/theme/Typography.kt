package com.example.vkcupformats.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.vkcupformats.R

class Typography {
    val LightS = getFontWithSize(size = TextSize.S, fontWeight = FontWeight.Light)
    val LightM = getFontWithSize(size = TextSize.M, fontWeight = FontWeight.Light)
    val LightL = getFontWithSize(size = TextSize.L, fontWeight = FontWeight.Light)
    val LightXl = getFontWithSize(size = TextSize.Xl, fontWeight = FontWeight.Light)
    val LightXxl = getFontWithSize(size = TextSize.Xxl, fontWeight = FontWeight.Light)
    val LightExtraLarge = getFontWithSize(size = TextSize.ExtraLarge, fontWeight = FontWeight.Light)

    val RegS = getFontWithSize(size = TextSize.S)
    val RegM = getFontWithSize(size = TextSize.M)
    val RegL = getFontWithSize(size = TextSize.L)
    val RegXl = getFontWithSize(size = TextSize.Xl)
    val RegXxl = getFontWithSize(size = TextSize.Xxl)
    val RegExtraLarge = getFontWithSize(size = TextSize.ExtraLarge)

    val SemiBoldS = getFontWithSize(size = TextSize.S, fontWeight = FontWeight.SemiBold)
    val SemiBoldM = getFontWithSize(size = TextSize.M, fontWeight = FontWeight.SemiBold)
    val SemiBoldL = getFontWithSize(size = TextSize.L, fontWeight = FontWeight.SemiBold)
    val SemiBoldXl = getFontWithSize(size = TextSize.Xl, fontWeight = FontWeight.SemiBold)
    val SemiBoldXxl = getFontWithSize(size = TextSize.Xxl, fontWeight = FontWeight.SemiBold)
    val SemiBoldExtraLarge = getFontWithSize(size = TextSize.ExtraLarge, fontWeight = FontWeight.SemiBold)

    val BoldS = getFontWithSize(size = TextSize.S, fontWeight = FontWeight.Bold)
    val BoldM = getFontWithSize(size = TextSize.M, fontWeight = FontWeight.Bold)
    val BoldL = getFontWithSize(size = TextSize.L, fontWeight = FontWeight.Bold)
    val BoldXl = getFontWithSize(size = TextSize.Xl, fontWeight = FontWeight.Bold)
    val BoldXxl = getFontWithSize(size = TextSize.Xxl, fontWeight = FontWeight.Bold)
    val BoldExtraLarge = getFontWithSize(size = TextSize.ExtraLarge, fontWeight = FontWeight.Bold)

    private fun getFontWithSize(size: Float, fontWeight: FontWeight = FontWeight.Normal) =
        TextStyle(
            fontFamily = Fonts.SanFrancisco,
            fontSize = size.sp,
            fontWeight = fontWeight,
            textAlign = TextAlign.Center,
            color = AppTheme.color.TextPrimary
        )
}

object Fonts {
    val SanFrancisco = FontFamily(
        Font(R.font.sf_light, FontWeight.Light),
        Font(R.font.sf_reg, FontWeight.Normal),
        Font(R.font.sf_semi_bold, FontWeight.SemiBold),
        Font(R.font.sf_bold, FontWeight.Bold),
    )
}

object TextSize {
    const val S = 12f
    const val M = 14f
    const val L = 16f
    const val Xl = 19f
    const val Xxl = 24f
    const val ExtraLarge = 36f
}
