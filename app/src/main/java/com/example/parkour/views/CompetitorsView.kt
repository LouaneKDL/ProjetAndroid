package com.example.parkour.views

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.parkour.R
import com.example.parkour.model.Competitors
import com.example.parkour.model.Courses
import com.example.parkour.model.Performances
import com.example.parkour.viewModel.CompetitionViewModel
import com.example.parkour.viewModel.CompetitorsViewModel
import com.example.parkour.viewModel.PerformancesViewModel


@SuppressLint("ResourceType")
@Composable
fun Competitors(
    modifier: Modifier = Modifier,
    competitionViewModel: CompetitionViewModel,
    competitorsViewModel: CompetitorsViewModel,
    performancesViewModel: PerformancesViewModel,
    navController: NavController,
    idCompetition: Int?,
    idCourse: Int?
) {

    //competitors of a competition
    val competitors by competitionViewModel.competitors.observeAsState(emptyList())
    if (idCompetition != null) {
        LaunchedEffect(idCompetition) {
            competitionViewModel.getInscriptionsByCompetitionId(idCompetition)
        }
    }

    //competitors of a course
    /*var competitorsList = mutableListOf<Competitors>()

    for (competitor in competitors){

        val courses by competitorsViewModel.courses.observeAsState(emptyList())
        competitorsViewModel.getCoursesByACompetitor(competitor.id)
        for (course in courses){
            if (course.id == idCourse){ //if a course of a competitior is the one wanted, we add the competitor
                competitorsList.add(competitor)
                break
            }
        }

    }*/
    val competitorsList = mutableListOf<Competitors>()

// On crée un cache pour les courses déjà récupérées
    val coursesCache = mutableMapOf<Int, List<Courses>>()

    for (competitor in competitors) {
        // On récupère les courses depuis le cache ou on les charge si elles ne sont pas encore là
        val courses = coursesCache.getOrPut(competitor.id) {
            competitorsViewModel.getCoursesByACompetitor(competitor.id)
            competitorsViewModel.courses.value ?: emptyList()
        }

        // On vérifie si une des courses correspond
        if (courses.any { it.id == idCourse }) {
            competitorsList.add(competitor)
        }
    }


    val details by performancesViewModel.performances.observeAsState(emptyList())
    performancesViewModel.getData()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.LightGray),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Compétiteurs",
            modifier = modifier.padding(10.dp),
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold
        )

        Column(
            modifier = modifier
                .clip(shape = RoundedCornerShape(8.dp))
                .background(Color.White)
                .padding(8.dp)

        ){

            LazyColumn {
                for (competitor in competitorsList){

                    item{
                        LazyRow(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .width(300.dp)
                                .border(width = 1.dp, color = Color.Black).padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            item{

                                Column {
                                    Text(
                                        text = "➣  " + competitor.first_name + " " + competitor.last_name,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "        • " + competitor.gender,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = "        • " + competitor.born_at,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = "        • " + competitor.email,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = "        • " + competitor.phone,
                                        fontSize = 13.sp
                                    )

                                    var detailCompetitor : Performances? = null
                                    for (detail in details){
                                        if (detail.course_id == idCourse && detail.competitor_id == competitor.id){
                                            detailCompetitor = detail
                                        }
                                    }

                                    if (detailCompetitor != null){
                                        Text(
                                            text = "        • Statut du parkour : " +
                                                    if (detailCompetitor.status == "over") {
                                                        "terminé"
                                                    }
                                                    else if (detailCompetitor.status == "to_verify"){
                                                        "à vérifier"
                                                    }
                                                    else if (detailCompetitor.status == "to_finish"){
                                                        "à finir"
                                                    }
                                                    else{
                                                        "défection"
                                                    },
                                            fontSize = 13.sp
                                        )
                                        Text(
                                            text = "        • Durée totale : " + detailCompetitor.total_time/20/60 + " minutes",
                                            fontSize = 13.sp
                                        )
                                    }
                                    else{
                                        Text(
                                            text = "        • Statut du parkour : non commencé",
                                            fontSize = 13.sp
                                        )
                                        Text(
                                            text = "        • Durée totale : non commencé",
                                            fontSize = 13.sp
                                        )
                                    }


                                }
                            }
                            item{
                                Column{
                                    Button(
                                        onClick = {
                                            navController.navigate("obstacles_view/${competitor.id}/${idCourse}")
                                        },
                                        colors = ButtonColors(
                                            Color.Black,
                                            contentColor = Color.White,
                                            disabledContainerColor = Color.Gray,
                                            disabledContentColor = Color.White
                                        )
                                    ) {
                                        Image(
                                            imageVector = ImageVector.vectorResource(R.drawable.baseline_info_24),
                                            contentDescription = "obstacles"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}