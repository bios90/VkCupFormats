package com.example.vkcupformats.ui.subviews

import android.view.LayoutInflater
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.widget.addTextChangedListener
import com.example.vkcupformats.databinding.PlaceHolderEtBinding

@Composable
fun PlaceHolderEditText(
    modifier: Modifier = Modifier,
    inputText: String,
    textColor: Color,
    isEditable: Boolean,
    onTextChanged: (String) -> Unit
) {
    AndroidView(
        modifier = modifier,
        update = { editText ->
            editText.setTextColor(textColor.toArgb())
            if (inputText != editText.text.toString()) {
                editText.setText(inputText)
            }
            editText.isEnabled = isEditable
        },
        factory = {
            PlaceHolderEtBinding.inflate(LayoutInflater.from(it), null, false).root.apply {
                addTextChangedListener(
                    afterTextChanged = {
                        val text = it.toString()
                        onTextChanged.invoke(text)
                    }
                )
            }
        }
    )
}