package com.example.fitranker.ui.personal.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitranker.R

@Composable
fun FitRankerHomeView() {
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
    ) { innerPadding ->
        HomeContent(modifier = Modifier.padding(innerPadding))
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
fun HomeContent(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        PointCard(title = "今日のポイント", point = "10", modifier = Modifier.weight(1f))
        PointCard(title = "累計ポイント", point = "100", modifier = Modifier.weight(1f))
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
            .background(color = Color(0xFF1F1F1F))
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
