package com.example.parkour.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.parkour.R
import com.example.parkour.viewModel.CoursesViewModel
import com.example.parkour.viewModel.ObstaclesViewModel

@Composable
fun ObstaclesOfTheParkour(
    viewModelObstacles: ObstaclesViewModel,
    viewModelCourse: CoursesViewModel,
    idParkour: Int?,
    navController: NavHostController
) {
    val obstacles by viewModelCourse.obstacles.observeAsState(emptyList())

    if (idParkour != null) {
        LaunchedEffect(idParkour) {
            viewModelCourse.getObstaclesByCourseId(idParkour)
        }
    }

    val parkour by viewModelCourse.course.observeAsState()
    if (idParkour != null) {
        LaunchedEffect(idParkour) {
            viewModelCourse.getCourseById(idParkour)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Obstacles du parkour : ${parkour?.name ?: "Chargement..."}",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
            modifier = Modifier.padding(20.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .border(1.dp, Color.LightGray, RoundedCornerShape(10.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(obstacles) { obstacle ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                            .border(1.dp, Color.Gray, RoundedCornerShape(10.dp)),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "➣ ${obstacle.obstacle_name}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )

                            Row {
                                IconButton(
                                    onClick = {
                                        if (idParkour != null) {
                                            viewModelCourse.deleteObstacleFromCourse(idParkour,obstacle.obstacle_id)
                                        }
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.baseline_remove_24),
                                        contentDescription = "Supprimer",
                                        modifier = Modifier.size(32.dp),
                                        tint = Color.Red
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { navController.navigate("add_obstacle_available/${idParkour}") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EA)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ajouter un obstacle disponible", color = Color.White)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { } ,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF03A9F4)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Créer un nouveau obstacle", color = Color.White)
        }
    }
}
