package com.aiinty.copayment.data.model.transaction

data class TransactionsResponse(
    val id: String,
    val sender_id: String?,
    val receiver_id: String?,
    val amount: Double,
    val type: Int,
    val created_at: String,
    val initiator_user_id: String?,
    val transaction_type: TransactionType
) {
    data class TransactionType(
        val id: Int,
        val name: String,
        val category_id: Int
    )
}