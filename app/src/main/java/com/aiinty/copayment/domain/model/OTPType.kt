package com.aiinty.copayment.domain.model

enum class OTPType(val code: String) {
    EMAIL("email"),
    RECOVERY("recovery"),
    EMAIL_CHANGE("email_change");

    companion object {
        fun otpTypeFromString(code: String): OTPType {
            return entries.find { it.code == code }
                ?: throw IllegalArgumentException("Unknown OTPType code: $code")
        }
    }
}