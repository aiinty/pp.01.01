package com.aiinty.copayment.domain.repository

import com.aiinty.copayment.data.model.auth.SignUpData
import com.aiinty.copayment.domain.model.OTPType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class FakeUserRepository : UserRepository {

    private var storedEmail: String? = null
    private var storedPassword: String? = null
    private var storedData: SignUpData? = null
    private var isVerified: Boolean = false
    private var accessToken: String? = null
    private var refreshToken: String? = null
    private var userId: String? = null

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    override suspend fun signUp(email: String, password: String, data: SignUpData?): Result<Unit> {
        return withContext(ioDispatcher) {
            delay(500)
            storedData = data
            storedEmail = email
            storedPassword = password
            isVerified = false

            accessToken = "fake_access_token_$email"
            refreshToken = "fake_refresh_token_$email"
            userId = "user_${email.hashCode()}"
            Result.success(Unit)
        }
    }

    override suspend fun signIn(email: String, password: String): Result<Unit> {
        return withContext(ioDispatcher) {
            delay(500)
            if (email == storedEmail && password == storedPassword) {

                accessToken = "fake_access_token_$email"
                refreshToken = "fake_refresh_token_$email"
                Result.success(Unit)
            } else {
                Result.failure(Exception("Invalid credentials"))
            }
        }
    }

    override suspend fun refreshToken(refreshToken: String): Result<Unit> {
        return withContext(ioDispatcher) {
            delay(500)
            if (this@FakeUserRepository.refreshToken == refreshToken) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Invalid Refresh Token"))
            }
        }
    }

    override suspend fun verifyOTP(type: OTPType, email: String, token: String): Result<Unit> {
        return withContext(ioDispatcher) {
            delay(300)
            if (email == storedEmail && token == "123456") {
                isVerified = true
                Result.success(Unit)
            } else {
                Result.failure(Exception("OTP verification failed"))
            }
        }
    }

    override suspend fun recover(email: String): Result<Unit> {
        return withContext(ioDispatcher) {
            delay(400)
            if (email == storedEmail) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Email not found"))
            }
        }
    }

    override suspend fun updateUser(email: String?, password: String?): Result<Unit> {
        return withContext(ioDispatcher) {
            delay(500)
            if (accessToken == accessToken) {
                email?.let { storedEmail = email }
                password.let { storedPassword = password }
                Result.success(Unit)
            } else {
                Result.failure(Exception("Access token expired"))
            }
        }
    }
}
