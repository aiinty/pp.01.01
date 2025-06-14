package com.aiinty.copayment.domain.usecase.auth

import com.aiinty.copayment.domain.repository.UserRepository

class RecoverUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(email:String): Result<Unit> {
        return userRepository.recover(email)
    }
}