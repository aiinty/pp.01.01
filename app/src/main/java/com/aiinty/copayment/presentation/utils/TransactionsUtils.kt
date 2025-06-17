package com.aiinty.copayment.presentation.utils

import com.aiinty.copayment.domain.model.Card
import com.aiinty.copayment.domain.model.Transaction
import com.aiinty.copayment.domain.model.TransactionType

object TransactionsUtils {
    fun isPositiveTransaction(card: Card, transaction: Transaction): Boolean {
        val trxType = transaction.transactionType

        return when (trxType) {
            TransactionType.DEPOSIT -> true
            TransactionType.TRANSFER -> transaction.senderId != card.id
            else -> false
        }
    }
}