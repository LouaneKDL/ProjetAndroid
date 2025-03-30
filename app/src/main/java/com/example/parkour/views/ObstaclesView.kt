package com.example.parkour.views

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.parkour.viewModel.CompetitorsViewModel
import com.example.parkour.viewModel.CoursesViewModel
import kotlinx.coroutines.delay

@SuppressLint("ResourceType")
@Composable
fun Obstacles(
    modifier: Modifier = Modifier,
    competitorViewModel: CompetitorsViewModel,
    coursesViewModel: CoursesViewModel,
    navController: NavController,
    idCompetitor: Int?,
    idCourse: Int?
) {
    var time by remember { mutableStateOf(0L) }
    var isTimerRunning by remember { mutableStateOf(false) }

    LaunchedEffect(isTimerRunning) {
        while (isTimerRunning) {
            val startTime = System.currentTimeMillis()
            delay(10)
            val elapsedTime = System.currentTimeMillis() - startTime
            time += elapsedTime
        }
    }


    val obstacles by coursesViewModel.obstacles.observeAsState(emptyList())
    if (idCourse != null) {
        LaunchedEffect(idCourse) {
            coursesViewModel.getObstaclesByCourseId(idCourse)
        }
    }

    val competitor by competitorViewModel.competitor.observeAsState()
    if (idCompetitor != null) {
        LaunchedEffect(idCompetitor) {
            competitorViewModel.getCompetitorById(idCompetitor)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${competitor?.first_name ?: ""} ${competitor?.last_name ?: ""}",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        Button(
            onClick = { isTimerRunning = !isTimerRunning },
            colors = ButtonDefaults.buttonColors(Color(0xFF6200EE)),
            modifier = Modifier.padding(16.dp)
        ) {
            Text(if (isTimerRunning) "Arrêter le parcours" else "Démarrer le parcours", color = Color.White)
        }

        val minutes = (time / 1000) / 60
        val seconds = (time / 1000) % 60
        val milliseconds = time % 1000

        Text(
            text = String.format("Temps total : %02d:%02d.%03d", minutes, seconds, milliseconds),
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(obstacles.size) { index ->
                val obstacle = obstacles[index]
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Obstacle: ${obstacle.name}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("Temps sur l'obstacle : 00:00", fontSize = 16.sp)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = false, onCheckedChange = {})
                            Text("Chute", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(20.dp))
                            Checkbox(checked = false, onCheckedChange = {})
                            Text("À vérifier", fontSize = 16.sp)
                        }
                    }
                }
            }
        }

        Button(
            onClick = { /* Obtenir obstacles disponibles */ },
            colors = ButtonDefaults.buttonColors(Color(0xFF6200EE)),
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Obstacles disponibles", color = Color.White)
        }
    }
}
