package com.aiinty.copayment.data.model.auth

data class UpdateUserRequest(
    val email: String? = null,
    val password: String? = null
)
