package com.example.fitranker.ui.personal.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fitranker.R
import com.example.fitranker.ui.navigation.Home
import com.example.fitranker.ui.personal.viewModel.HomeUiState
import com.example.fitranker.ui.personal.viewModel.HomeViewModel

@Composable
fun FitRankerApp(viewModel: HomeViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Home) {
        composable<Home> {
            val uiState by viewModel.uiState.collectAsState()
            LaunchedEffect(Unit) {
                viewModel.load(id = 1)
            }
            FitRankerHomeView(uiState)
        }
    }

}

@Composable
fun FitRankerHomeView(
    uiState: HomeUiState,
//    onRankingClick: () -> Unit,
//    onHistoryClick: () -> Unit,
//    onAddTrainingClick: () -> Unit
) {
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
                onClick = {},
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
        HomeContent(uiState = uiState, modifier = Modifier.padding(innerPadding))
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
fun HomeContent(uiState: HomeUiState, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
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
        Column {
            Text(
                text = "今日のトレーニング",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 10.dp)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TrainingItemCard(
                    icon = R.drawable.icon_kintore,
                    title = "ベンチプレス - 3 x 10回",
                    point = 50
                )
                TrainingItemCard(
                    icon = R.drawable.icon_kintore,
                    title = "スクワット - 3 x 12回",
                    point = 60
                )
                TrainingItemCard(
                    icon = R.drawable.icon_warking,
                    title = "ランニング - 20分",
                    point = 40
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            HomeButton(
                icon = R.drawable.icon_ranking,
                title = "ランキングを見る",
                modifier = Modifier.weight(1f)
            )
            HomeButton(
                icon = R.drawable.icon_history,
                title = "トレーニング履歴",
                modifier = Modifier.weight(1f)
            )
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
fun HomeButton(@DrawableRes icon: Int, title: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .height(140.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(color = Color(0xFF132815))
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