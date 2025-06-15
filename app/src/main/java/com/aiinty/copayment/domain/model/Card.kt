package com.aiinty.copayment.domain.model

import java.util.Date

data class Card(
    val id: String,
    val cardNumber: String,
    val cardHolderName: String,
    val expirationDate: String,
    val cvv: String,
    val isActive: Boolean,
    val balance: Double,
    val cardStyle: CardStyle,
)
