package com.example.parkour.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.example.parkour.api.RetrofitInstance
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkour.api.ParkourApi
import com.example.parkour.model.Competition
import com.example.parkour.model.CompetitionRequest
import com.example.parkour.model.CompetitorRequest
import com.example.parkour.model.Competitors
import com.example.parkour.model.Courses
import kotlinx.coroutines.launch

/**
 * ViewModel for managing competition data
 */
class CompetitionViewModel : ViewModel(){

    private val parkourApi = RetrofitInstance.parkourApi

    private val _competitions = MutableLiveData<List<Competition>>()
    val competitions: LiveData<List<Competition>> = _competitions

    /**
     * Fetches the list of competitions
     */
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

    /**
     * Fetches the competition by its ID
     *
     * @param id the id competition
     */
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

    /**
     * Fetches the list of competitors at a competition
     *
     * @param id the id competition
     */
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

    /**
     * Fetches the list of courses at a competition
     *
     * @param id the id competition
     */
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
    /**
     * Posts a new competition
     *
     * @param competition The competition data
     */
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

    /**
     * Adds a competitor at a competition
     *
     * @param competitionId the ID of the competition
     * @param competitors the competitor data to add
     */
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

    /**
     *  Updates an existing competition
     *  @param id the ID of the competition to update
     * @param updatedCompetition the updated competition data
     */
    fun updateCompetition(id: Int, updatedCompetition: Competition) {
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
    /**
     * Deletes a competition by its ID.
     *
     * @param id The ID of the competition to delete.
     */
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

    /**
     * Deletes a competition from a competition.
     *
     * @param id The ID of the competition.
     * @param idCompetitior The ID of the competitor to delete.
     */
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