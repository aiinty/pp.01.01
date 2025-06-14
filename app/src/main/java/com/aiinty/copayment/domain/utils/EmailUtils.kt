package com.aiinty.copayment.domain.utils

object EmailUtils {
    fun maskEmailWithStars(email: String): String {
        val atIndex = email.indexOf("@")
        if (atIndex <= 0) return "*****"

        val firstChar = email.first()
        val domainPart = email.substring(atIndex)
        return "$firstChar****$domainPart"
    }
}