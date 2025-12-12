package com.example.fitranker.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitranker.data.local.TokenStorage
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

data class LoginUiState(
    val email: String = "",
    val password: String = ""
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val tokenStorage: TokenStorage
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun updateEmail(value: String) {
        _uiState.value = _uiState.value.copy(email = value)
    }

    fun updatePassword(value: String) {
        _uiState.value = _uiState.value.copy(password = value)
    }

    fun login(onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val auth = FirebaseAuth.getInstance()
                val result = auth.signInWithEmailAndPassword(
                    _uiState.value.email,
                    _uiState.value.password
                ).await()

                val user = result.user ?: return@launch
                val token = user.getIdToken(true).await().token

                tokenStorage.saveToken(token)

                onSuccess()

            } catch (e: Exception) {
                println("Login failed: $e")
            }
        }
    }
}
