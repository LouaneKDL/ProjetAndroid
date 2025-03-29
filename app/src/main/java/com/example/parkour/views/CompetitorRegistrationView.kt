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
import com.example.parkour.viewModel.CompetitionViewModel
import com.example.parkour.viewModel.CompetitorsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompetitorRegistration(modifier: Modifier = Modifier, viewModelCompetitor: CompetitorsViewModel, competitionViewModel: CompetitionViewModel, navController: NavController) {

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("H") }
    var bornAt by remember { mutableStateOf("") }

    var firstNameError by remember { mutableStateOf(false) }
    var lastNameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf(false) }
    var bornAtError by remember { mutableStateOf(false) }

    val competitorResponse by competitionViewModel.competitorPost.observeAsState()
    var competitorId by remember { mutableStateOf(-1) }

    LaunchedEffect(competitorResponse) {
        competitorResponse?.let { competitor ->
            competitorId = competitor.id
        }
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF4F5F7))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Ajouter un compétiteur",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it; firstNameError = it.isBlank() },
            label = { Text("Prénom") },
            isError = firstNameError,
            placeholder = { Text("Entrez votre prénom", color = Color.Gray) },
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
        if (firstNameError) Text("Le prénom est requis", color = Color.Red, fontSize = 12.sp)

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it; lastNameError = it.isBlank() },
            label = { Text("Nom") },
            isError = lastNameError,
            placeholder = { Text("Entrez votre nom", color = Color.Gray) },
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
        if (lastNameError) Text("Le nom est requis", color = Color.Red, fontSize = 12.sp)

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()
            },
            label = { Text("Email") },
            isError = emailError,
            placeholder = { Text("Entrez votre email", color = Color.Gray) },
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
        if (emailError) Text("Email invalide", color = Color.Red, fontSize = 12.sp)

        OutlinedTextField(
            value = phone,
            onValueChange = {
                phone = it
                phoneError = !it.matches(Regex("^(\\+\\d{1,3})?\\d{10}\$"))
            },
            label = { Text("Téléphone") },
            isError = phoneError,
            placeholder = { Text("Entrez votre téléphone", color = Color.Gray) },
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
        if (phoneError) Text("Numéro invalide (10 chiffres requis)", color = Color.Red, fontSize = 12.sp)

        OutlinedTextField(
            value = bornAt,
            onValueChange = {
                bornAt = it
                bornAtError = !it.matches(Regex("^\\d{4}-\\d{2}-\\d{2}\$"))
            },
            label = { Text("Date de naissance") },
            isError = bornAtError,
            placeholder = { Text("Entrez la date de naissance (YYYY-MM-DD)", color = Color.Gray) },
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
        if (bornAtError) Text("Format de date invalide", color = Color.Red, fontSize = 12.sp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("H" to "Homme", "F" to "Femme").forEach { (code, label) ->
                Button(
                    onClick = { gender = code },
                    modifier = Modifier
                        .padding(8.dp)
                        .width(120.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (gender == code) Color(0xFF6200EE) else Color.Gray
                    ),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(label, color = Color.White)
                }
            }
        }

        Button(
            onClick = {
                firstNameError = firstName.isBlank()
                lastNameError = lastName.isBlank()
                emailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                phoneError = !phone.matches(Regex("^(\\+\\d{1,3})?\\d{10}\$"))
                bornAtError = !bornAt.matches(Regex("^\\d{4}-\\d{2}-\\d{2}\$"))

                if (!firstNameError && !lastNameError && !emailError && !phoneError && !bornAtError) {
                    val competitor = CompetitorRequest(firstName, lastName, email, phone, gender, bornAt)
                    viewModelCompetitor.postCompetitor(competitor)

                    competitionViewModel.postCompetitorToCompetitionById(competitorId, competitor)
                    navController.popBackStack()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .height(50.dp),
            enabled = !firstNameError && !lastNameError && !emailError && !phoneError && !bornAtError,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            Text("Ajouter", fontSize = 18.sp, color = Color.White)
        }
    }
}
