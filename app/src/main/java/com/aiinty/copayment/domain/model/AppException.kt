package com.aiinty.copayment.domain.model

import androidx.annotation.StringRes

sealed class AppException : Exception() {
    data class UiTextError(val userMessage: String) : AppException()
    data class UiResError(@StringRes val resId: Int) : AppException()
}