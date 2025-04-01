package com.example.parkour.views

import android.util.Log
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
import com.example.parkour.model.Obstacles
import com.example.parkour.viewModel.CoursesViewModel
import com.example.parkour.viewModel.ObstaclesViewModel

@Composable
fun AddObstacleAvailableView(
    viewModelObstacles: ObstaclesViewModel,
    viewModelCourse: CoursesViewModel,
    idParkour: Int?,
    navController: NavHostController?
) {
    val obstacles by viewModelCourse.obstacles.observeAsState(emptyList())  // Obstacles de la course
    val obstaclesAvailable by viewModelObstacles.obstacles.observeAsState(emptyList())  // Tous les obstacles

    viewModelObstacles.getData()
    if (idParkour != null) {
        LaunchedEffect(idParkour) {
            viewModelCourse.getObstaclesByCourseId(idParkour)
        }
    }



    // Exclure les obstacles déjà utilisés dans la course
    val existingObstacleIds = obstacles.map { it.obstacle_id }



    var filteredObstacles by remember { mutableStateOf(emptyList<Obstacles>()) }

    LaunchedEffect(obstacles, obstaclesAvailable) {
        filteredObstacles = obstaclesAvailable.filter { it.id !in obstacles.map { o -> o.obstacle_id } }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Obstacles disponibles",
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
                items(filteredObstacles) { obstacle ->  // Utilisation de la liste filtrée
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
                                text = "➣ ${obstacle.name}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )

                            IconButton(
                                onClick = {
                                    if (idParkour != null) {
                                        Log.i("fkezifjzeipfezjifpjk","${obstacle.id}")
                                        viewModelCourse.postObstacleToCourseById(idParkour,
                                            obstacle.id
                                        )
                                        viewModelCourse.getObstaclesByCourseId(idParkour)
                                    }
                                }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.baseline_add_24),
                                    contentDescription = "Ajouter",
                                    modifier = Modifier.size(32.dp),
                                    tint = Color.Green
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
