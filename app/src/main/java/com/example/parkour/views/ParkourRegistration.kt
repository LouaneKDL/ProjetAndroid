package com.example.parkour.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.parkour.model.CompetitorRequest
import com.example.parkour.model.CourseRequest
import com.example.parkour.viewModel.CompetitionViewModel
import com.example.parkour.viewModel.CoursesViewModel

/**
 * Composable function to register a new parkour course.
 *
 * @param modifier Modifier for styling and layout.
 * @param viewModelParkour ViewModel for managing parkour course data.
 * @param competitionViewModel ViewModel for managing competition data.
 * @param navController Navigation controller for navigating between screens.
 * @param idCompetition The ID of the competition to which the parkour course belongs.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParkourRegistration(
    modifier: Modifier = Modifier,
    viewModelParkour: CoursesViewModel,
    competitionViewModel: CompetitionViewModel,
    navController: NavController,
    idCompetition: Int?
) {
    // State variables for parkour course details
    var name by remember { mutableStateOf("") }
    var max_duration by remember { mutableStateOf("") }
    var competitionId: Int = idCompetition ?: -1
    var nameError by remember { mutableStateOf(false) }
    var maxDurationError by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF4F5F7))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Ajouter un Parkour",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Input field for parkour course name
        OutlinedTextField(
            value = name,
            onValueChange = { name = it; nameError = it.isBlank() },
            label = { Text("Nom Parkour") },
            isError = nameError,
            placeholder = { Text("Entrez le nom du parkour", color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .background(Color.White, RoundedCornerShape(12.dp)),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.White,
                focusedBorderColor = Color(0xFF6200EE),
                unfocusedBorderColor = Color.Gray
            )
        )
        if (nameError) Text("Le nom est requis", color = Color.Red, fontSize = 12.sp)

        // Input field for maximum duration
        OutlinedTextField(
            value = max_duration,
            onValueChange = { max_duration = it; maxDurationError = it.isBlank() },
            label = { Text("Durée maximale") },
            isError = maxDurationError,
            placeholder = { Text("Entrez la durée maximale du parkour", color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .background(Color.White, RoundedCornerShape(12.dp)),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.White,
                focusedBorderColor = Color(0xFF6200EE),
                unfocusedBorderColor = Color.Gray
            )
        )
        if (maxDurationError) Text("La durée maximale est requise", color = Color.Red, fontSize = 12.sp)

        // Button to submit the parkour course data
        Button(
            onClick = {
                nameError = name.isBlank()
                maxDurationError = max_duration.isBlank()

                if (!nameError && !maxDurationError) {
                    val course = CourseRequest(name, Integer.parseInt(max_duration), competitionId)
                    viewModelParkour.postCourse(course)
                    navController.popBackStack()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .height(50.dp),
            enabled = !nameError && !maxDurationError,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            Text("Ajouter", fontSize = 18.sp, color = Color.White)
        }
    }
}
