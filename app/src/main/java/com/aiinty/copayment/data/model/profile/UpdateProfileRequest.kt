package com.aiinty.copayment.data.model.profile

data class UpdateProfileRequest(
    val full_name: String,
    val avatar_url: String,
    val phone: String?,
)
