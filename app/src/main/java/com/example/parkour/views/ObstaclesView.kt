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
import com.example.parkour.model.Performance_obstaclesPost
import com.example.parkour.model.Performance_obstaclesRequest
import com.example.parkour.model.PerformancesRequest
import com.example.parkour.viewModel.CompetitorsViewModel
import com.example.parkour.viewModel.CoursesViewModel
import com.example.parkour.viewModel.PerformanceObstaclesViewModel
import com.example.parkour.viewModel.PerformancesViewModel
import kotlinx.coroutines.delay
import java.time.Instant
import java.util.Timer
import kotlin.concurrent.schedule

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("ResourceType", "DefaultLocale")
@Composable
fun Obstacles(
    modifier: Modifier = Modifier,
    competitorViewModel: CompetitorsViewModel,
    coursesViewModel: CoursesViewModel,
    performanceViewModel1: PerformancesViewModel,
    performanceViewModel2: PerformancesViewModel,
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

    val performance by performanceViewModel1.performance.observeAsState()
    val performances by performanceViewModel2.performances.observeAsState(emptyList())

    LaunchedEffect(idPerformances) {
        if (idPerformances != null) {
            performanceViewModel1.getPerformanceById(idPerformances)
        }
        if (idPerformances == null) { //si il n'y a pas de performance il faut en créer une
            val emptyPerformance: PerformancesRequest = PerformancesRequest(
                competitor_id = idCompetitor ?: -1,
                course_id = idCourse ?: -1,
                status = "to_finish",
                total_time = 0,
            )
            //on l'envoie
            performanceViewModel1.postPerformances(emptyPerformance)
            delay(500)
            //on veut récupérer son id, on utilise la date de creation pour avoir la derniere créer
            performanceViewModel2.getData()

            var idPerf = -1

            val mostRecentPerformance = performances.maxByOrNull {
                Instant.parse(it.created_at)
            }?.let {
                idPerf = it.id
            }
            if (idPerf != -1) {
                performanceViewModel1.getPerformanceById(idPerf)
                Log.i("AWAAAAAAAAAAA","OKKKKKKKKKKKKKKKKKKKKK ${idPerf}")

            }
            else{
                Log.i("AWAAAAAAAAAAA","NAAAAAAAAAAAAAAAAAAANN")
            }

        }

    }
    Log.i("ICI", "PERFORMANCE EST INITIALISE A : $performance")
    var time by remember { mutableLongStateOf(0L) }
    var isTimerRunning by remember { mutableStateOf(false) }
    var timerByObstacle by remember { mutableLongStateOf(0L) }

    LaunchedEffect(performance) {
        if (performance != null) {
            time = performance!!.total_time.times(10).toLong() ?: 0L
        }
    }

    var isDataLoaded by remember { mutableStateOf(false) }
    val detailsPerformances by performanceViewModel1.details.observeAsState(emptyList())
    LaunchedEffect(performance) {

        if (performance?.id != null) {
            performanceViewModel1.getPerformanceDetailsById(performance!!.id)
        }
        isDataLoaded = true
        Log.i("ICI", "CETTE FOIS CEST DETAILSPERFORMANCES INITIALISE A : $detailsPerformances")
    }

    LaunchedEffect(isTimerRunning) {
        while (isTimerRunning) {
            val startTime = System.currentTimeMillis()
            delay(10)
            val elapsedTime = System.currentTimeMillis() - startTime
            timerByObstacle += elapsedTime
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
                            performanceViewModel1.updatePerformance(
                                id = it.id,
                                updatedPerformances = updatedPerformances
                            )
                            //performanceViewModel1.getPerformanceById(it.id)
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

                LaunchedEffect(performance, detailsPerformances, obstacle) {
                    Log.i("OBSTACLE_DEBUG", "Vérification des détails pour obstacle ${obstacle.obstacle_name}")
                    if (performance != null) {
                        // Chercher si un détail existe déjà pour cet obstacle
                        detail = detailsPerformances.find { it.obstacle_id == obstacle.obstacle_id }

                        // Si pas de détail pour cet obstacle, en créer un
                        if (detail == null) {
                            Log.i("OBSTACLE_DEBUG", "Création détail pour obstacle ${obstacle.obstacle_name}")
                            val detailPerfObs: Performance_obstaclesPost = Performance_obstaclesPost(
                                has_fell = 0,
                                to_verify = 0,
                                time = 0,
                                obstacle_id = obstacle.obstacle_id,
                                performance_id = performance!!.id
                            )
                            performanceObstaclesViewModel.postPerformanceObstacles(detailPerfObs)

                            // Attendre un peu puis récupérer les détails mis à jour
                            delay(500)
                            performanceViewModel1.getPerformanceDetailsById(performance!!.id)

                            // Essayer de trouver le détail après la mise à jour
                            detail = detailsPerformances.find { it.obstacle_id == obstacle.obstacle_id }
                        }

                        isObstacleDataReady = detail != null
                    }
                }
//isObstacleDataReady
                Log.i("ICII", "ALORS LA CEST LE DETAIL ENCORE $detail")
                if (detail != null) {
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
                                var enable by remember { mutableStateOf(true) }
                                Button(
                                    enabled = enable,
                                    onClick = {
                                        if(isTimerRunning) {
                                            val updatedPerformance_obstacles: Performance_obstaclesRequest? =
                                                detail?.let {
                                                    Performance_obstaclesRequest(
                                                        to_verify = detail!!.to_verify,
                                                        time = (timerByObstacle / 10).toInt(),
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
                                            timerByObstacle = 0;
                                            enable=false
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
