package com.example.parkour

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.parkour.ui.theme.ParkourTheme
import com.example.parkour.viewModel.CompetitionViewModel
import com.example.parkour.viewModel.CoursesViewModel
import com.example.parkour.viewModel.CompetitorsViewModel
import com.example.parkour.viewModel.PerformancesViewModel
import com.example.parkour.views.Competition
import com.example.parkour.views.Competitors
import com.example.parkour.views.CompetitorRegistration

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val competitionViewModel = ViewModelProvider(this)[CompetitionViewModel::class.java]
        val coursesViewModel = ViewModelProvider(this)[CoursesViewModel::class.java]
        val competitorsViewModel = ViewModelProvider(this)[CompetitorsViewModel::class.java]
        val performancesViewModel = ViewModelProvider(this)[PerformancesViewModel::class.java]

        val viewModels = arrayOf(competitionViewModel, coursesViewModel, competitorsViewModel, performancesViewModel)

        val competitorsViewModel = ViewModelProvider(this)[CompetitorsViewModel::class.java]
        // vueInscriptionConcurrent
        enableEdgeToEdge()
        setContent {
            ParkourTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Routes.competitionView, builder = {

                        composable(Routes.competitionView){
                            Competition(
                                modifier = Modifier.padding(innerPadding),
                                competitionViewModel,
                                navController
                            )
                        }

                        composable(Routes.competitorRegistrationView){
                            CompetitorRegistration(
                                modifier = Modifier.padding(innerPadding),
                                competitorsViewModel,
                                navController
                            )
                        }
                    })
                    /*
                    Competition(
                        modifier = Modifier.padding(innerPadding),
                        viewModels
                    )
                    */
                }
            }
        }
    }
}
