package com.example.fitranker.di

import com.example.fitranker.data.local.TokenStorage
import com.example.fitranker.data.remote.ApiClient
import com.example.fitranker.data.remote.AuthInterceptor
import com.example.fitranker.data.remote.TrainingApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideTokenStateFlow(
        tokenStorage: TokenStorage
    ): StateFlow<String?> {
        return tokenStorage.getToken()
            .stateIn(
                scope = CoroutineScope(SupervisorJob() + Dispatchers.IO),
                started = SharingStarted.Eagerly,
                initialValue = null
            )
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(
        tokenStateFlow: StateFlow<String?>
    ): AuthInterceptor {
        return AuthInterceptor(tokenStateFlow)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(authInterceptor)   // ここで適用される
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit =
        ApiClient.createRetrofit(okHttpClient)

    @Provides
    @Singleton
    fun provideTrainingApi(
        retrofit: Retrofit
    ): TrainingApi =
        retrofit.create(TrainingApi::class.java)
}