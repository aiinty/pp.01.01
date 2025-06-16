package com.aiinty.copayment.domain.model

data class Transaction(
    val id: String,
    val senderId: String?,
    val receiverId: String?,
    val amount: Double,
    val createdAt: String,
    val initiatorUserId: String?,
    val transactionType: TransactionType
) {
    val category: TransactionCategory get() = transactionType.category
}