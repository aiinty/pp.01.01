package com.aiinty.copayment.presentation.navigation

import com.aiinty.copayment.domain.model.OTPType
import com.aiinty.copayment.domain.model.TransactionType

sealed class NavigationEvent {
    data class ToRoute(val route: String): NavigationEvent()
    data object Back: NavigationEvent()

    data class ToVerifyOTP(
        val type: OTPType,
        val email: String,
        val nextDestination: String? = null
    ) : NavigationEvent()
    data class ToTransaction(
        val type: TransactionType,
    ) : NavigationEvent()
}