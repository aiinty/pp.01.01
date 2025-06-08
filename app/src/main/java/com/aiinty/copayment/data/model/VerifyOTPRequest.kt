package com.aiinty.copayment.data.model

data class VerifyOTPRequest(
    val type: String,
    val email: String,
    val token: String,
)