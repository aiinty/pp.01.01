package com.aiinty.copayment.domain.usecase.auth

import com.aiinty.copayment.domain.model.OTPType
import com.aiinty.copayment.domain.repository.UserRepository

class VerifyOTPUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(type: OTPType, email: String, token: String): Result<Unit> {
        return userRepository.verifyOTP(type, email, token)
    }
}