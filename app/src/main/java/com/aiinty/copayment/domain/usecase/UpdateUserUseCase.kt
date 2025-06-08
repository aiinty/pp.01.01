package com.aiinty.copayment.domain.usecase

import com.aiinty.copayment.domain.repository.UserRepository

class UpdateUserUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(email: String?, password: String?): Result<Unit> {
        return userRepository.updateUser(email, password)
    }
}