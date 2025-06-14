package com.aiinty.copayment.data.model

data class AuthUser(
    val id: String,
    val email: String,
    val user_metadata: UserMetadata
)