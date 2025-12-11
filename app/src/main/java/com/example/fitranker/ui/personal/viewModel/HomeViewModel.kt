package com.example.fitranker.ui.personal.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitranker.data.remote.TrainingRecordRequest
import com.example.fitranker.data.repository.TrainingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

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

enum class Exercise(val id: Int, val label: String) {
    BENCH_PRESS(id = 1, label = "ベンチプレス"),
    SQUAT(id = 2, label = "スクワット"),
    RUNNING(id = 3, label = "ランニング"),
}

data class AddTrainingUiState(
    val weight: String = "",
    val reps: String = "",
    val exercise: Exercise = Exercise.BENCH_PRESS,
    val date: LocalDate = LocalDate.now(),
    val isShow: Boolean = false,
    val errorMessage: String? = null
)

class HomeViewModel(private val repository: TrainingRepository = TrainingRepository()) :
    ViewModel() {
    private var _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState
    private var _addTrainingUiState = MutableStateFlow(AddTrainingUiState())
    val addTrainingUiState: StateFlow<AddTrainingUiState> = _addTrainingUiState
    private var userId: Int? = null

    fun load(id: Int) {
        userId = id
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

    fun showTrainingSheet() {
        _addTrainingUiState.update {
            it.copy(isShow = true)
        }
    }
    fun hideTrainingSheet() {
        _addTrainingUiState.update {
            it.copy(isShow = false)
        }
    }
    fun updateAddTraining(fnc: (AddTrainingUiState) -> AddTrainingUiState) {
        _addTrainingUiState.update(fnc)
    }
    fun sheetSaveClicked() {
        val current = _addTrainingUiState.value

        val weightInt = current.weight.toIntOrNull()
        val repsInt = current.reps.toIntOrNull()

        if (weightInt == null || repsInt == null) {
            _addTrainingUiState.update { it.copy(errorMessage = "入力された値が不正です") }
            return
        }

        val uid = userId ?: return
        val input = TrainingRecordRequest(
            userId = uid,
            exerciseId = current.exercise.id,
            date = current.date.toString(),
            amount = repsInt, // TODO ランニングにも対応させる, weightつける
        )
        viewModelScope.launch {
            try {
                repository.postTrainingRecord(input)
                hideTrainingSheet()
                load(uid)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "トレーニング記録の保存に失敗しました: $e")
                }
            }
        }
    }
}