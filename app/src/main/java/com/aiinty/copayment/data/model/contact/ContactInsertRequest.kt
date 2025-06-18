package com.aiinty.copayment.data.model.contact

data class ContactInsertRequest(
    val user_id: String,
    val contact_user_id: String,
    val card_id: String,
)
