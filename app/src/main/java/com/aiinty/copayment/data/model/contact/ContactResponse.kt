package com.aiinty.copayment.data.model.contact

import com.aiinty.copayment.data.model.profile.ProfileResponse

data class ContactResponse(
    val id: String,
    val user_id: String,
    val contact_user_id: String,
    val card_id: String,
    val profiles: ProfileResponse
)