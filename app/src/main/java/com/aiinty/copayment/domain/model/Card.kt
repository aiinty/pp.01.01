package com.aiinty.copayment.domain.model

data class Card(
    val id: String,
    val userId: String,
    val cardNumber: String,
    val cardStyle: CardStyle,
    val cardHolderName: String,
    val expirationDate: String,
    val cvv: String,
    val isActive: Boolean,
    val balance: Double,
)
