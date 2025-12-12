package com.example.fitranker.data.repository

import com.example.fitranker.data.local.TokenStorage
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val tokenStorage: TokenStorage
) {
    suspend fun signIn(email: String, password: String): Boolean {
        val user = FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, password)
            .await()
            .user ?: return false

        val token = user.getIdToken(true).await().token
        tokenStorage.saveToken(token)
        return true
    }

    suspend fun refreshToken(): String? {
        val user = FirebaseAuth.getInstance().currentUser ?: return null
        val newToken = user.getIdToken(true).await().token
        tokenStorage.saveToken(newToken)
        return newToken
    }

    suspend fun signOut() {
        FirebaseAuth.getInstance().signOut()
        tokenStorage.clearToken()
    }
}
