package com.example.fitrunker.ui.personal.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun FitRunkerHomeView() {
    val background = Color(0xFF07190A)
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = background,
    ) { innerPadding ->

    }
}