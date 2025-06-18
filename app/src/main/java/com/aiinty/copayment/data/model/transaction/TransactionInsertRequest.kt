package com.aiinty.copayment.data.model.transaction

data class TransactionInsertRequest(
    val type_id: Int,
    val amount: Double,
    val sender_card_id: String?,
    val receiver_card_id: String?,
    val initiator_user_id: String?
)
