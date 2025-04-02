package com.example.parkour.views

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableLongStateOf
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
import com.example.parkour.model.*
import com.example.parkour.viewModel.*
import kotlinx.coroutines.delay

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

@SuppressLint("UnrememberedMutableState")
@Composable
fun ObstaclesView(
    modifier: Modifier = Modifier,
    competitorViewModel: CompetitorsViewModel,
    coursesViewModel: CoursesViewModel,
    performanceViewModel: PerformancesViewModel,
    performanceObstaclesViewModel: PerformanceObstaclesViewModel,
    navController: NavController,
    idCompetitor: Int?,
    idCourse: Int?,
    idPerformance: Int?
) {
    val obstacles by coursesViewModel.obstacles.observeAsState(emptyList())
    val competitor by competitorViewModel.competitor.observeAsState()
    val course by coursesViewModel.course.observeAsState()
    val performance by performanceViewModel.performance.observeAsState()
    val performanceDetails by performanceViewModel.details.observeAsState(emptyList())


    var time by remember { mutableLongStateOf(0L) }
    var isTimerRunning by remember { mutableStateOf(false) }
    var currentPerformanceId by remember { mutableStateOf(idPerformance) }
    var isLoading by remember { mutableStateOf(true) }
    var obstacleStartTimes by remember { mutableStateOf<Map<Int, Long>>(emptyMap()) }
    val obstacleStates = remember { mutableStateMapOf<Int, ObstacleUiState>() }

    LaunchedEffect(performanceDetails) {
        performanceDetails.forEach { detail ->
            obstacleStates[detail.obstacle_id] = ObstacleUiState(
                time = detail.time,
                hasFell = detail.has_fell == 1,
                toVerify = detail.to_verify == 1,
                isCompleted = detail.time > 0
            )
        }
    }
    LaunchedEffect(idCourse) {
        idCourse?.let {
            coursesViewModel.getObstaclesByCourseId(it)
            coursesViewModel.getCourseById(it)
        }
    }

    LaunchedEffect(idCompetitor) {
        idCompetitor?.let {
            competitorViewModel.getCompetitorById(it)
        }
    }

    LaunchedEffect(idCompetitor, idCourse, performanceViewModel.performances.value) {
        if (currentPerformanceId == null && idCompetitor != null && idCourse != null) {
            performanceViewModel.postPerformances(
                PerformancesRequest(
                    competitor_id = idCompetitor,
                    course_id = idCourse,
                    status = "to_finish",
                    total_time = 0
                )
            )
            delay(500)
            performanceViewModel.getData()

            val newPerf = performanceViewModel.performances.value
                ?.firstOrNull { it.competitor_id == idCompetitor && it.course_id == idCourse }

            newPerf?.let {
                currentPerformanceId = it.id
                performanceViewModel.getPerformanceById(it.id)
            }
        } else if (currentPerformanceId != null) {
            performanceViewModel.getPerformanceById(currentPerformanceId!!)
        }
    }


    LaunchedEffect(performance, obstacles) {
        if (performance != null && obstacles.isNotEmpty()) {
            obstacles.forEach { obstacle ->
                if (performanceDetails.none { it.obstacle_id == obstacle.obstacle_id }) {
                    performanceObstaclesViewModel.postPerformanceObstacles(
                        Performance_obstaclesPost(
                            performance_id = performance!!.id,
                            obstacle_id = obstacle.obstacle_id,
                            has_fell = 0,
                            to_verify = 0,
                            time = 0
                        )
                    )
                    delay(100)
                }
            }
            performanceViewModel.getPerformanceDetailsById(performance!!.id)
            isLoading = false
        }
    }


    LaunchedEffect(isTimerRunning) {
        if (isTimerRunning) {
            val startTime = System.currentTimeMillis() - time
            while (isTimerRunning) {
                time = System.currentTimeMillis() - startTime
                delay(10)
            }
            performance?.let {
                performanceViewModel.updatePerformance(
                    it.id,
                    PerformancesRequest(
                        competitor_id = it.competitor_id,
                        course_id = it.course_id,
                        status = "finished",
                        total_time = (time / 10).toInt()
                    )
                )
            }
        }
    }

    LaunchedEffect(isTimerRunning, obstacleStartTimes) {
        if (isTimerRunning) {
            while (isTimerRunning) {
                obstacleStates.forEach { (obstacleId, state) ->
                    if (!state.isCompleted) {
                        obstacleStartTimes[obstacleId]?.let { startTime ->
                            val currentTime = ((System.currentTimeMillis() - startTime) / 10).toInt()
                            obstacleStates[obstacleId] = state.copy(time = currentTime)
                        }
                    }
                }
                delay(100)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            isLoading -> {
                LoadingView()
            }
            obstacles.isEmpty() -> {
                EmptyView(message = "Aucun obstacle trouvé pour ce parcours")
            }
            else -> {
                HeaderView(
                    competitor = competitor,
                    course = course,
                    time = time,
                    isTimerRunning = isTimerRunning,
                    onTimerToggle = {
                        isTimerRunning = !isTimerRunning
                        if (isTimerRunning) {
                            obstacleStartTimes = obstacles.associate { it.obstacle_id to System.currentTimeMillis() }
                        }
                    }
                )

                ObstaclesListView(
                    obstacles = obstacles,
                    obstacleStates = obstacleStates,
                    isTimerRunning = isTimerRunning,
                    onObstacleCompleted = { obstacleId ->
                        obstacleStartTimes[obstacleId]?.let { startTime ->
                            val finalTime = ((System.currentTimeMillis() - startTime) / 10).toInt()
                            val state = obstacleStates[obstacleId] ?: return@let

                            obstacleStates[obstacleId] = state.copy(
                                time = finalTime,
                                isCompleted = true
                            )

                            performanceDetails.find { it.obstacle_id == obstacleId }?.let { detail ->
                                performanceObstaclesViewModel.updatePerformanceObstacles(
                                    detail.id,
                                    Performance_obstaclesRequest(
                                        has_fell = if (state.hasFell) 1 else 0,
                                        to_verify = if (state.toVerify) 1 else 0,
                                        time = finalTime
                                    )
                                )
                            }
                        }
                    },
                    onFellChange = { obstacleId, hasFell ->
                        obstacleStates[obstacleId]?.let { state ->
                            obstacleStates[obstacleId] = state.copy(hasFell = hasFell)
                        }
                    },
                    onVerifyChange = { obstacleId, toVerify ->
                        obstacleStates[obstacleId]?.let { state ->
                            obstacleStates[obstacleId] = state.copy(toVerify = toVerify)
                        }
                    }
                )
            }
        }
    }
}

data class ObstacleUiState(
    val time: Int,
    val hasFell: Boolean,
    val toVerify: Boolean,
    val isCompleted: Boolean
)

@Composable
private fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text("Initialisation en cours...")
        }
    }
}

@Composable
private fun EmptyView(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(message)
    }
}

@Composable
private fun HeaderView(
    competitor: Competitors?,
    course: Courses?,
    time: Long,
    isTimerRunning: Boolean,
    onTimerToggle: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(bottom = 16.dp)
    ) {
        Text(
            text = "Performance de ${competitor?.first_name ?: ""} ${competitor?.last_name ?: ""}",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Parcours: ${course?.name ?: ""}",
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = formatTime(time),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Button(
            onClick = onTimerToggle,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isTimerRunning) Color.Red else Color(0xFF6200EE)
            ),
            modifier = Modifier.padding(8.dp)
        ) {
            Text(if (isTimerRunning) "Arrêter" else "Commencer")
        }
    }
}

@Composable
private fun ObstaclesListView(
    obstacles: List<ObstacleCourse>,
    obstacleStates: Map<Int, ObstacleUiState>,
    isTimerRunning: Boolean,
    onObstacleCompleted: (Int) -> Unit,
    onFellChange: (Int, Boolean) -> Unit,
    onVerifyChange: (Int, Boolean) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(obstacles) { obstacle ->
            val state = obstacleStates[obstacle.obstacle_id] ?: return@items

            ObstacleCard(
                obstacle = obstacle,
                time = state.time,
                hasFell = state.hasFell,
                toVerify = state.toVerify,
                isTimerRunning = isTimerRunning,
                isCompleted = state.isCompleted,
                onValidate = { onObstacleCompleted(obstacle.obstacle_id) },
                onFellChange = { onFellChange(obstacle.obstacle_id, it) },
                onVerifyChange = { onVerifyChange(obstacle.obstacle_id, it) }
            )
        }
    }
}

@Composable
private fun ObstacleCard(
    obstacle: ObstacleCourse,
    time: Int,
    hasFell: Boolean,
    toVerify: Boolean,
    isTimerRunning: Boolean,
    isCompleted: Boolean,
    onValidate: () -> Unit,
    onFellChange: (Boolean) -> Unit,
    onVerifyChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = obstacle.obstacle_name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Temps: ${formatTime(time.toLong() * 10)}",
                modifier = Modifier.padding(vertical = 8.dp)
            )

            if (!isCompleted && isTimerRunning) {
                Button(
                    onClick = onValidate,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Valider l'obstacle")
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Checkbox(
                    checked = hasFell,
                    onCheckedChange = onFellChange,
                    enabled = !isCompleted && isTimerRunning
                )
                Text("Chute")

                Spacer(modifier = Modifier.width(24.dp))

                Checkbox(
                    checked = toVerify,
                    onCheckedChange = onVerifyChange,
                    enabled = !isCompleted && isTimerRunning
                )
                Text("À vérifier")
            }
        }
    }
}

private fun formatTime(millis: Long): String {
    val totalSeconds = millis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    val centis = (millis % 1000) / 10
    return String.format("%02d:%02d.%02d", minutes, seconds, centis)
}