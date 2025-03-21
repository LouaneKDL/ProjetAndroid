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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.parkour.R
import com.example.parkour.viewModel.CompetitionViewModel
import com.example.parkour.viewModel.CoursesViewModel


@SuppressLint("ResourceType")
@Composable
fun Competition(modifier: Modifier = Modifier, viewModels: Array<ViewModel>) {

    var buttonCoursesPressed by remember { mutableStateOf(-1) }

    if (buttonCoursesPressed != -1){
        Parkour(modifier, viewModels, buttonCoursesPressed)
    }
    else{

        var viewModel : CompetitionViewModel = viewModels.get(0) as CompetitionViewModel
        val competitions by viewModel.competitions.observeAsState(emptyList())
        viewModel.getData()

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color.LightGray),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = "Compétitions",
                modifier = modifier.padding(10.dp),
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold
            )

            Button(
                modifier = Modifier.padding(10.dp),
                onClick = {},
                colors = ButtonColors(
                    Color.Black,
                    contentColor = Color.White,
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.White
                ),
                enabled = false
            ){
                Text(
                    text = "Ajouter une compétition",
                    modifier = Modifier,
                    color = Color.White
                )
            }

            Column(
                modifier = modifier
                    .clip(shape = RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .padding(8.dp)

            ){

                LazyColumn {
                    for (compet in competitions){
                        item{
                            LazyRow(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .width(300.dp)
                                    .border(width = 1.dp, color = Color.Black).padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                item{

                                    Column {
                                        Text(
                                            text = "➣  " + compet.name,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "        • " + compet.gender,
                                            fontSize = 13.sp
                                        )
                                        Text(
                                            text = "        • " + compet.age_min + " à " + compet.age_max,
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                                item{
                                    Column{
                                        Button(
                                            onClick = {},
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
                                        Button(
                                            onClick = {
                                                buttonCoursesPressed = compet.id
                                            },
                                            colors = ButtonColors(
                                                Color.Black,
                                                contentColor = Color.White,
                                                disabledContainerColor = Color.Gray,
                                                disabledContentColor = Color.White
                                            )
                                        ) {
                                            Image(
                                                imageVector = ImageVector.vectorResource(R.drawable.baseline_info_24),
                                                contentDescription = "parkours"
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