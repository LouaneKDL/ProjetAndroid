package com.example.parkour.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkour.api.RetrofitInstance
import com.example.parkour.model.CompetitorRequest
import com.example.parkour.model.Competitors
import com.example.parkour.model.Courses
import com.example.parkour.model.Performance_obstacles
import com.example.parkour.model.Performances
import kotlinx.coroutines.launch

/**
 * ViewModel for managing competitor date
 */
class CompetitorsViewModel : ViewModel() {

    private val parkourApi = RetrofitInstance.parkourApi

    private val _competitors = MutableLiveData<List<Competitors>>()
    val competitors: LiveData<List<Competitors>> = _competitors

    /**
     * Fetches the list of competitors
     */
    fun getData(){
        viewModelScope.launch{
            val response = parkourApi.getCompetitors()
            if(response.isSuccessful){
                _competitors.postValue(response.body())
                Log.i("Reponse :",response.body().toString())
            }
            else{
                Log.i("Error :", response.message())
            }
        }
    }

    private val _competitor = MutableLiveData<Competitors>()
    val competitor: LiveData<Competitors> = _competitor

    /**
     * Fetches the competitor by its ID
     *
     * @param id the id competitor
     */
    fun getCompetitorById(id: Int?){
        viewModelScope.launch{
            val response = parkourApi.getCompetitor(id)
            if(response.isSuccessful){
                _competitor.postValue(response.body())
                Log.i("Reponse :",response.body().toString())
            }
            else{
                Log.i("Error :", response.message())
            }
        }
    }

    private val _performance = MutableLiveData<Performances>()
    val performance: LiveData<Performances> = _performance

    /**
     * Fetches the list of performance at a competitor
     *
     * @param id the id competitor
     */
    fun getPerformanceByACompetitor(id:Int){
        viewModelScope.launch{
            val response = parkourApi.getPerformanceForACompetitor(id)
            if(response.isSuccessful){
                _performance.postValue(response.body())
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
     * Fetches the list of courses at a competitor
     *
     * @param id the id competitor
     */
    fun getCoursesByACompetitor(id:Int){
        viewModelScope.launch{
            val response = parkourApi.getCoursesByACompetitorId(id)
            if(response.isSuccessful){
                _courses.postValue(response.body())
                Log.i("Reponse :",response.body().toString())
            }
            else{
                Log.i("Error :", response.message())
            }
        }
    }

    private val _detailPerformances = MutableLiveData<Performance_obstacles>()
    val detailPerformances: LiveData<Performance_obstacles> = _detailPerformances

    /**
     * Fetches performance data at a competitor
     *
     * @param id the id competitor
     */
    fun getPerformanceDetailsByCompetitor(id:Int){
        viewModelScope.launch{
            val response = parkourApi.getPerformanceDetailsByIdCompetitor(id)
            if(response.isSuccessful){
                _detailPerformances.postValue(response.body())
                Log.i("Reponse :",response.body().toString())
            }
            else{
                Log.i("Error :", response.message())
            }
        }
    }

    private val _competitorsPost = MutableLiveData<CompetitorRequest>()
    val competitorsPost: LiveData<CompetitorRequest> = _competitorsPost

    /**
     * Posts a new competitor
     *
     * @param competitorRequest The competitor data
     */
    fun postCompetitor(competitorRequest: CompetitorRequest){
        viewModelScope.launch {
            try {
                Log.i("Payload :", competitorRequest.toString())
                val response = parkourApi.postCompetitors(competitorRequest)
                if(response.isSuccessful){
                    _competitorsPost.postValue(response.body())
                    Log.i("Reponse :", response.body().toString())
                    getData()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.i("Error :", "Code: ${response.code()}, Message: ${response.message()}, Error Body: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("Error :", "Exception: ${e.message}")
            }
        }
    }

    /**
     *  Updates an existing competitor
     *  @param id the ID of the competitor to update
     * @param updatedCompetitor the updated competitor data
     */
    fun updateCompetitor(id: Int?, updatedCompetitor: CompetitorRequest) {
        viewModelScope.launch {
            try {
                val response = parkourApi.putCompetitors(id, updatedCompetitor)
                if (response.isSuccessful) {
                    Log.i("Success", "Compétiteur mis à jour : ${response.body()}")
                } else {
                    Log.e("Error", "Erreur lors de la mise à jour : ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Exception", "Erreur : ${e.message}")
            }
        }
    }

    /**
     * Deletes a competitor by its ID.
     *
     * @param id The ID of the competitor to delete.
     */
    fun deleteCompetitor(id:Int){
        viewModelScope.launch {
            try {
                val response = parkourApi.deleteCompetitor(id)
                if (response.isSuccessful) {
                    Log.i("Success", "Competitor delete : ${response.body()}")
                } else {
                    Log.e("Error", "Erreur lors du delete: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Exception", "Erreur : ${e.message}")
            }
        }
    }


}