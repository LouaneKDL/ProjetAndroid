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
import com.example.parkour.model.Performances
import com.example.parkour.viewModel.CompetitorsViewModel
import com.example.parkour.viewModel.CoursesViewModel
import com.example.parkour.viewModel.PerformancesViewModel
import kotlinx.coroutines.delay
import java.time.LocalDate

/**
 * Composable function to display and manage obstacles for a specific competitor's performance.
 *
 * @param modifier Modifier for styling and layout.
 * @param competitorViewModel ViewModel for managing competitor data.
 * @param coursesViewModel ViewModel for managing course data.
 * @param performanceViewModel ViewModel for managing performance data.
 * @param navController Navigation controller for navigating between screens.
 * @param idCompetitor The ID of the competitor.
 * @param idCourse The ID of the course.
 * @param idPerformances The ID of the performance.
 */
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("ResourceType")
@Composable
fun Obstacles(
    modifier: Modifier = Modifier,
    competitorViewModel: CompetitorsViewModel,
    coursesViewModel: CoursesViewModel,
    performanceViewModel: PerformancesViewModel,
    navController: NavController,
    idCompetitor: Int?,
    idCourse: Int?,
    idPerformances: Int?
) {
    // State variables for managing the timer
    var time by remember { mutableStateOf(0L) }
    var isTimerRunning by remember { mutableStateOf(false) }

    // LaunchedEffect to run the timer when isTimerRunning is true
    LaunchedEffect(isTimerRunning) {
        while (isTimerRunning) {
            val startTime = System.currentTimeMillis()
            delay(10)
            val elapsedTime = System.currentTimeMillis() - startTime
            time += elapsedTime
        }
    }

    // Observe the list of obstacles for the course
    val obstacles by coursesViewModel.obstacles.observeAsState(emptyList())
    if (idCourse != null) {
        LaunchedEffect(idCourse) {
            coursesViewModel.getObstaclesByCourseId(idCourse)
        }
    }

    // Observe the competitor details
    val competitor by competitorViewModel.competitor.observeAsState()
    if (idCompetitor != null) {
        LaunchedEffect(idCompetitor) {
            competitorViewModel.getCompetitorById(idCompetitor)
        }
    }

    // Observe the course details
    val course by coursesViewModel.course.observeAsState()
    if (idCourse != null) {
        LaunchedEffect(idCourse) {
            coursesViewModel.getCourseById(idCourse)
        }
    }

    // Observe the performance details
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

        // Button to start or stop the timer
        Button(
            onClick = {
                isTimerRunning = !isTimerRunning

                if (performance == null) {
                    // Create a new performance if none exists
                    val idPerformances = 9999 // /!\ ID to auto increment
                    val emptyPerformance = Performances(
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

                if (!isTimerRunning) {
                    // Update the performance with the elapsed time
                    val updatedPerformance = performance?.let {
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

                    updatedPerformance?.let {
                        performanceViewModel.updatePerformance(
                            id = it.id,
                            updatedPerformances = it
                        )
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(Color(0xFF6200EE)),
            modifier = Modifier.padding(16.dp)
        ) {
            Text(if (isTimerRunning) "Arrêter le parcours" else "Démarrer le parcours", color = Color.White)
        }

        // Display the total elapsed time
        val minutes = (time / 1000) / 60
        val seconds = (time / 1000) % 60
        val milliseconds = time % 1000

        Text(
            text = String.format("Temps total : %02d:%02d.%03d", minutes, seconds, milliseconds),
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(16.dp)
        )

        // List of obstacles with their details
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
                        Text("Obstacle: ${obstacle.obstacle_name}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("Temps sur l'obstacle - 00:00", fontSize = 16.sp)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = false, onCheckedChange = {})
                            Text("Chute", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(20.dp))
                            Checkbox(checked = false, onCheckedChange = {})
                            Text("À vérifier", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(20.dp))
                            Checkbox(checked = false, onCheckedChange = {})
                            Text("Complété", fontSize = 16.sp)
                        }
                    }
                }
            }
        }

        // Button to navigate to available obstacles
        Button(
            onClick = { /* Obtenir obstacles disponibles */ },
            colors = ButtonDefaults.buttonColors(Color(0xFF6200EE)),
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Obstacles disponibles", color = Color.White)
        }
    }
}
