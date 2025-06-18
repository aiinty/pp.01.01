package com.aiinty.copayment.domain.repository

import com.aiinty.copayment.domain.model.Contact

interface ContactRepository {
    suspend fun getContacts(userId: String, range: String): Result<List<Contact>>
    suspend fun insertContact(contact: Contact): Result<Unit>
}