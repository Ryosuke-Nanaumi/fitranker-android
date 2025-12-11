package com.example.fitranker.ui.personal.viewModel

import androidx.lifecycle.ViewModel
import com.example.fitranker.data.repository.TrainingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: TrainingRepository
): ViewModel() {
}