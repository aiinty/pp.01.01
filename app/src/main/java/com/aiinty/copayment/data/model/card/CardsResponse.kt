package com.aiinty.copayment.data.model.card

data class CardsResponse(
    val id: String? = null,
    val user_id: String,
    val card_number: String,
    val card_style: Int = 0,
    val cardholder_name: String,
    val expiration_date: String,
    val cvv: String,
    val balance: Double = 0.0,
    val is_frozen: Boolean = false,
    val is_contactless_disabled: Boolean = false,
    val is_magstripe_disabled: Boolean = false
)
