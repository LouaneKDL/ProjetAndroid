package com.example.parkour.views

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.parkour.model.CompetionUpdate
import com.example.parkour.viewModel.CompetitionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifierCompetition(modifier: Modifier = Modifier, competitionViewModel: CompetitionViewModel, navController: NavController, idCompetition: Int?) {

    val context = LocalContext.current

    var competitionName by remember { mutableStateOf("") }

    var ageMin by remember { mutableStateOf(10) }
    var ageMax by remember { mutableStateOf(11) }

    val ageMinAllowed = 9;
    val ageMaxAllowed = 120;

    var sexes = listOf("Femme", "Homme")
    var expanded by remember { mutableStateOf(false) }
    var sexe by remember { mutableStateOf(sexes.random()) }

    var hasRetry by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier,
            // .size(width = 240.dp, height = 100.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Modifier la compétition",
                    modifier = modifier.padding(25.dp, 10.dp),
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    modifier = Modifier,
                    value = competitionName,
                    onValueChange = { competitionName = it },
                    placeholder = { Text("Entrer le nom de la compétition") },
                    label = { Text("Nom de la compétition") }
                )

                Spacer(modifier = Modifier.size(30.dp))


                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(onClick = { if (ageMin > ageMinAllowed) ageMin-- }) { Text("-") }

                    OutlinedTextField(
                        value = ageMin.toString(),
                        onValueChange = { newValue ->
                            newValue.toIntOrNull()?.let {
                                if (it in ageMinAllowed..ageMaxAllowed) ageMin = it
                            } // Sécurise l'entrée
                        },
                        label = { Text("Âge minimum") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.width(250.dp)
                    )

                    Button(onClick = { if (ageMin < ageMax) ageMin++ }) { Text("+") }
                }

                Spacer(modifier = Modifier.size(30.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(onClick = { if (ageMax > ageMin) ageMax-- }) { Text("-") }

                    OutlinedTextField(
                        value = ageMax.toString(),
                        onValueChange = { newValue ->
                            newValue.toIntOrNull()?.let {
                                if (it in ageMinAllowed..ageMaxAllowed) ageMax = it
                            } // Sécurise l'entrée
                        },
                        label = { Text("Âge maximum") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.width(250.dp)
                    )

                    Button(onClick = { if (ageMax < ageMaxAllowed) ageMax++ }) { Text("+") }
                }

                Spacer(modifier = Modifier.size(30.dp))
                Box(

                ){
                    Row {
                        Text(text = "Sexe : ")
                        Text(
                            text = sexe,
                            modifier = Modifier.clickable { expanded = true }
                        )
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                        ) {
                            sexes.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        sexe = option
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Autoriser un 2ème essai : ")
                    Spacer(modifier = Modifier.width(10.dp))
                    Switch(
                        checked = hasRetry,
                        onCheckedChange = { hasRetry = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color.Black
                        )
                    )
                }

                Button(
                    modifier = Modifier.padding(10.dp),
                    onClick = {

                        // We are gonna do the api call to add the competition with the given parameter
                        if(competitionName.trim() == ""){
                            Toast.makeText(context, "Veuillez entrer un nom de compétition ! ", Toast.LENGTH_SHORT).show()
                        }else{

                            val competition = CompetionUpdate(
                                name = competitionName,
                                age_min = ageMin,
                                age_max = ageMax,
                                gender = if (sexe == "Homme") "H" else "F",
                                has_retry = hasRetry,
                                status = "not_ready"
                            )
                            // Log.e("LAla", "retry : " + hasRetry);
                            competitionViewModel.updateCompetition(idCompetition, competition)
                            navController.popBackStack()
                        }

                    },
                    colors = ButtonColors(
                        Color.Black,
                        contentColor = Color.White,
                        disabledContainerColor = Color.Gray,
                        disabledContentColor = Color.White
                    ),
                ) {
                    Text("Ajouter")
                }
            }
        }
    }
}
