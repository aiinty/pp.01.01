package com.aiinty.copayment.presentation.navigation

import com.aiinty.copayment.domain.model.OTPType

sealed class NavigationEvent {
    data class ToRoute(val route: String): NavigationEvent()
    data object Back: NavigationEvent()
    data class ToVerifyOTP(
        val type: OTPType,
        val email: String,
        val nextDestination: String? = null
    ) : NavigationEvent()
}