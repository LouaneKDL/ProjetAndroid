package com.example.parkour

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
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
import com.example.parkour.views.AddCompetition
import com.example.parkour.views.Competition
import com.example.parkour.views.CompetitorRegistration
import com.example.parkour.views.Competitors
import com.example.parkour.views.Parkour
import com.example.parkour.views.ParkourRegistration

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
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Routes.competitionView, builder = {

                        composable(Routes.addCompetitionView) {
                            AddCompetition(
                                Modifier.padding(innerPadding),
                                competitionViewModel,
                                navController
                            )
                        }
                        composable(Routes.competitionView){
                            Competition(
                                modifier = Modifier.padding(innerPadding),
                                competitionViewModel,
                                navController
                            )
                        }

                        composable("competition_registration_view"){
                            CompetitorRegistration(
                                modifier = Modifier.padding(innerPadding),
                                competitorsViewModel,
                                competitionViewModel,
                                navController,
                            )
                        }

                        composable("parkour_registration_view/{id}"){
                            backStackEntry ->
                                val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                            ParkourRegistration(
                                modifier = Modifier.padding(innerPadding),
                                coursesViewModel,
                                competitionViewModel,
                                navController,
                                id
                            )
                        }

                        composable("parkour_view/{id}"){
                            backStackEntry ->
                                val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
                            Parkour(
                                modifier = Modifier.padding(innerPadding),
                                competitionViewModel,
                                navController,
                                id
                            )
                        }

                        composable("competitor_view/{idCompetition}/{idCourse}"){
                            backStackEntry ->
                                val idCompetition = backStackEntry.arguments?.getString("idCompetition")?.toIntOrNull()
                                val idCourse = backStackEntry.arguments?.getString("idCourse")?.toIntOrNull()
                            Competitors(
                                modifier = Modifier.padding(innerPadding),
                                competitionViewModel,
                                competitorsViewModel,
                                navController,
                                idCompetition,
                                idCourse
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
