package com.aiinty.copayment.data.network

data class ApiError(
    val code: Int? = null,
    val error_code: String? = null,
    val msg: String? = null,
    val statusCode: String? = null,
    val error: String? = null,
    val message: String? = null
)