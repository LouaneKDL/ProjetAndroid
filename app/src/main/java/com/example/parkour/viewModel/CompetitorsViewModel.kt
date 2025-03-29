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

class CompetitorsViewModel : ViewModel() {

    private val parkourApi = RetrofitInstance.parkourApi

    private val _competitors = MutableLiveData<List<Competitors>>()
    val competitors: LiveData<List<Competitors>> = _competitors

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

    fun getCompetitorById(id:Int){
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

    fun postCompetitor(competitorRequest: CompetitorRequest){
        viewModelScope.launch{
            val response = parkourApi.postCompetitors(competitorRequest)
            if(response.isSuccessful){
                _competitorsPost.postValue(response.body())
                Log.i("Reponse :",response.body().toString())
            }
            else{
                Log.i("Error :", response.message())
            }
        }
    }

    fun updateCompetitor(id: Int, updatedCompetitor: Competitors) {
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