package com.example.parkour.views

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.example.parkour.Routes
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
    if (idCompetition != null) {
        LaunchedEffect(idCompetition) {
            viewModel.getCoursesByCompetitionId(idCompetition)
        }
    }

    val competition by viewModel.competition.observeAsState()
    if (idCompetition != null) {
        viewModel.getCompetitionById(idCompetition)
    }

    var constructionMode by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        ConstructionModeToggle(
            isEnabled = constructionMode,
            onToggle = { constructionMode = it }
        )
        Text(
            text = "Parkours de la compétition ${competition?.name}",
            modifier = modifier.padding(10.dp),
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold
        )

        Button(
            modifier = Modifier.padding(10.dp),
            onClick = {navController.navigate("modifier_competition/${idCompetition}")},
            colors = ButtonColors(
                Color.Black,
                contentColor = Color.White,
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.White
            ),
            enabled = constructionMode
        ){
            Text(
                text = "Modifier la compétition",
                modifier = Modifier,
                color = Color.White
            )
        }


        Button(
            modifier = Modifier.padding(10.dp),
            onClick = {navController.navigate("parkour_registration_view/${idCompetition}")},
            colors = ButtonColors(
                Color.Black,
                contentColor = Color.White,
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.White
            ),
            enabled = constructionMode
        ){
            Text(
                text = "Ajouter un parkour",
                modifier = Modifier,
                color = Color.White
            )
        }

        Column(
            modifier = modifier
                .clip(shape = RoundedCornerShape(8.dp))
                .background(Color.White)
                .padding(8.dp)

        ) {

            LazyColumn {
                for (parkour in parkours) {
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
                                        text = "➣  " + parkour.name,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(//((detailCompetitor.total_time*10).toLong()/1000)/60 + ":" + ((detailCompetitor.total_time*10).toLong()/1000)%60
                                        text = "        • durée maximale : " + ((parkour.max_duration).toLong())/60 + " minutes",
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = "        • position : " + parkour.position,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = "        • " + if (parkour.is_over == 1) {
                                            "terminé"
                                        } else {
                                            "non terminé"
                                        },
                                        fontSize = 13.sp,
                                        color = if (parkour.is_over == 1) {
                                            Color.Green
                                        } else {
                                            Color.Red
                                        }
                                    )
                                }
                            }
                            item {
                                Column {
                                    if (parkour.is_over == 0) {
                                        Button(
                                            onClick = {
                                                navController.navigate("competitor_view/${idCompetition}/${parkour.id}")
                                            },
                                            colors = ButtonColors(
                                                Color.Black,
                                                contentColor = Color.White,
                                                disabledContainerColor = Color.Gray,
                                                disabledContentColor = Color.White
                                            )
                                        ) {
                                            Image(
                                                imageVector = ImageVector.vectorResource(R.drawable.baseline_people_alt_24),
                                                contentDescription = "concurrents"
                                            )
                                        }
                                    } else {
                                        Button(
                                            onClick = {
                                                navController.navigate(Routes.parkourClassificationView + "/${idCompetition}/${parkour.id}")
                                            },
                                            colors = ButtonColors(
                                                Color.Black,
                                                contentColor = Color.White,
                                                disabledContainerColor = Color.Gray,
                                                disabledContentColor = Color.White
                                            )
                                        ) {
                                            Image(
                                                imageVector = ImageVector.vectorResource(R.drawable.baseline_elevator_24),
                                                contentDescription = "podium",
                                                colorFilter = ColorFilter.tint(Color.White)
                                            )
                                        }
                                    }

                                    Button(
                                        onClick = {
                                            navController.navigate("obstacle_of_the_parkour_view/${parkour.id}")
                                        },
                                        colors = ButtonColors(
                                            Color.Black,
                                            contentColor = Color.White,
                                            disabledContainerColor = Color.Gray,
                                            disabledContentColor = Color.White
                                        )
                                    ) {
                                        Image(
                                            imageVector = if (parkour.is_over == 0) {
                                                ImageVector.vectorResource(R.drawable.baseline_info_24)
                                            } else {
                                                ImageVector.vectorResource(R.drawable.baseline_keyboard_backspace_24)
                                            },
                                            contentDescription = if (parkour.is_over == 0) {
                                                "concurrents"
                                            } else {
                                                "podium"
                                            }
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