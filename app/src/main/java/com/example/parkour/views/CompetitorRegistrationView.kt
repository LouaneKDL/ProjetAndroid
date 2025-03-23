package com.example.parkour.views

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.parkour.R
import com.example.parkour.Routes
import com.example.parkour.viewModel.CompetitionViewModel
import com.example.parkour.viewModel.CompetitorsViewModel

@Composable
fun CompetitorRegistration(modifier: Modifier = Modifier,
                           competitorsViewModel: CompetitorsViewModel,
                           navController: NavController,
                           competitionsViewModel: CompetitionViewModel,
                           competitionID: String
                           ) {

    val competitors by competitorsViewModel.competitors.observeAsState(emptyList())
    competitorsViewModel.getData()

    val registeredCompetitors by competitionsViewModel.competitions.observeAsState(emptyList())
    competitionsViewModel.getInscriptionsByCompetitionId(competitionID.toInt())

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Button(
                onClick = {
                navController.navigate(Routes.competitionView)
                },
                colors = ButtonColors(
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
                                        text = "➣  " + competitor.last_name + " " + competitor.first_name,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "        • " + competitor.gender,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = "        • " + competitor.born_at,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                            item {
                                Column {
                                    if(registeredCompetitors.any{ it.id == competitor.id}){
                                        Text(
                                            text = "Inscrit ",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Green
                                        )
                                    }else{
                                        Button(
                                            onClick = {},
                                            colors = ButtonColors(
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


                                }
                            }
                        }
                    }
                }
            }
        }

    }
}