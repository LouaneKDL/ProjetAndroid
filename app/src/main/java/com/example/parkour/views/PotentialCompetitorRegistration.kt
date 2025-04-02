package com.example.parkour.views

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.parkour.R
import com.example.parkour.Routes
import com.example.parkour.api.ParkourApi
import com.example.parkour.viewModel.CompetitionViewModel
import com.example.parkour.viewModel.CompetitorsViewModel


import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

/**
 * Composable function to register potential competitors for a competition.
 *
 * @param modifier Modifier for styling and layout.
 * @param competitorsViewModel ViewModel for managing competitor data.
 * @param navController Navigation controller for navigating between screens.
 * @param competitionsViewModel ViewModel for managing competition data.
 * @param competitionID The ID of the competition for which competitors are being registered.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PotentialCompetitorRegistration(
    modifier: Modifier = Modifier,
    competitorsViewModel: CompetitorsViewModel,
    navController: NavController,
    competitionsViewModel: CompetitionViewModel,
    competitionID: Int
) {
    // Observe the list of competitors and registered competitors
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
            .background(Color.LightGray),
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
                onClick = {
                    navController.navigate(Routes.competitionView)
                }, colors = ButtonColors(
                    Color.Black,
                    contentColor = Color.White,
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.White
                )
            ) {
                Text(
                    text = "Retour"
                )
            }
        }

        Text(
            text = "Concurrents",
            modifier = modifier.padding(10.dp),
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold
        )
        Button(
            enabled = constructionMode,
            onClick = {
                navController.navigate("competition_registration_view")

            }, colors = ButtonColors(
                Color.Black,
                contentColor = Color.White,
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.White
            )
        ) {
            Text(
                text = "Ajouter un concurrent"
            )
        }
        Button(
            onClick = {
                navController.navigate("parkour_view/${competitionID}")

            }, colors = ButtonColors(
                Color.Black,
                contentColor = Color.White,
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.White
            )
        ) {
            Text(
                text = "Voir les parcours de cette compétition"
            )
        }

        Column(
            modifier = modifier
                .clip(shape = RoundedCornerShape(8.dp))
                .background(Color.White)
                .padding(8.dp)

        ) {
            LazyColumn {
                for (competitor in competitors) {
                    item {
                        LazyRow(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .width(300.dp)
                                .border(width = 1.dp, color = Color.Black)
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            item {
                                Column {
                                    Text(
                                        text = "->  " + competitor.last_name + " " + competitor.first_name,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "-> " + competitor.gender, fontSize = 13.sp
                                    )
                                    Text(
                                        text = "-> " + competitor.born_at, fontSize = 13.sp
                                    )
                                }
                            }
                            item {
                                Column {
                                    if (competitor.id in registeredCompetitorIDs) {
                                        Text(
                                            text = "Inscrit",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Green
                                        )
                                    } else {
                                        Button(
                                            onClick = {
                                                val format = "yyyy-MM-dd"
                                                val formater = DateTimeFormatter.ofPattern(format)
                                                val birthDate = LocalDate.parse(competitor.born_at, formater)
                                                val currentDate = LocalDate.now()
                                                val age = Period.between(birthDate, currentDate).years

                                                competition?.let {
                                                    var errorMessages = mutableListOf<String>()
                                                    if (age < it.age_min || age > it.age_max) {
                                                        errorMessages.add("Âge non valide (${age} ans) requis : ${it.age_min} - ${it.age_max}")
                                                    }
                                                    if (competitor.gender != it.gender) {
                                                        errorMessages.add("Genre non valide (${competitor.gender}) requis :${it.gender}")
                                                    }
                                                    if (errorMessages.isEmpty()) {
                                                        competitionsViewModel.postCompetitorToCompetitionById(
                                                            competitionID,
                                                            ParkourApi.CompetitourRequest(competitor.id)
                                                        )
                                                    } else {
                                                        Toast.makeText(
                                                            context,
                                                            errorMessages.joinToString("\n"),
                                                            Toast.LENGTH_LONG
                                                        ).show()
                                                    }
                                                }
                                            }, colors = ButtonColors(
                                                Color.Black,
                                                contentColor = Color.White,
                                                disabledContainerColor = Color.Gray,
                                                disabledContentColor = Color.White
                                            )
                                        ) {
                                            Text(
                                                text = "Inscrire",
                                            )
                                        }


                                    }
                                    Button(
                                        enabled = constructionMode,
                                        onClick = {
                                            navController.navigate("modifier_competiteur/${competitor.id}")
                                        }, colors = ButtonColors(
                                            Color.Black,
                                            contentColor = Color.White,
                                            disabledContainerColor = Color.Gray,
                                            disabledContentColor = Color.White
                                        )
                                    ) {
                                        Text(
                                            text = "Modifier Competiteur",
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
}
