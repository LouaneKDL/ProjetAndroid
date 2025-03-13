package com.example.parkour

import android.app.LauncherActivity
import android.os.Bundle
import android.widget.ListView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parkour.ui.theme.ParkourTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ParkourTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Competition(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Competition(modifier: Modifier = Modifier) {

    Column(
        modifier = modifier.fillMaxSize().background(Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Compétitions RamboWarrior",
            modifier = modifier.padding(24.dp),
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold
        )
        Column(
            modifier = modifier.background(Color.White).padding(8.dp)
        ){
            val compets = listOf(
                "Ninja Warrior 2023 - Hommes",
                "Parkour 2024 - Femmes",
                "SuperFort 2025 - Hommes - 20 à 30 ans")
            for (compet in compets){
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "- "+compet,
                        fontSize = 15.sp
                    )
                    Button(onClick = {}, Modifier.size(40.dp)) { Text(text = "C") }
                    Button(onClick = {}, Modifier.size(40.dp)) { Text(text = "P") }
                }
            }

        }

        Button(
            onClick = {},
            colors = ButtonColors(
                Color.Black,
                contentColor = Color.White,
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.White
            )
        ){
            Text(
                text = "Ajouter une compétition",
                modifier = modifier
            )
        }

    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ParkourTheme {
        Competition()
    }
}