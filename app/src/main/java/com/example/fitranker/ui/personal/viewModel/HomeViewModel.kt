package com.example.fitranker.ui.personal.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitranker.R
import com.example.fitranker.data.remote.TrainingRecordInfo
import com.example.fitranker.data.remote.TrainingRecordRequest
import com.example.fitranker.data.repository.TrainingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.OffsetDateTime
import javax.inject.Inject

data class TrainingRecord(
    // 今はidは使わないかも
    val id: Int,
    val label: Int,
    val name: String,
    val amount: Int,
    val point: Int
)

data class HomeUiState(
    val userName: String = "",
    val totalPoints: Int = 0,
    val todaysPoint: Int = 0,
    val trainings: List<TrainingRecord> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

// TODO: ここに状態を持たせないようにする(dbに寄せる)
enum class Exercise(val id: Int, val label: String, val resId: Int) {
    BENCH_PRESS(id = 1, label = "ベンチプレス", resId = R.drawable.icon_kintore),
    SQUAT(id = 2, label = "スクワット", resId = R.drawable.icon_kintore),
    RUNNING(id = 3, label = "ランニング", resId = R.drawable.icon_warking),
}

data class AddTrainingUiState(
    val weight: String = "",
    val reps: String = "",
    val exercise: Exercise = Exercise.BENCH_PRESS,
    val date: LocalDate = LocalDate.now(),
    val isShow: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: TrainingRepository) :
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
                val personalUserInfoDeferred = async { repository.getPersonalInfo(id) }
                val recordsDeferred = async { repository.getTrainingRecords(id) }

                val personalUserInfo = personalUserInfoDeferred.await()
                val recordsInfo = recordsDeferred.await()

                val records = generateTodaysTrainingRecords(recordsInfo)

                _uiState.update {
                    it.copy(
                        userName = personalUserInfo.name,
                        totalPoints = personalUserInfo.totalPoint,
                        todaysPoint = personalUserInfo.todaysPoint,
                        trainings = records,
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

    private fun generateTodaysTrainingRecords(trainingRecordsInfo: List<TrainingRecordInfo>): List<TrainingRecord> {
        val today = LocalDate.now()
        return trainingRecordsInfo.filter { trainingRecordInfo ->
            OffsetDateTime.parse(trainingRecordInfo.date).toLocalDate() == today
        }.map { info ->
            TrainingRecord(
                id = info.trainingId,
                label = generateTrainingIcon(info.exerciseId),
                name = generateTrainingName(info.exerciseId),
                amount = info.amount,
                point = info.point
            )
        }
    }

    // 後からexerciseIdの渡し方考えた方が綺麗になるかも
    // 今だと、DBとフロント両方に状態を持ってしまっている
    private fun generateTrainingIcon(exerciseId: Int): Int {
        return Exercise.entries.find { it.id == exerciseId }?.resId ?: R.drawable.icon_kintore
    }

    private fun generateTrainingName(exerciseId: Int): String {
        return Exercise.entries.find { it.id == exerciseId }?.label ?: "不明"
    }
}