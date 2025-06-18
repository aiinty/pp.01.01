package com.aiinty.copayment.presentation.model

import com.aiinty.copayment.domain.model.Contact

data class ContactWithMaskedCard(
    val contact: Contact,
    val maskedCard: String
)