package com.aiinty.copayment.presentation.utils

object CardUtils {
    fun maskCardNumber(cardNumber: String): String {
        val clean = cardNumber.filter { it.isDigit() }
        return if (clean.length <= 4) clean
        else "*".repeat(clean.length - 4) + clean.takeLast(4)
    }

    fun formatCardNumberWithSpaces(cardNumber: String): String {
        return cardNumber.chunked(4).joinToString(" ")
    }
    fun formatExpiryToSlash(mmyy: String): String {
        val clean = mmyy.filter { it.isDigit() }.take(4)
        return when {
            clean.length < 3 -> clean
            else -> clean.substring(0, 2) + "/" + clean.substring(2)
        }
    }
}