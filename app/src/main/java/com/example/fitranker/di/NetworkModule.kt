package com.example.fitranker.di

import com.example.fitranker.data.remote.ApiClient
import com.example.fitranker.data.remote.TrainingApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideTrainingApi(): TrainingApi {
        return ApiClient.create(TrainingApi::class.java)
    }
}