
package com.example.parkour.views

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.parkour.model.CompetitionRequest
import com.example.parkour.viewModel.CompetitionViewModel

/*
{
  "name": "string",
  "age_min": 0,
  "age_max": 0,
  "gender": "H",
  "has_retry": true
}
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddCompetition(
    modifier: Modifier,
    competitionViewModel: CompetitionViewModel?,
    navController: NavHostController?
) {
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
                    text = "Ajouter une nouvelle compétition",
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

                            val competition = CompetitionRequest(
                                name = competitionName,
                                age_min = ageMin,
                                age_max = ageMax,
                                gender = if (sexe == "Homme") "H" else "F",
                                has_retry = if(hasRetry) 1 else 0,
                            )
                            // Log.e("LAla", "retry : " + hasRetry);
                            competitionViewModel?.postCompetition(competition)
                            navController?.popBackStack()
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
/*
@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun myPreview() {
    AddCompetition(
        modifier = Modifier,
        null,
        null
    )
}
*/
