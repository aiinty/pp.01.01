package com.aiinty.copayment.domain.model

import java.time.OffsetDateTime

data class Transaction(
    val id: String,
    val senderId: String?,
    val receiverId: String?,
    val amount: Double,
    val createdAt: OffsetDateTime,
    val initiatorProfile: Profile?,
    val transactionType: TransactionType,
) {
    val category: TransactionCategory get() = transactionType.category
}