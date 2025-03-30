package com.example.parkour.views

import android.graphics.Picture
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.parkour.R
import com.example.parkour.Routes
import com.example.parkour.viewModel.ObstaclesViewModel
import com.example.parkour.model.Obstacles

@Composable
fun AddObstacle(modifier: Modifier, viewModel: ObstaclesViewModel, navController: NavController) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var obstacleName by remember { mutableStateOf("") }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imageUri = it
                Log.i("image", "Selected image URI: $it")
            }
        }

    val obstacles by viewModel.obstacles.observeAsState(emptyList())
    viewModel.getData()

    Column(
        modifier = modifier.fillMaxSize().background(Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Ajouter un obstacle",
            modifier = modifier.padding(50.dp),
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold
        )

        Row {
            Text(
                text = "Nom",
                modifier = modifier.padding(
                    start = 60.dp,
                    top = 30.dp,
                    end = 60.dp,
                    bottom = 30.dp
                )
            )
            TextField(
                value = obstacleName,
                onValueChange = { obstacleName = it },
                label = { Text(text = "Rentrer un nom") },
                modifier = Modifier.padding(end = 20.dp)
            )
        }
        val configuration = LocalConfiguration.current


        Row(
            modifier = Modifier.padding(top = 30.dp)
        ) {

            /* Text(
                text = "Photo",
                modifier = modifier.padding(
                    start = 60.dp,
                    top = 30.dp,
                    end = 30.dp,
                    bottom = 30.dp
                )
            )*/

            Button(onClick = { launcher.launch("image/*") }) {
                Text("Sélectionner une image")


            }
        }

        imageUri?.let { uri ->
            //  Text("oui")

            val painter = rememberAsyncImagePainter(uri)
            Image(
                painter = painter,
                contentDescription = "Image sélectionnée",
                modifier = Modifier
                    .padding(start = 20.dp)
                    .size(100.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
        }
        Button(
            onClick = {
                if (obstacleName.isNotEmpty() && imageUri != null) {
                    val imageUrl = imageUri.toString()
                    viewModel.postObstacle(Obstacles(1, obstacleName, imageUrl))
                } else {
                    Log.e("AddObstacle", "Nom ou image manquante")
                }
            },
            modifier = Modifier.padding(20.dp)
        ) {
            Text("Valider")
        }

        LazyColumn {
            for (obstacle in obstacles) {
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
                                    text = "➣  " + obstacle.name,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                obstacle.picture?.let { imageUrl ->
                                    Log.d("DEBUG", "Affichage de l'image: $imageUrl")
                                    val painter = rememberAsyncImagePainter(model = imageUrl)
                                    Image(
                                        painter = painter,
                                        contentDescription = "Image sélectionnée",
                                        modifier = Modifier
                                            .padding(start = 20.dp)
                                            .size(100.dp)
                                            .clip(RoundedCornerShape(10.dp))
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



