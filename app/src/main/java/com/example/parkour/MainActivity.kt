package com.example.parkour

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.parkour.ui.theme.ParkourTheme
import com.example.parkour.viewModel.AddObstacleView
import com.example.parkour.viewModel.CompetitionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val competitionViewModel = ViewModelProvider(this)[CompetitionViewModel::class.java]
        enableEdgeToEdge()
        setContent {
            ParkourTheme {
                Surface(Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AddObstacleView(Modifier)
                }
            }
        }
    }
}
