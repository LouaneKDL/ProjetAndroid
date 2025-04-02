package com.example.parkour

object Routes {

    var competitionView = "competition_view"
    var competitorRegistrationView = "competition_registration_view"
    var parkourView = "parkour_view/{id}"
    var addCompetitionView = "add_competition_view"
    var addPotentialCompetitorRegistrationView = "add_potential_competitor/{id}"
    var competitorView = "competitor_view/{idCompetition}/{idCourse}"
    var obstaclesView = "obstacles_view/{idCompetitor}/{idCourse}/{idPerformances}"
    var addObstacle = "add_obstacles_view"

    var modifCompetition = "modifier_competition/{id}"
    var modifCompetiteur = "modifier_competiteur/{id}"

    var obstacleOfTheParkour = "obstacle_of_the_parkour_view/{id}"
    var addObstacleAvailable = "add_obstacle_available/{id}"
}