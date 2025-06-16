package com.aiinty.copayment.domain.repository

import com.aiinty.copayment.domain.model.Card
import com.aiinty.copayment.domain.model.Transaction

interface TransactionRepository {
    suspend fun getTransactions(userId: String?, cardId: String?, range: String): Result<List<Transaction>>
    suspend fun insertTransaction(transaction: Transaction): Result<Unit>
}