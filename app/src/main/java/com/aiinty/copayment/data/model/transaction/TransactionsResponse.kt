package com.aiinty.copayment.data.model.transaction

import com.aiinty.copayment.data.model.profile.ProfileResponse

data class TransactionsResponse(
    val id: String,
    val sender_id: String?,
    val receiver_id: String?,
    val amount: Double,
    val type: Int,
    val created_at: String,
    val initiator_user_id: String?,
    val profiles: ProfileResponse?,
)