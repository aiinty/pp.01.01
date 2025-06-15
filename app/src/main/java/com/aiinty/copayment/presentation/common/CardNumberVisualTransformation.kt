package com.aiinty.copayment.presentation.common

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class CardNumberVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = text.text.take(16)
        val formatted = buildString {
            for (i in trimmed.indices) {
                append(trimmed[i])
                if ((i + 1) % 4 == 0 && i != 15) append(' ')
            }
        }
        val mapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                var spaces = 0
                for (i in 1..offset) {
                    if (i % 4 == 0 && i != 16) spaces++
                }
                return offset + spaces
            }
            override fun transformedToOriginal(offset: Int): Int {
                return offset - (0 until offset).count { (it % 5 == 4) }
            }
        }
        return TransformedText(AnnotatedString(formatted), mapping)
    }
}
