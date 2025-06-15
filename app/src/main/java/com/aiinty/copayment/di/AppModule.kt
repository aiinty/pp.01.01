package com.aiinty.copayment.di

import android.content.Context
import com.aiinty.copayment.data.local.UserPreferences
import com.aiinty.copayment.data.network.RetrofitInstance
import com.aiinty.copayment.data.network.AuthApi
import com.aiinty.copayment.data.network.AvatarApi
import com.aiinty.copayment.data.network.CardApi
import com.aiinty.copayment.data.network.ProfileApi
import com.aiinty.copayment.data.repository.AvatarRepositoryImpl
import com.aiinty.copayment.data.repository.ProfileRepositoryImpl
import com.aiinty.copayment.data.repository.UserRepositoryImpl
import com.aiinty.copayment.domain.repository.AvatarRepository
import com.aiinty.copayment.domain.repository.ProfileRepository
import com.aiinty.copayment.domain.repository.UserRepository
import com.aiinty.copayment.presentation.common.ErrorHandler
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideUserPreferences(@ApplicationContext context: Context): UserPreferences {
        return UserPreferences(context)
    }

    @Provides
    fun provideAuthApi(): AuthApi {
        return RetrofitInstance.authApi
    }

    @Provides
    fun provideCardApi(): CardApi {
        return RetrofitInstance.cardApi
    }

    @Provides
    fun provideProfileApi(): ProfileApi {
        return RetrofitInstance.profileApi
    }

    @Provides
    fun provideAvatarApi(): AvatarApi {
        return RetrofitInstance.avatarApi
    }

    @Provides
    fun provideErrorHandler(
        userPrefs: UserPreferences
    ): ErrorHandler {
        return ErrorHandler(userPrefs)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .create()
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        api: AuthApi,
        gson: Gson,
        userPrefs: UserPreferences
    ): UserRepository {
        return UserRepositoryImpl(api, gson, userPrefs)
    }

    @Provides
    @Singleton
    fun provideProfileRepository(
        api: ProfileApi,
        gson: Gson,
        userPrefs: UserPreferences
    ): ProfileRepository {
        return ProfileRepositoryImpl(api, gson, userPrefs)
    }

    @Provides
    @Singleton
    fun provideAvatarRepository(
        api: AvatarApi,
        gson: Gson,
        userPrefs: UserPreferences,
    ): AvatarRepository {
        return AvatarRepositoryImpl(api, gson, userPrefs)
    }
}