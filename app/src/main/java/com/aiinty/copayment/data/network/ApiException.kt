package com.aiinty.copayment.data.network

class ApiException(val apiError: ApiError) : Exception() {
    override val message: String?
        get() = apiError.msg
}