package com.aiinty.copayment.data.network

data class ApiError(
    val code: Int,
    val error_code: String?,
    val msg: String?
)
