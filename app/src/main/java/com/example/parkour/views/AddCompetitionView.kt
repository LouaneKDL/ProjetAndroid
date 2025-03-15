package com.example.parkour.views

import android.content.ClipData.Item
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun addCompetitionView(modifier: Modifier) {
    val gender = listOf("Femme", "Homme")
    val yesNo = listOf("Oui", "Non")

    val (selectedGender, onGenderSelected) = remember { mutableStateOf(gender[0]) }
    val (selectedYesNo, onYesNoSelected) = remember { mutableStateOf(yesNo[0]) }

    Column(
        modifier = modifier.fillMaxSize().background(Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Ajouter une compétition",
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
                ),

                )
            TextField(
                value = "",
                onValueChange = {},
                label = { Text(text = "Rentrer un nom") },
                modifier = Modifier.padding(end= 20.dp)
            )
        }

        Row {
            Text(
                text = "Age minimum",
                modifier = modifier.padding(24.dp)

            )
            TextField(
                value = "",
                onValueChange = {},
                label = { Text(text = "Rentrer un age minimum") },
                modifier = Modifier.padding(end= 20.dp)
            )
        }

        Row {
            Text(
                text = "Age maximum",
                modifier = modifier.padding(24.dp)
            )
            TextField(
                value = "",
                onValueChange = {},
                label = { Text(text = "Rentrer un age maximum") },
                modifier = Modifier.padding(end= 20.dp)
            )
        }

        Row {
            Text(
                text = "Genre",
                modifier = modifier.padding(start = 60.dp, top = 30.dp, end = 60.dp, bottom = 30.dp)
            )
            Column(modifier.selectableGroup()) {
                gender.forEach { text ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .selectable(
                                selected = (text == selectedGender),
                                onClick = { onGenderSelected(text) },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (text == selectedGender),
                            onClick = null // null recommended for accessibility with screen readers
                        )
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        }
        Row {
            Text(
                text = "A déjà essayé",
                modifier = modifier.padding(30.dp)
            )
            Column(modifier.selectableGroup()) {
                yesNo.forEach { text ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .selectable(
                                selected = (text == selectedYesNo),
                                onClick = { onYesNoSelected(text) },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (text == selectedYesNo),
                            onClick = null
                        )
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }


        }
        // val statusList = listOf("not_ready", "not_started", "started", "finished")
        Column { //voir pour la liste déroulante

        }


        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(onClick = {
                    println("clique")
                }) {
                    Text("Ajouter")
                }
            }
        }

    }
}



