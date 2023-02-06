package com.example.vkcupformats.ui.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class TextCutFixTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TextCutFixFilter(
            text = text,
        )
    }
}

private fun TextCutFixFilter(text: AnnotatedString): TransformedText {

    val out = " " + text.text + " "
    val prefixOffset = 1

    val numberOffsetTranslator = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            return offset + prefixOffset
        }

        override fun transformedToOriginal(offset: Int): Int {
            if (offset < prefixOffset) return 0
            return offset - prefixOffset
        }
    }

    return TransformedText(AnnotatedString(out), numberOffsetTranslator)
}