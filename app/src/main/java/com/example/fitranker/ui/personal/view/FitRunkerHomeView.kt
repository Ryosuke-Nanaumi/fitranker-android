package com.example.fitranker.ui.personal.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.fitranker.R
import com.example.fitranker.ui.navigation.History
import com.example.fitranker.ui.navigation.Home
import com.example.fitranker.ui.personal.viewModel.HomeUiState
import com.example.fitranker.ui.personal.viewModel.HomeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitRankerRoute() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Home) {
        composable<Home> {
            val viewModel: HomeViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()
            val addState by viewModel.addTrainingUiState.collectAsState()
            val showTrainingSheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true
            )
            val scope = rememberCoroutineScope()

            if (addState.isShow) {
                ModalBottomSheet(
                    onDismissRequest = {
                        scope.launch { showTrainingSheetState.hide() }.invokeOnCompletion {
                            viewModel.hideTrainingSheet()
                        }
                    },
                    containerColor = Color(0xFF113616),
                    sheetState = showTrainingSheetState
                ) {
                    AddTrainingSheetContent(
                        state = addState,
                        onWeightChanged = { newWeight ->
                            viewModel.updateAddTraining { it.copy(weight = newWeight) }
                        },
                        onRepsChanged = { newReps ->
                            viewModel.updateAddTraining { it.copy(reps = newReps) }
                        },
                        onExerciseSelected = { newExercise ->
                            viewModel.updateAddTraining { it.copy(exercise = newExercise) }
                        },
                        onDateSelected = { newDate ->
                            viewModel.updateAddTraining { it.copy(date = newDate) }
                        },
                        onSaveClick = viewModel::sheetSaveClicked
                    )
                }
            }

            LaunchedEffect(Unit) {
                viewModel.load(id = 1)
            }
            FitRankerHomeView(
                uiState = uiState,
                onAddTrainingClick = viewModel::showTrainingSheet,
                onHistoryClick = {
                    navController.navigate(History(userId = 1))
                }
            )
        }
        composable<History> { entry ->
            val args = entry.toRoute<History>()
            HistoryRoute(
                userId = args.userId
            )
        }
    }
}

@Composable
fun FitRankerHomeView(
    uiState: HomeUiState,
//    onRankingClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onAddTrainingClick: () -> Unit
) {
    if (uiState.isLoading) {
        LoadingIndicator()
        return
    }

    uiState.errorMessage?.let { error ->
        ErrorView(errorMessage = error)
        return
    }
    val background = Color(0xFF07190A)
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            HomeHeader(
                userName = "foo",
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding(),
            )
        },
        containerColor = background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTrainingClick,
                containerColor = Color(0xFF38FF14),
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add training"
                )
            }
        },
    ) { innerPadding ->
        HomeContent(uiState = uiState, modifier = Modifier.padding(innerPadding), onHistoryClick = onHistoryClick)
    }
}
@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF07190A)),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.material3.CircularProgressIndicator(
            color = Color(0xFF38FF14),
            strokeWidth = 5.dp
        )
    }
}

@Composable
fun ErrorView(errorMessage: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(errorMessage, color = Color.Red)
    }
}

@Composable
fun HomeHeader(
    userName: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                painter = painterResource(R.drawable.icon_user),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = userName,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "",
                tint = Color.White
            )
        }
    }
}

@Composable
fun HomeContent(uiState: HomeUiState, modifier: Modifier = Modifier, onHistoryClick: () -> Unit) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                PointCard(
                    title = "今日のポイント",
                    point = uiState.todaysPoint.toString(),
                    modifier = Modifier.weight(1f)
                )
                PointCard(
                    title = "累計ポイント",
                    point = uiState.totalPoints.toString(),
                    modifier = Modifier.weight(1f)
                )
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "今日のトレーニング(直近5件)",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                uiState.trainings.take(5).forEach { training ->
                    TrainingItemCard(
                        icon = training.label,
                        title = "${training.name}: ${training.amount}回",
                        point = training.point
                    )
                }
            }
        }

        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                HomeButton(
                    icon = R.drawable.icon_ranking,
                    title = "ランキングを見る",
                    modifier = Modifier.weight(1f),
                    onClick = onHistoryClick
                )
                HomeButton(
                    icon = R.drawable.icon_history,
                    title = "トレーニング履歴",
                    modifier = Modifier.weight(1f),
                    onClick = onHistoryClick
                )
            }
        }
    }
}

@Composable
fun PointCard(title: String, point: String, modifier: Modifier = Modifier) {
    val pointColor = if (title == "今日のポイント") {
        Color(0xFF38FF14)
    } else {
        Color.White
    }
    Column(
        modifier = modifier
            .height(140.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(color = Color(0xFF132815))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 20.sp,
        )
        Text(
            text = "$point pt",
            color = pointColor,
            fontSize = 35.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
fun TrainingItemCard(@DrawableRes icon: Int, title: String, point: Int) {
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
            TrainingIcon(
                iconRes = icon,
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                color = Color.White,
                fontSize = 20.sp
            )
        }

        Text(
            text = "+$point pt",
            color = Color(0xFF38FF14),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun HomeButton(@DrawableRes icon: Int, title: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .height(140.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(color = Color(0xFF132815))
            .clickable{ onClick() }
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TrainingIcon(iconRes = icon)
        Text(
            text = title,
            color = Color.White,
            fontSize = 16.sp,
        )
    }
}

@Composable
fun TrainingIcon(
    @DrawableRes iconRes: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(56.dp)
            .background(Color(0xFF1E471D), shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = Color(0xFF38FF14),
            modifier = Modifier.size(32.dp)
        )
    }
}