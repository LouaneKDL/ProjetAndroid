package com.example.parkour.views

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
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
import com.example.parkour.model.Performance_obstacles
import com.example.parkour.model.Performances
import com.example.parkour.viewModel.CompetitorsViewModel
import com.example.parkour.viewModel.CoursesViewModel
import com.example.parkour.viewModel.PerformanceObstaclesViewModel
import com.example.parkour.viewModel.PerformancesViewModel
import kotlinx.coroutines.delay
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("ResourceType")
@Composable
fun Obstacles(
    modifier: Modifier = Modifier,
    competitorViewModel: CompetitorsViewModel,
    coursesViewModel: CoursesViewModel,
    performanceViewModel: PerformancesViewModel,
    performanceObstaclesViewModel: PerformanceObstaclesViewModel,
    navController: NavController,
    idCompetitor: Int?,
    idCourse: Int?,
    idPerformances: Int?
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

    val course by coursesViewModel.course.observeAsState()
    if (idCourse != null) {
        LaunchedEffect(idCourse) {
            coursesViewModel.getCourseById(idCourse)
        }
    }

    val performance by performanceViewModel.performance.observeAsState()
    if (idPerformances != null) {
        LaunchedEffect(idPerformances) {
            performanceViewModel.getPerformanceById(idPerformances)
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
            text = "Performances de ${competitor?.first_name} ${competitor?.last_name} sur le parkour ${course?.name}",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        Button(
            onClick = {

                isTimerRunning = !isTimerRunning

                if (performance == null){
                    var idPerformances = 9999 // /!\ ID à auto incrémenter
                    var emptyPerformance: Performances = Performances(
                        id = idPerformances,
                        competitor_id = idCompetitor ?: -1,
                        course_id = idCourse ?: -1,
                        status = "to_finish",
                        total_time = 0,
                        created_at = LocalDate.now().toString(),
                        updated_at = ""
                    )
                    performanceViewModel.postPerformances(emptyPerformance)
                    performanceViewModel.getPerformanceById(idPerformances)
                }

                if (isTimerRunning == false){

                    var updatedPerformances: Performances? = performance?.let {
                        Performances(
                            id = it.id,
                            competitor_id = it.competitor_id,
                            course_id = it.course_id,
                            status = it.status,
                            total_time = time.toInt(),
                            created_at = it.created_at,
                            updated_at = LocalDate.now().toString()
                        )
                    }

                    if (updatedPerformances != null) {
                        performance?.let {
                            performanceViewModel.updatePerformance(
                                id = it.id,
                                updatedPerformances = updatedPerformances
                            )
                        }
                    }
                }

            },
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
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(obstacles.size) { index ->
                val obstacle = obstacles[index]

                val detailsPerformances by performanceObstaclesViewModel.performanceObstacles.observeAsState(emptyList())
                performanceObstaclesViewModel.getData()

                var detail: Performance_obstacles? = null
                for (detailsPerf in detailsPerformances){
                    if (detailsPerf.obstacle_id == obstacle.id){// && detailsPerf.performance_id == performance?.id){
                        detail = detailsPerf
                        break
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Obstacle: ${obstacle.name}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("Temps sur l'obstacle - 00:00", fontSize = 16.sp)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (detail != null) {
                                Checkbox(checked = detail.has_fell==1, onCheckedChange = {})
                            }
                            Text("Chute", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(20.dp))
                            Checkbox(checked = detail?.to_verify==1, onCheckedChange = {})
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
