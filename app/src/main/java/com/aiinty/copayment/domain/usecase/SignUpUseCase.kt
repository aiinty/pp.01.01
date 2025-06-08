package com.aiinty.copayment.domain.usecase

import com.aiinty.copayment.data.model.SignUpData
import com.aiinty.copayment.domain.repository.UserRepository

class SignUpUseCase(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(email:String, password:String, data: SignUpData?): Result<Unit> {
        return userRepository.signUp(email, password, data)
    }
}