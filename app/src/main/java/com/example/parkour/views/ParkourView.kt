package com.example.parkour.views

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.livedata.observeAsState
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
        viewModel.getCoursesByCompetitionId(idCompetition)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Parkours",
            modifier = modifier.padding(10.dp),
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold
        )

        Button(
            modifier = Modifier.padding(10.dp),
            onClick = { navController.navigate("parkour_registration_view/${idCompetition}") },
            colors = ButtonColors(
                Color.Black,
                contentColor = Color.White,
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.White
            ),
            enabled = true
        ) {
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
                                    Text(
                                        text = "        • durée maximale : " + parkour.max_duration,
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

                                }
                            }
                        }
                    }
                }
            }
        }
    }
}