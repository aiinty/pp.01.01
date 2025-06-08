package com.aiinty.copayment.data.model

data class SignUpRequest(
    val email: String,
    val password: String,
    val data: SignUpData? = null
)

