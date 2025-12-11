package com.example.fitranker.ui.personal.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.fitranker.ui.personal.viewModel.HistoryViewModel

@Composable
fun HistoryRoute(
    userId: Int,
    onBack: () -> Unit
) {
    val viewModel: HistoryViewModel = hiltViewModel<HistoryViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(userId) {
        viewModel.load(userId)
    }

    FitRankerHistoryView(
        uiState = uiState,
        onBack = onBack
    )
}

@Composable
fun FitRankerHistoryView(
    uiState: HistoryViewModel.HistoryUiState,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF07190A))
            .systemBarsPadding()
    ) {
        HistoryHeader(onBack = onBack)
        when {
            uiState.isLoading -> {
                LoadingIndicator()
            }

            uiState.errorMessage != null -> {
                ErrorView(errorMessage = uiState.errorMessage)
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.records) { record ->
                        TrainingItemHistoryCard(record)
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryHeader(onBack: () -> Unit) {
    var enabled by remember { mutableStateOf(true) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.KeyboardArrowLeft,
            contentDescription = "",
            tint = Color.White,
            modifier = Modifier
                .size(28.dp)
                .clickable(enabled = enabled) {
                    enabled = false
                    onBack()
                }
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = "トレーニング履歴",
            color = Color.White,
            fontSize = 22.sp
        )
    }
}


@Composable
fun TrainingItemHistoryCard(
    record: HistoryViewModel.TrainingHistoryRecord
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF132815))
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            TrainingIcon(iconRes = record.label)

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = record.name,
                    color = Color.White,
                    fontSize = 20.sp,
                )

                Text(
                    text = record.date,
                    color = Color(0xFFB0B0B0),
                    fontSize = 14.sp
                )
            }
        }

        Text(
            text = "+${record.point} pt",
            color = Color(0xFF38FF14),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}