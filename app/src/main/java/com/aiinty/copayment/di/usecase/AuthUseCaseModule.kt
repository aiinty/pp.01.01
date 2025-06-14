package com.aiinty.copayment.di.usecase

import com.aiinty.copayment.domain.repository.UserRepository
import com.aiinty.copayment.domain.usecase.auth.RecoverUseCase
import com.aiinty.copayment.domain.usecase.auth.RefreshTokenUseCase
import com.aiinty.copayment.domain.usecase.auth.SignInUseCase
import com.aiinty.copayment.domain.usecase.auth.SignUpUseCase
import com.aiinty.copayment.domain.usecase.auth.UpdateUserUseCase
import com.aiinty.copayment.domain.usecase.auth.VerifyOTPUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AuthUseCaseModule {

    @Provides
    fun provideSignInUseCase(
        userRepository: UserRepository
    ): SignInUseCase {
        return SignInUseCase(userRepository)
    }

    @Provides
    fun provideRefreshTokenUseCase(
        userRepository: UserRepository
    ): RefreshTokenUseCase {
        return RefreshTokenUseCase(userRepository)
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
