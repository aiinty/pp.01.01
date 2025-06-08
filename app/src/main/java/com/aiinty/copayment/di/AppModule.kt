package com.aiinty.copayment.di

import android.content.Context
import com.aiinty.copayment.data.local.UserPreferences
import com.aiinty.copayment.data.network.RetrofitInstance
import com.aiinty.copayment.data.network.SupabaseApi
import com.aiinty.copayment.data.repository.UserRepositoryImpl
import com.aiinty.copayment.domain.repository.UserRepository
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
    fun provideApi(): SupabaseApi {
        return RetrofitInstance.api
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        api: SupabaseApi,
        userPrefs: UserPreferences
    ): UserRepository {
        return UserRepositoryImpl(api, userPrefs)
    }
}