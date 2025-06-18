package com.aiinty.copayment.domain.repository

import com.aiinty.copayment.domain.model.Contact
import com.aiinty.copayment.domain.model.Transaction
import com.aiinty.copayment.domain.model.TransactionType
import kotlinx.coroutines.delay
import java.time.OffsetDateTime

class FakeContactRepository : ContactRepository {

    private val contacts = mutableListOf<Contact>()

    override suspend fun getContacts(userId: String, range: String): Result<List<Contact>> {
        delay(500)
        return Result.success(contacts.filter { it.userId == userId })
    }

    override suspend fun insertContact(contact: Contact): Result<Unit> {
        delay(500)
        contacts.add(contact)
        return Result.success(Unit)
    }
}