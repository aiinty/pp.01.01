package com.aiinty.copayment.domain.usecase.profile

import com.aiinty.copayment.domain.model.Contact
import com.aiinty.copayment.domain.repository.ContactRepository

class GetContactsUseCase(
    private val contactRepository: ContactRepository
) {

    suspend operator fun invoke(userId: String, range: String = "0-9"): Result<List<Contact>> {
        return contactRepository.getContacts(userId, range)
    }
}
