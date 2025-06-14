package com.aiinty.copayment.domain.usecase.auth

import com.aiinty.copayment.domain.repository.UserRepository

class RefreshTokenUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(refreshToken: String): Result<Unit> {
        return userRepository.refreshToken(refreshToken)
    }
}