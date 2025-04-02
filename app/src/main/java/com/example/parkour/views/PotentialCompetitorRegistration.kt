package com.example.parkour.views

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.parkour.Routes
import com.example.parkour.api.ParkourApi
import com.example.parkour.viewModel.CompetitionViewModel
import com.example.parkour.viewModel.CompetitorsViewModel
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PotentialCompetitorRegistration(
    modifier: Modifier = Modifier,
    competitorsViewModel: CompetitorsViewModel,
    navController: NavController,
    competitionsViewModel: CompetitionViewModel,
    competitionID: Int
) {
    val competitors by competitorsViewModel.competitors.observeAsState(emptyList())
    competitorsViewModel.getData()
    val registeredCompetitors by competitionsViewModel.competitors.observeAsState(emptyList())
    competitionsViewModel.getInscriptionsByCompetitionId(competitionID)
    val registeredCompetitorIDs = registeredCompetitors.map { it.id }.toSet()
    val competition by competitionsViewModel.competition.observeAsState()
    competitionsViewModel.getCompetitionById(competitionID)

    val context = LocalContext.current
    var constructionMode by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ConstructionModeToggle(
            isEnabled = constructionMode,
            onToggle = { constructionMode = it }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Button(
                onClick = { navController.navigate(Routes.competitionView) },
                colors = ButtonColors(
                    Color.Black,
                    contentColor = Color.White,
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.White
                )
            ) {
                Text(text = "Retour")
            }
        }

        Text(
            text = "Concurrents",
            modifier = modifier.padding(10.dp),
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )

        Button(
            enabled = constructionMode,
            onClick = { navController.navigate("competition_registration_view") },
            modifier = Modifier.padding(8.dp),
            colors = ButtonColors(
                Color.Black,
                contentColor = Color.White,
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.White
            )
        ) {
            Text(text = "Ajouter un concurrent")
        }

        Button(
            onClick = { navController.navigate("parkour_view/${competitionID}") },
            modifier = Modifier.padding(8.dp),
            colors = ButtonColors(
                Color.Black,
                contentColor = Color.White,
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.White
            )
        ) {
            Text(text = "Voir les parcours de cette compétition")
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.White, shape = RoundedCornerShape(12.dp))
                .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
                .padding(8.dp)
        ) {
            competitors.forEach { competitor ->
                item {
                    LazyRow(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                            .background(Color(0xFFF0F0F0))
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        item {
                            Column {
                                Text(
                                    text = "${competitor.last_name} ${competitor.first_name}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Text(text = "Genre: ${competitor.gender}", fontSize = 14.sp)
                                Text(text = "Né(e) le: ${competitor.born_at}", fontSize = 14.sp)
                            }
                        }
                        item {
                            Column {
                                if (competitor.id in registeredCompetitorIDs) {
                                    Text(
                                        text = "Inscrit",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Green
                                    )
                                } else {
                                    Button(
                                        onClick = {
                                            val format = "yyyy-MM-dd"
                                            val formatter = DateTimeFormatter.ofPattern(format)
                                            val birthDate = LocalDate.parse(competitor.born_at, formatter)
                                            val currentDate = LocalDate.now()
                                            val age = Period.between(birthDate, currentDate).years

                                            competition?.let {
                                                val errors = mutableListOf<String>()
                                                if (age !in it.age_min..it.age_max) {
                                                    errors.add("Âge non valide ($age ans) requis: ${it.age_min}-${it.age_max}")
                                                }
                                                if (competitor.gender != it.gender) {
                                                    errors.add("Genre non valide (${competitor.gender}), requis: ${it.gender}")
                                                }
                                                if (errors.isEmpty()) {
                                                    competitionsViewModel.postCompetitorToCompetitionById(
                                                        competitionID,
                                                        ParkourApi.CompetitourRequest(competitor.id)
                                                    )
                                                } else {
                                                    Toast.makeText(context, errors.joinToString("\n"), Toast.LENGTH_LONG).show()
                                                }
                                            }
                                        },
                                        colors = ButtonColors(
                                            Color.Black,
                                            contentColor = Color.White,
                                            disabledContainerColor = Color.Gray,
                                            disabledContentColor = Color.White
                                        )
                                    ) {
                                        Text("Inscrire")
                                    }
                                }
                                Button(
                                    enabled = constructionMode,
                                    onClick = { navController.navigate("modifier_competiteur/${competitor.id}") },
                                    colors = ButtonColors(
                                        Color.Black,
                                        contentColor = Color.White,
                                        disabledContainerColor = Color.Gray,
                                        disabledContentColor = Color.White
                                    )
                                ) {
                                    Text("Modifier Competiteur")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}