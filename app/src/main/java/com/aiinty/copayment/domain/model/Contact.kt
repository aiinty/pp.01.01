package com.aiinty.copayment.domain.model

data class Contact(
    val id: String,
    val userId: String,
    val profile: Profile,
    val cardId: String
)
