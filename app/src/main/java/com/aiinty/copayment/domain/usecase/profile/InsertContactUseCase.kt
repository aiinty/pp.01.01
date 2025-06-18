package com.aiinty.copayment.domain.usecase.profile

import com.aiinty.copayment.domain.model.Contact
import com.aiinty.copayment.domain.model.Transaction
import com.aiinty.copayment.domain.repository.ContactRepository
import com.aiinty.copayment.domain.repository.TransactionRepository

class InsertContactUseCase(
    private val contactRepository: ContactRepository
) {

    suspend operator fun invoke(contact: Contact): Result<Unit> {
        return contactRepository.insertContact(contact)
    }
}
