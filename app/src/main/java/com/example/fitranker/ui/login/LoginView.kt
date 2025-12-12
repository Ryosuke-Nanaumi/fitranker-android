package com.example.fitranker.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun LoginView(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = state.email,
            onValueChange = { viewModel.updateEmail(it) },
            label = { Text("Email") }
        )
        OutlinedTextField(
            value = state.password,
            onValueChange = { viewModel.updatePassword(it) },
            label = { Text("Password") }
        )
        Button(onClick = { viewModel.login(onLoginSuccess) }) {
            Text("ログイン")
        }
    }
}
