package com.example.fitranker.ui.personal.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.fitranker.ui.personal.viewModel.HistoryViewModel

@Composable fun HistoryRoute(
    userId: Int,
) {
    val viewModel: HistoryViewModel = hiltViewModel<HistoryViewModel>()
}

@Composable
fun FitRankerHistoryView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Text("History Screen", color = Color.White)
    }
}
