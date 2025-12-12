package com.example.fitranker.data.remote

import kotlinx.coroutines.flow.StateFlow
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

// tokenStateFlowを作るときに、その依存関係はhiltが自動的に解決してくれる(今回はtokenStorageが入る)
class AuthInterceptor @Inject constructor(
    private val tokenStateFlow: StateFlow<String?>
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenStateFlow.value

        val newReq = chain.request().newBuilder().apply {
            if (!token.isNullOrBlank()) {
                header("Authorization", "Bearer $token")
            }
        }.build()

        return chain.proceed(newReq)
    }
}
