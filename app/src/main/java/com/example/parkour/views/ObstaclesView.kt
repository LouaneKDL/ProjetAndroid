package com.example.parkour.views

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.example.parkour.model.Performance_obstacles
import com.example.parkour.model.Performance_obstaclesRequest
import com.example.parkour.model.PerformancesRequest
import com.example.parkour.viewModel.CompetitorsViewModel
import com.example.parkour.viewModel.CoursesViewModel
import com.example.parkour.viewModel.PerformanceObstaclesViewModel
import com.example.parkour.viewModel.PerformancesViewModel
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("ResourceType", "DefaultLocale")
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

    Log.i("ICIIII","DANS OBSTACLE VIEWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW ${idCompetitor}   ${idCourse}   ${idPerformances}   ${idPerformances}")

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

    LaunchedEffect(idPerformances) {
        if (idPerformances != null) {
            performanceViewModel.getPerformanceById(idPerformances)
        }
    }

    var time by remember { mutableLongStateOf(0L) }
    var isTimerRunning by remember { mutableStateOf(false) }

    LaunchedEffect(performance) {
        if (performance != null) {
            time = performance!!.total_time.times(10).toLong() ?: 0L
        }
    }

    var isDataLoaded by remember { mutableStateOf(false) }
    val detailsPerformances by performanceViewModel.details.observeAsState(emptyList())
    LaunchedEffect(idPerformances) {
        if (idPerformances != null) {
            performanceViewModel.getPerformanceDetailsById(idPerformances)
        }
        isDataLoaded = true
    }

    LaunchedEffect(isTimerRunning) {
        while (isTimerRunning) {
            val startTime = System.currentTimeMillis()
            delay(10)
            val elapsedTime = System.currentTimeMillis() - startTime
            time += elapsedTime
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

                if (performance == null) {
                    val emptyPerformance: PerformancesRequest = PerformancesRequest(
                        competitor_id = idCompetitor ?: -1,
                        course_id = idCourse ?: -1,
                        status = "to_finish",
                        total_time = 0,
                    )
                    performanceViewModel.postPerformances(emptyPerformance)
                    //performanceViewModel.getPerformanceById()
                }

                if (!isTimerRunning) {

                    val updatedPerformances: PerformancesRequest? = performance?.let {
                        PerformancesRequest(
                            competitor_id = it.competitor_id,
                            course_id = it.course_id,
                            status = it.status,
                            total_time = (time/100).toInt(),
                        )
                    }

                    if (updatedPerformances != null) {
                        performance?.let {
                            performanceViewModel.updatePerformance(
                                id = it.id,
                                updatedPerformances = updatedPerformances
                            )
                            performanceViewModel.getPerformanceById(it.id)
                        }
                    }
                }

            },
            colors = ButtonDefaults.buttonColors(Color(0xFF6200EE)),
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                if (isTimerRunning) "Arrêter le parcours" else "Démarrer le parcours",
                color = Color.White
            )
        }

        val minutes = (time / 1000) / 60
        val seconds = (time / 1000) % 60
        val milliseconds = time % 1000

        Text(
            text = String.format("Temps total : %02d:%02d.%01d", minutes, seconds, milliseconds),
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(16.dp)
        )





        LazyColumn(modifier = Modifier.fillMaxWidth()) {

            items(obstacles.size) { index ->
                val obstacle = obstacles[index]



                var detail by remember { mutableStateOf<Performance_obstacles?>(null) }

                var isObstacleDataReady by remember { mutableStateOf(false) }

                LaunchedEffect(performance, detailsPerformances, obstacle, isDataLoaded) {
                    if (performance != null && detailsPerformances.isNotEmpty()) {

                        for (detailsPerf in detailsPerformances){

                            if (detailsPerf.obstacle_id == obstacle.obstacle_id){
                                detail = detailsPerf
                                isObstacleDataReady = true
                            }
                        }

                    }
                }

                ///////////////////////////

                if (detail != null && isObstacleDataReady) {
                    var hasFellChecked by remember(detail?.id) {
                        mutableStateOf(detail?.has_fell == 1)
                    }
                    var toVerifyChecked by remember(detail?.id) {
                        mutableStateOf(detail?.to_verify == 1)
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        colors = CardDefaults.cardColors(Color.White),
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {

                            Text(
                                "Obstacle: ${obstacle.obstacle_name}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )

                            var timeObs = detail!!.time
                            var minuteObs = (timeObs / 100) / 60
                            var secondObs = (timeObs / 100) % 60
                            var millisObs = timeObs % 100
                            Text(String.format("Temps sur l'obstacle - %02d:%02d.%01d", minuteObs, secondObs, millisObs), fontSize = 16.sp)

                            if (detail!!.time == 0){
                                Button(
                                    onClick = {
                                        val updatedPerformance_obstacles: Performance_obstaclesRequest? = detail?.let {
                                            Performance_obstaclesRequest(
                                                to_verify = detail!!.to_verify,
                                                time = (detail!!.time/10).toInt(),
                                                has_fell = detail!!.has_fell,
                                            )
                                        }

                                        if (updatedPerformance_obstacles != null) {
                                            detail?.let {
                                                performanceObstaclesViewModel.updatePerformanceObstacles(
                                                    id = it.id,
                                                    updatedPerformances = updatedPerformance_obstacles
                                                )
                                            }
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(Color(0xFF6200EE)),
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text("Valider l'obstacle", color = Color.White)
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = hasFellChecked,
                                    onCheckedChange = { checked ->
                                        hasFellChecked = checked
                                        val has_fell = if (checked) 1 else 0
                                        val updatedPerformanceObstacles =
                                            Performance_obstaclesRequest(
                                                has_fell = has_fell,
                                                to_verify = detail!!.to_verify,
                                                time = detail!!.time
                                            )
                                        performanceObstaclesViewModel.updatePerformanceObstacles(
                                            detail!!.id,
                                            updatedPerformanceObstacles
                                        )
                                    }, enabled = detail!!.time == 0
                                )
                                Text("Chute", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(20.dp))

                                Checkbox(
                                    checked = toVerifyChecked,
                                    onCheckedChange = { checked ->
                                        toVerifyChecked = checked
                                        val verify = if (checked) 1 else 0
                                        val updatedPerformanceObstacles =
                                            Performance_obstaclesRequest(
                                                has_fell = detail!!.has_fell,
                                                to_verify = verify,
                                                time = detail!!.time
                                            )
                                        performanceObstaclesViewModel.updatePerformanceObstacles(
                                            detail!!.id,
                                            updatedPerformanceObstacles
                                        )
                                    }, enabled = detail!!.time == 0
                                )
                                Text("À vérifier", fontSize = 16.sp)
                            }
                        }
                    }

                } /*else {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        colors = CardDefaults.cardColors(Color.White),
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Chargement des données pour ${obstacle.obstacle_name}...",
                                fontSize = 16.sp
                            )
                            CircularProgressIndicator(
                                modifier = Modifier.padding(8.dp),
                                color = Color(0xFF6200EE)
                            )
                        }

                    }
                }*/
            }
            /* PAS ICI Button(
                onClick = { /* Obtenir obstacles disponibles */ },
                colors = ButtonDefaults.buttonColors(Color(0xFF6200EE)),
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Obstacles disponibles", color = Color.White)
            }*/
        }
    }
}
