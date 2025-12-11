package com.example.fitranker.ui.personal.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitranker.R
import com.example.fitranker.data.repository.TrainingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: TrainingRepository
) : ViewModel() {
    private var uid: Int? = null
    data class HistoryUiState(
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
        val records: List<TrainingHistoryRecord> = emptyList()
    )

    data class TrainingHistoryRecord(
        val id: Int,
        val label: Int,
        val name: String,
        val amount: Int,
        val point: Int,
        val date: String
    )

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState

    fun load(userId: Int) {
        uid = userId
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }

                val response = repository.getTrainingRecords(userId)

                val mapped = response.map {
                    TrainingHistoryRecord(
                        id = it.trainingId,
                        label = generateTrainingIcon(it.exerciseId),
                        name = generateTrainingName(it.exerciseId),
                        amount = it.amount,
                        point = it.point,
                        date = generateDate(it.date)
                    )
                }

                _uiState.update {
                    it.copy(
                        records = mapped,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message
                    )
                }
            }
        }
    }

    fun deleteTraining(trainingId: Int) {
        if (uid == null) return
        viewModelScope.launch {
            try {
                repository.deleteTrainingRecord(trainingId)
                load(uid!!)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "削除に失敗しました: ${e.message}")
                }
            }
        }
    }

    private fun generateTrainingIcon(exerciseId: Int): Int {
        return Exercise.entries.find { it.id == exerciseId }?.resId ?: R.drawable.icon_kintore
    }

    private fun generateTrainingName(exerciseId: Int): String {
        return Exercise.entries.find { it.id == exerciseId }?.label ?: "不明"
    }

    private fun generateDate(date: String): String {
        return date.split("T").first()
    }
}
