package com.example.parkour.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.example.parkour.api.RetrofitInstance
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkour.api.ParkourApi
import com.example.parkour.model.CompetionUpdate
import com.example.parkour.model.Competition
import com.example.parkour.model.CompetitionRequest
import com.example.parkour.model.Competitors
import com.example.parkour.model.Courses
import kotlinx.coroutines.launch

class CompetitionViewModel : ViewModel(){

    private val parkourApi = RetrofitInstance.parkourApi

    private val _competitions = MutableLiveData<List<Competition>>()
    val competitions: LiveData<List<Competition>> = _competitions

    fun getData(){
        viewModelScope.launch {
            val response = parkourApi.getCompetitions()
            if(response.isSuccessful){
                _competitions.postValue(response.body())
                Log.i("Reponse :",response.body().toString())
            }
            else{
                Log.i("Error :", response.message())
            }
        }
    }

    private val _competition = MutableLiveData<Competition>()
    val competition: LiveData<Competition> = _competition

    fun getCompetitionById(id:Int){
        viewModelScope.launch {
            val response = parkourApi.getCompetitionById(id)
            if(response.isSuccessful){
                _competition.postValue(response.body())
                Log.i("Reponse :",response.body().toString())
            }
            else{
                Log.i("Error :", response.message())
            }
        }
    }

    private val _competitors = MutableLiveData<List<Competitors>>()
    val competitors: LiveData<List<Competitors>> = _competitors

    fun getInscriptionsByCompetitionId(id: Int){
        viewModelScope.launch {
            val response = parkourApi.getInscriptionsByCompetitionId(id)
            if(response.isSuccessful){
                _competitors.postValue(response.body())
                Log.i("Reponse :",response.body().toString())
            }
            else{
                Log.i("Error :", response.message())
            }
        }
    }

    private val _courses = MutableLiveData<List<Courses>>()
    val courses: LiveData<List<Courses>> = _courses

    fun getCoursesByCompetitionId(id:Int){
        viewModelScope.launch {
            val response = parkourApi.getCoursesByCompetitionId(id)
            if(response.isSuccessful){
                _courses.postValue(response.body())
                Log.i("Reponse :",response.body().toString())
            }
            else{
                Log.i("Error :", response.message())
            }
        }
    }

    private val _competitionPost = MutableLiveData<Competition>()
    val competitionPost: LiveData<Competition> = _competitionPost

    fun postCompetition(competition: CompetitionRequest){
        viewModelScope.launch {
            val response = parkourApi.postCompetitions(competition)
            if(response.isSuccessful){
                _competitionPost.postValue(response.body())
                Log.i("Reponse :",response.body().toString())
            }
            else{
                Log.i("Error :", response.message())
            }
        }
    }

    private val _competitorPost = MutableLiveData<Competitors>()
    val competitorPost: LiveData<Competitors> = _competitorPost

    fun postCompetitorToCompetitionById(competitionId: Int, competitors: ParkourApi.CompetitourRequest){
        viewModelScope.launch {
            val response = parkourApi.postCompetitorsToCompetitionById(competitionId,competitors)
            Log.d("API Request", "URL: /api/competitions/$competitionId/add_competitor")
            Log.d("API Request", "Competitor ID: $competitors")
            if(response.isSuccessful){
                _competitorPost.postValue(response.body())
                Log.i("Reponse :",response.body().toString())
                getInscriptionsByCompetitionId(competitionId)
            }
            else{
                Log.i("Error :", response.message())
            }
        }
    }

    fun updateCompetition(id: Int?, updatedCompetition: CompetionUpdate) {
        viewModelScope.launch {
            try {
                val response = parkourApi.putCompetition(id, updatedCompetition)
                if (response.isSuccessful) {
                    Log.i("Success", "Compétition mise à jour : ${response.body()}")
                } else {
                    Log.e("Error", "Erreur lors de la mise à jour : ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Exception", "Erreur : ${e.message}")
            }
        }
    }

    fun deleteCompetition(id:Int){
        viewModelScope.launch {
            try {
                val response = parkourApi.deleteCompetitionById(id)
                if (response.isSuccessful) {
                    Log.i("Success", "Compétition delete : ${response.body()}")
                } else {
                    Log.e("Error", "Erreur lors du delete : ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Exception", "Erreur : ${e.message}")
            }
        }
    }

    fun deleteCompetition(id:Int, idCompetitior: Int){
        viewModelScope.launch {
            try {
                val response = parkourApi.deleteCompetitorFromCompetition(id, idCompetitior)
                if (response.isSuccessful) {
                    Log.i("Success", "Competitor delete : ${response.body()}")
                } else {
                    Log.e("Error", "Erreur lors du delete : ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Exception", "Erreur : ${e.message}")
            }
        }
    }




}