package com.example.weathersdk.internal.di

import com.example.weathersdk.internal.ApiKeyManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object ApiKeyModule {

    @Singleton
    @Provides
    @ApiKey
    fun provideApiKey(): String? = ApiKeyManager.getInstance().apiKey
}

@Qualifier
internal annotation class ApiKey
