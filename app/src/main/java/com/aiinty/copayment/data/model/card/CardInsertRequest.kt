package com.aiinty.copayment.data.model.card

data class CardInsertRequest(
    val user_id: String,
    val card_number: String,
    val card_style: Int = 0,
    val cardholder_name: String,
    val expiration_date: String,
    val cvv: String,
    val balance: Double = 0.0
)
