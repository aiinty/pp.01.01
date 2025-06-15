package com.aiinty.copayment.data.model.auth

data class AuthResponse(
    val access_token: String?,
    val refresh_token: String?,
    val user: AuthUser?
)
