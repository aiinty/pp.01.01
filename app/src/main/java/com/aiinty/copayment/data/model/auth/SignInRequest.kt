package com.aiinty.copayment.data.model.auth

data class SignInRequest(
    val email: String,
    val password: String,
)