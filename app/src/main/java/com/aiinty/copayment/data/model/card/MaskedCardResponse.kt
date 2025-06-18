package com.aiinty.copayment.data.model.card

data class MaskedCardResponse(
    val card_id: String,
    val masked_number: String?,
)
