package com.aiinty.copayment.data.model.profile

data class ProfileResponse (
    val id: String,
    val phone: String?,
    val full_name: String,
    val avatar_url: String
)