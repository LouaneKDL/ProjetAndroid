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
import com.example.parkour.viewModel.PerformancesViewModel
import java.util.concurrent.TimeUnit

@Composable
fun ParkourClassificationView(
    modifier: Modifier = Modifier,
    parkourId: Int,
    competitionID: Int,
    competitionViewModel: CompetitionViewModel,
    performancesViewModel: PerformancesViewModel
) {
    val registeredCompetitors by competitionViewModel.competitors.observeAsState(emptyList())
    LaunchedEffect(competitionID) {
        competitionViewModel.getInscriptionsByCompetitionId(competitionID)
    }

    val performances by performancesViewModel.performances.observeAsState(emptyList())
    LaunchedEffect(Unit) {
        performancesViewModel.getData()
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Classement",
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Liste des concurrents classés
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                competitorsWithPerformance.withIndex().toList()
            ) { (index, pair) -> // pair correspond to the competitor with the performance
                CompetitorItem(rank = index + 1, competitor = pair.first, performance = pair.second)
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun CompetitorItem(rank: Int, competitor: Competitors, performance: Performances?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
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

            // Nom du concurrent
            Text(
                text = competitor.first_name.uppercase() + " " + competitor.last_name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            // Récupération du temps total en millisecondes
            val millis = (performance?.total_time?.toLong()) ?: 0L

            // Conversion en heures, minutes et secondes
            val hours = TimeUnit.MILLISECONDS.toHours(millis)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
            val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60

            // Formatage du temps en HH:mm:ss
            val formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds)

            // Vérification du score (évite les erreurs si performance est null)
            val scoreText = if (performance?.total_time != null && performance.total_time > 0) {
                "Score : %.1f s".format(millis / 1000.0) // Arrondi avec 1 décimale
            } else {
                "Score : disqualifié"
            }

            // Affichage dans Compose
            Text(
                text = "$scoreText\nTemps : $formattedTime",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
