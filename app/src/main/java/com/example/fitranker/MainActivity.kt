package com.example.fitranker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.fitranker.ui.personal.view.FitRankerApp
import com.example.fitranker.ui.personal.view.FitRankerHomeView
import com.example.fitranker.ui.personal.viewModel.HomeViewModel
import com.example.fitranker.ui.theme.FitRankerTheme

class MainActivity : ComponentActivity() {
    private val homeViewModel: HomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bg = Color(0xFF07190A)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(bg.toArgb()),
            navigationBarStyle = SystemBarStyle.dark(bg.toArgb())
        )
        setContent {
            FitRankerTheme {
                FitRankerApp(viewModel = homeViewModel)
            }
        }
    }
}