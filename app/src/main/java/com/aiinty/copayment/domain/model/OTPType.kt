package com.aiinty.copayment.domain.model

enum class OTPType(val code: String) {
    EMAIL("email"),
    RECOVERY("recovery");

    companion object {
        fun otpTypeFromString(code: String): OTPType {
            return OTPType.values().find { it.code == code }
                ?: throw IllegalArgumentException("Unknown OTPType code: $code")
        }
    }
}