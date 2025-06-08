package com.aiinty.copayment.domain.repository

import com.aiinty.copayment.data.model.SignUpData
import com.aiinty.copayment.domain.model.OTPType

interface UserRepository {
    suspend fun signUp(email: String, password: String, data: SignUpData?): Result<Unit>
    suspend fun signIn(email: String, password: String): Result<Unit>
    suspend fun verifyOTP(type: OTPType, email: String, token: String): Result<Unit>
    suspend fun recover(email: String): Result<Unit>
    suspend fun updateUser(email: String?, password: String?): Result<Unit>
}