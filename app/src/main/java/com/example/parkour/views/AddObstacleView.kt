package com.example.parkour.views

import android.app.DatePickerDialog
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
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
import java.util.Calendar


@Composable
fun AddObstacle(viewModel: ObstaclesViewModel, navController: NavController) {
    var obstacleName by rememberSaveable { mutableStateOf("") }
    var obstacleCreated by rememberSaveable { mutableStateOf("") }
    var obstacleUpdated by rememberSaveable { mutableStateOf("") }
    var errorMessage by rememberSaveable { mutableStateOf("") }

    val obstacles by viewModel.obstacles.observeAsState(emptyList())
    viewModel.getData()

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

   /*val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            obstacleUpdated = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )*/
    val datePickerCreated = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val formattedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
            obstacleCreated = formattedDate
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val datePickerUpdated = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val formattedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
            obstacleUpdated = formattedDate
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Ajouter un obstacle",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        OutlinedTextField(
            value = obstacleName,
            onValueChange = { obstacleName = it },
            label = { Text(text = "Rentrer un nom") },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )


        OutlinedTextField(
            value = obstacleCreated,
            onValueChange = { obstacleCreated = it },
            label = { Text(text = "Rentrer la date de création") },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { datePickerCreated.show() }) {
                    Icon(imageVector = Icons.Default.DateRange, contentDescription = "Sélectionner une date")
                }
            }
        )

// Champ pour obstacleUpdated
        OutlinedTextField(
            value = obstacleUpdated,
            onValueChange = { obstacleUpdated = it },
            label = { Text(text = "Rentrer la date de modification") },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { datePickerUpdated.show() }) {
                    Icon(imageVector = Icons.Default.DateRange, contentDescription = "Sélectionner une date")
                }
            }
        )




        Button(
            onClick = {
                Log.d("ObstacleInfo", "name: $obstacleName, created: $obstacleCreated, updated: $obstacleUpdated")
                if (obstacleName.isNotEmpty() && obstacleCreated.isNotEmpty() && obstacleUpdated.isNotEmpty()) {
                    viewModel.postObstacle(
                        Obstacles(
                            id = 1,
                            name = obstacleName,
                            created_at = obstacleCreated,
                            updated_at = obstacleUpdated
                        )
                    )
                } else {
                    errorMessage = "Tous les champs doivent être remplis."
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EA)),
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Text("Valider")
        }

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(obstacles) { obstacle ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.Black)
                        .padding(10.dp)
                ) {
                    Text(
                        text = "➣  ${obstacle.name}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "➣  ${obstacle.created_at}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "➣  ${obstacle.updated_at}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
