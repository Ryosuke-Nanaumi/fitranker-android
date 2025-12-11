package com.example.fitranker.ui.personal.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitranker.ui.personal.viewModel.AddTrainingUiState
import com.example.fitranker.ui.personal.viewModel.Exercise
import java.time.LocalDate

@Composable
fun AddTrainingSheetContent(
    state: AddTrainingUiState,
    onWeightChanged: (String) -> Unit,
    onRepsChanged: (String) -> Unit,
    onExerciseSelected: (Exercise) -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    onSaveClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "トレーニング記録追加",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "日付",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                DateSelector(
                    date = state.date,
                    onDateSelected = onDateSelected
                )
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "種目",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                ExerciseSelector(
                    selected = state.exercise,
                    onSelected = onExerciseSelected
                )
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "重量",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                    TrainingInput(
                        value = state.weight,
                        label = "重量(kg)",
                        onValueChange = onWeightChanged
                    )
                }
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "回数",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                    TrainingInput(
                        value = state.reps,
                        label = "回数(回)",
                        onValueChange = onRepsChanged,
                    )
                }
            }
        }

        item {
            Button(
                onClick = onSaveClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF27F94B)
                )
            ) {
                Text(
                    text = "保存",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }

        item {
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }
}

@Composable
fun ExerciseSelector(
    selected: Exercise,
    onSelected: (Exercise) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Exercise.entries.forEach { exercise ->
            val isSelected = exercise == selected
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isSelected) Color(0xFF27F94B)
                        else Color(0xFF2A2A2A)
                    )
                    .clickable { onSelected(exercise) }
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text(
                    text = exercise.label,
                    color = if (isSelected) Color.Black else Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun TrainingInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = { input ->
            if (input.all { it.isDigit() }) onValueChange(input)
        },
        label = { Text(label) },
        singleLine = true,
        shape = RoundedCornerShape(32.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.Gray,
            focusedBorderColor = Color(0xFF27F94B),
            focusedLabelColor = Color(0xFF27F94B),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
        )
    )
}

@Composable
fun DateSelector(
    date: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF2A2A2A))
            .clickable {
                onDateSelected(date.plusDays(1))
            }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = date.toString(),
            color = Color.White,
        )
    }
}