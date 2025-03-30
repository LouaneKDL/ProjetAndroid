package com.example.parkour

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.parkour.model.Obstacles
import com.example.parkour.ui.theme.ParkourTheme
import com.example.parkour.viewModel.AddObstacleView
import com.example.parkour.viewModel.CompetitionViewModel
import com.example.parkour.viewModel.CoursesViewModel
import com.example.parkour.viewModel.CompetitorsViewModel
import com.example.parkour.viewModel.PerformancesViewModel
import com.example.parkour.views.Competition
import com.example.parkour.views.CompetitorRegistration
import com.example.parkour.views.Competitors
import com.example.parkour.views.Obstacles
import com.example.parkour.views.Parkour

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Unused
        val competitionViewModel = ViewModelProvider(this)[CompetitionViewModel::class.java]
        val coursesViewModel = ViewModelProvider(this)[CoursesViewModel::class.java]
        val competitorsViewModel = ViewModelProvider(this)[CompetitorsViewModel::class.java]
        val performancesViewModel = ViewModelProvider(this)[PerformancesViewModel::class.java]

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
