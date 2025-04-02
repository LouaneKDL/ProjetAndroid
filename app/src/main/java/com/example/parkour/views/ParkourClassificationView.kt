package com.example.parkour.views

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parkour.model.Competitors
import com.example.parkour.model.Performances
import com.example.parkour.viewModel.CompetitionViewModel
import com.example.parkour.viewModel.CoursesViewModel
import com.example.parkour.viewModel.PerformancesViewModel
import java.util.concurrent.TimeUnit

@Composable
fun ParkourClassificationView(
    modifier: Modifier = Modifier,
    parkourId: Int,
    competitionID: Int,
    competitionViewModel: CompetitionViewModel,
    performancesViewModel: PerformancesViewModel,
    coursesViewModel: CoursesViewModel
) {
    val registeredCompetitors by competitionViewModel.competitors.observeAsState(emptyList())
    LaunchedEffect(competitionID) {
        competitionViewModel.getInscriptionsByCompetitionId(competitionID)
    }

    val performances by performancesViewModel.performances.observeAsState(emptyList())
    LaunchedEffect(Unit) {
        performancesViewModel.getData()
    }

    val course by coursesViewModel.course.observeAsState()
    LaunchedEffect(Unit) {
        coursesViewModel.getCourseById(parkourId)
    }


    // performancesViewModel.getPerformanceById()
    /*
    val competitorsWithPerformance = registeredCompetitors.map { competitor ->
        val performance = performances.find { it.competitor_id == competitor.id }
        competitor to (performance?.total_time ?: Long.MAX_VALUE)
    }.sortedBy { it.second }
     */


    val competitorsWithPerformance = registeredCompetitors.map { competitor ->
        val performance = performances.find { it.competitor_id == competitor.id }
        competitor to performance
    }.sortedBy { it.second?.total_time ?: Int.MAX_VALUE }

    val sortedCompetitors = competitorsWithPerformance
        .sortedWith(compareBy({ it.second?.status == "defected" }, { it.second?.total_time ?: Int.MAX_VALUE }))
        //.sortedWith(compareBy({ it.second?.status == "defected" }, { it.second?.status == "to_finish" }, { it.second?.status == "to_verify" }, { it.second?.total_time ?: Int.MAX_VALUE }))
        //.sortedWith(compareBy({ it.second?.status != "over" }, { it.second?.total_time ?: Int.MAX_VALUE }))

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Classement pour le parkour ${course?.name}",
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Liste des concurrents classés
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                sortedCompetitors.withIndex().toList()
            ) { (index, pair) -> // pair correspond to the competitor with the performance
                CompetitorItem(rank = index + 1, competitor = pair.first, performance = pair.second)
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun CompetitorItem(rank: Int, competitor: Competitors, performance: Performances?) {

    val backgroundColor = when (rank) {
        1 -> Color(0xFFFFD700)
        2 -> Color(0xFFC0C0C0)
        3 -> Color(0xFFCD7F32)
        else -> Color.White
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$rank.",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(30.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = competitor.first_name.uppercase() + " " + competitor.last_name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = when (performance?.status) {
                        "to_verify" -> "À Vérifier"
                        "to_finish" -> "À Finir"
                        "defected" -> "Disqualifié"
                        else -> "Terminé"
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    // color = Color.Gray
                )
            }

            val millis = (performance?.total_time?.toLong()) ?: 0L

            val hours = TimeUnit.MILLISECONDS.toHours(millis)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
            val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60

            val formattedTime = buildString {
                if (hours > 0) append("${hours}h ")
                if (minutes > 0 || hours > 0) append("${minutes}min ")
                append("${seconds}s")
            }.trim()

            val displayText = when {
                performance == null -> "Aucune donnée"
                performance.status == "defected" -> "Disqualifié"
                else -> formattedTime
            }

            Text(
                text = displayText,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

