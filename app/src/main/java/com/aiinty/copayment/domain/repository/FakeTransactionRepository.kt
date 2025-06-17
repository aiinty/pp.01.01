package com.aiinty.copayment.domain.repository

import com.aiinty.copayment.domain.model.Card
import com.aiinty.copayment.domain.model.Transaction
import com.aiinty.copayment.domain.model.TransactionType
import kotlinx.coroutines.delay
import java.time.OffsetDateTime

class FakeTransactionRepository : TransactionRepository {

    private val fakeTransactions = mutableListOf(
        Transaction(
            id = "0994585b-8995-499d-8754-978331bda9b7",
            senderId = "ebd7c411-bf57-4190-875b-53115fbc647e",
            receiverId = null,
            amount = 25.5,
            createdAt = OffsetDateTime.now(),
            initiatorProfile = null,
            transactionType = TransactionType.WITHDRAW
        ),
        Transaction(
            id = "2994585b-8995-499d-8754-978331bda9b7",
            senderId = "ebd7c411-bf57-4190-875b-53115fbc647e",
            receiverId = "fbd7c411-bf57-4190-875b-53115fbc647e",
            amount = 100.0,
            createdAt = OffsetDateTime.now(),
            initiatorProfile = null,
            transactionType = TransactionType.TRANSFER
        )
    )

    override suspend fun getTransactions(userId: String?, cardId: String?, range: String): Result<List<Transaction>> {
        delay(500)
        val filtered = fakeTransactions.filter { tx ->
            (userId == null || tx.initiatorProfile?.id == userId) &&
                    (cardId == null || tx.senderId == cardId || tx.receiverId == cardId)
        }
        return Result.success(filtered)
    }

    override suspend fun insertTransaction(transaction: Transaction): Result<Unit> {
        delay(500)
        fakeTransactions.add(transaction)
        return Result.success(Unit)
    }
}
