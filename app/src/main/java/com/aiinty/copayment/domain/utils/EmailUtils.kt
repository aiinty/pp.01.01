package com.aiinty.copayment.domain.utils

object EmailUtils {
    fun maskEmailWithStars(email: String): String {
        val atIndex = email.indexOf("@")
        val domainPart = email.substring(atIndex)
        return "*****$domainPart"
    }
}