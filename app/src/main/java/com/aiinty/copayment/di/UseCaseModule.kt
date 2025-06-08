package com.aiinty.copayment.di

import com.aiinty.copayment.domain.repository.UserRepository
import com.aiinty.copayment.domain.usecase.RecoverUseCase
import com.aiinty.copayment.domain.usecase.SignInUseCase
import com.aiinty.copayment.domain.usecase.SignUpUseCase
import com.aiinty.copayment.domain.usecase.UpdateUserUseCase
import com.aiinty.copayment.domain.usecase.VerifyOTPUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideSignInUseCase(
        userRepository: UserRepository
    ): SignInUseCase {
        return SignInUseCase(userRepository)
    }

    @Provides
    fun provideSignUpUseCase(
        userRepository: UserRepository
    ): SignUpUseCase {
        return SignUpUseCase(userRepository)
    }

    @Provides
    fun provideVerifyOTPUseCase(
        userRepository: UserRepository
    ): VerifyOTPUseCase {
        return VerifyOTPUseCase(userRepository)
    }

    @Provides
    fun provideRecoverUseCase(
        userRepository: UserRepository
    ): RecoverUseCase {
        return RecoverUseCase(userRepository)
    }

    @Provides
    fun provideUpdateUserUseCase(
        userRepository: UserRepository
    ): UpdateUserUseCase {
        return UpdateUserUseCase(userRepository)
    }
}
