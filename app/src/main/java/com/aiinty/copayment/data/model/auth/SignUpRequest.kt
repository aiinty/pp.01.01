package com.aiinty.copayment.data.model.auth

data class SignUpRequest(
    val email: String,
    val password: String,
    val data: SignUpData? = null
)

