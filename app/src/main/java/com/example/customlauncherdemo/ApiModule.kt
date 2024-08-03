package com.example.customlauncherdemo

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Provides
    fun provideApi(): ApiService {
        return ApiService()
    }
}