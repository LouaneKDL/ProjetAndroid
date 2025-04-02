package com.example.parkour.views

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.parkour.R
import com.example.parkour.viewModel.CompetitionViewModel

/**
 * Composable function to display a list of parkours for a specific competition.
 *
 * @param modifier Modifier for styling and layout.
 * @param viewModel ViewModel for managing competition data.
 * @param navController Navigation controller for navigating between screens.
 * @param idCompetition The ID of the competition.
 */
@SuppressLint("ResourceType")
@Composable
fun Parkour(
    modifier: Modifier = Modifier,
    viewModel: CompetitionViewModel,
    navController: NavController,
    idCompetition: Int?
) {

    val parkours by viewModel.courses.observeAsState(emptyList())
    val competition by viewModel.competition.observeAsState()

    if (idCompetition != null) {
        LaunchedEffect(idCompetition) {
            viewModel.getCoursesByCompetitionId(idCompetition)
            viewModel.getCompetitionById(idCompetition)
        }
    }

    var constructionMode by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ConstructionModeToggle(
            isEnabled = constructionMode,
            onToggle = { constructionMode = it }
        )

        Text(
            text = "Parkours de la compétition ${competition?.name}",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            onClick = { navController.navigate("modifier_competition/${idCompetition}") },
            enabled = constructionMode,
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Modifier la compétition")
        }

        Button(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            onClick = { navController.navigate("parkour_registration_view/${idCompetition}") },
            enabled = constructionMode,
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Ajouter un parkour")
        }

        LazyColumn(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .padding(8.dp)
        ) {
            for (parkour in parkours) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("➣ ${parkour.name}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Text("Durée maximale : ${parkour.max_duration / 60} min", fontSize = 14.sp)
                                Text("Position : ${parkour.position}", fontSize = 14.sp)
                                Text(
                                    text = if (parkour.is_over == 1) "Terminé" else "Non terminé",
                                    fontSize = 14.sp,
                                    color = if (parkour.is_over == 1) Color.Green else Color.Red
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                IconButton(onClick = { navController.navigate("competitor_view/${idCompetition}/${parkour.id}") }) {
                                    Image(
                                        imageVector = ImageVector.vectorResource(R.drawable.baseline_people_alt_24),
                                        contentDescription = "Compétiteurs",
                                        colorFilter = ColorFilter.tint(Color.Black)
                                    )
                                }

                                IconButton(onClick = { navController.navigate("obstacle_of_the_parkour_view/${parkour.id}") }) {
                                    Image(
                                        imageVector = ImageVector.vectorResource(R.drawable.baseline_info_24),
                                        contentDescription = "Obstacles"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
