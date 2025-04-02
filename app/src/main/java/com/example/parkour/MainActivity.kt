package com.example.parkour

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.parkour.model.Obstacles
import com.example.parkour.ui.theme.ParkourTheme
import com.example.parkour.viewModel.CompetitionViewModel
import com.example.parkour.viewModel.CoursesViewModel
import com.example.parkour.viewModel.CompetitorsViewModel
import com.example.parkour.viewModel.ObstaclesViewModel
import com.example.parkour.viewModel.PerformancesViewModel
import com.example.parkour.viewModel.PerformanceObstaclesViewModel
import com.example.parkour.viewModel.ResetViewModel
import com.example.parkour.views.AddCompetition
import com.example.parkour.views.AddObstacle
import com.example.parkour.views.AddObstacleAvailableView
import com.example.parkour.views.Competition
import com.example.parkour.views.CompetitorRegistration
import com.example.parkour.views.Competitors
import com.example.parkour.views.Parkour
import com.example.parkour.views.ParkourRegistration
import com.example.parkour.views.PotentialCompetitorRegistration
import com.example.parkour.views.Obstacles
import com.example.parkour.views.ObstaclesOfTheParkour

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Unused
        val competitionViewModel = ViewModelProvider(this)[CompetitionViewModel::class.java]
        val coursesViewModel = ViewModelProvider(this)[CoursesViewModel::class.java]
        val competitorsViewModel = ViewModelProvider(this)[CompetitorsViewModel::class.java]
        val performancesViewModel = ViewModelProvider(this)[PerformancesViewModel::class.java]
        val obstaclesViewModel = ViewModelProvider(this)[ObstaclesViewModel::class.java]
        val performanceObstaclesViewModel = ViewModelProvider(this)[PerformanceObstaclesViewModel::class.java]
        //val resetViewModel = ViewModelProvider(this)[ResetViewModel::class.java]
        //resetViewModel.reset()

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

                        composable("obstacle_of_the_parkour_view/{id}"){
                                backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id")?.toInt()
                            ObstaclesOfTheParkour(
                                obstaclesViewModel,
                                coursesViewModel,
                                id,
                                navController
                            )
                        }

                        composable("add_obstacle_available/{id}"){
                                backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id")?.toInt()
                            AddObstacleAvailableView(
                                obstaclesViewModel,
                                coursesViewModel,
                                id,
                                navController
                            )
                        }

                        composable(Routes.addObstacle) {
                            AddObstacle(
                                obstaclesViewModel,
                                navController
                            )
                        }

                        composable("obstacles_view/{idCompetitor}/{idCourse}/{idPerformances}"){
                                backStackEntry ->
                            val idCompetitor = backStackEntry.arguments?.getString("idCompetitor")?.toIntOrNull()
                            val idCourse = backStackEntry.arguments?.getString("idCourse")?.toIntOrNull()
                            val idPerformances = backStackEntry.arguments?.getString("idPerformances")?.toIntOrNull()
                            Obstacles(
                                modifier = Modifier.padding(innerPadding),
                                competitorsViewModel,
                                coursesViewModel,
                                performancesViewModel,
                                performanceObstaclesViewModel,
                                navController,
                                idCompetitor,
                                idCourse,
                                idPerformances
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
                                val id = backStackEntry.arguments?.getString("id")?.toInt()
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
                                performancesViewModel,
                                coursesViewModel,
                                navController,
                                idCompetition,
                                idCourse
                            )
                        }

                        composable("add_potential_competitor/{id}"){
                                backStackEntry ->
                            val idCompetition = backStackEntry.arguments?.getString("id").orEmpty().toInt()
                            PotentialCompetitorRegistration(
                                modifier = Modifier.padding(innerPadding),
                                competitorsViewModel,
                                navController,
                                competitionViewModel,
                                idCompetition
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
