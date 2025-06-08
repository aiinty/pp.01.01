package com.aiinty.copayment.data.model

data class AuthResponse(
    val access_token: String,
    val refresh_token: String,
    val user: User
)
