package com.example.fitranker.ui.personal.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitranker.data.repository.TrainingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TrainingUiModel(
    val name: String,
    val detail: String,
    val point: Int
)

data class HomeUiState(
    val userName: String = "",
    val totalPoints: Int = 0,
    val todaysPoint: Int = 0,
//    val trainings: List<TrainingUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class HomeViewModel(private val repository: TrainingRepository = TrainingRepository()) :
    ViewModel() {
    private var _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState

    fun load(id: Int) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                val res = repository.getPersonalInfo(id)

                _uiState.update {
                    it.copy(
                        userName = res.name,
                        totalPoints = res.totalPoint,
                        todaysPoint = res.todaysPoint,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "personalUserInfoの取得に失敗しました: $e"
                )
            }
        }
    }
}