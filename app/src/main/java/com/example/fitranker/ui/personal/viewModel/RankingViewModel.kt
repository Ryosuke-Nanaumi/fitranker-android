package com.example.fitranker.ui.personal.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitranker.data.repository.TrainingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RankingViewModel @Inject constructor(
    private val repository: TrainingRepository
) : ViewModel() {
    data class RankingUiState(
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
        val rankings: List<DisplayedRanking> = emptyList()
    )
    data class DisplayedRanking(
        val name: String,
        val point: Int,
    )

    private val _uiState = MutableStateFlow(RankingUiState())
    val uiState: StateFlow<RankingUiState> = _uiState

    fun load() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }

                val response = repository.getRankingInfo()

                println("foo: $response")

                val mapped = response.map {
                    DisplayedRanking(
                        name = it.name,
                        point = it.point
                    )
                }

                _uiState.update {
                    it.copy(
                        rankings = mapped,
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
}
