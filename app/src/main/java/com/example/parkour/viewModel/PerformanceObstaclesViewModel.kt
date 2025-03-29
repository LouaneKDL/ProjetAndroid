package com.example.parkour.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkour.api.RetrofitInstance
import com.example.parkour.model.Competitors
import com.example.parkour.model.Performance_obstacles
import com.example.parkour.model.Performances
import kotlinx.coroutines.launch

class PerformanceObstaclesViewModel : ViewModel() {

    private val parkourApi = RetrofitInstance.parkourApi

    private val _performanceObstacles = MutableLiveData<List<Performance_obstacles>>()
    val performanceObstacles: LiveData<List<Performance_obstacles>> = _performanceObstacles

    fun getData(){
        viewModelScope.launch{
            val response = parkourApi.getPerformanceObstacles()
            if(response.isSuccessful){
                _performanceObstacles.postValue(response.body())
                Log.i("Reponse :",response.body().toString())
            }
            else{
                Log.i("Error :", response.message())
            }
        }
    }


    private val _performanceObstacle = MutableLiveData<Performance_obstacles>()
    val performanceObstacle: LiveData<Performance_obstacles> = _performanceObstacle

    fun getPerfomanceById(id:Int){
        viewModelScope.launch{
            val response = parkourApi.getPerformanceObstaclesById(id)
            if(response.isSuccessful){
                _performanceObstacle.postValue(response.body())
                Log.i("Reponse :",response.body().toString())
            }
            else{
                Log.i("Error :", response.message())
            }
        }
    }

    private val _postPerformanceObstacle = MutableLiveData<Performance_obstacles>()
    val postPerformanceObstacle: LiveData<Performance_obstacles> = _postPerformanceObstacle

    fun postPerformanceObstacles(performanceObstacles: Performance_obstacles){
        viewModelScope.launch{
            val response = parkourApi.postPerformanceObstacles(performanceObstacles)
            if(response.isSuccessful){
                _postPerformanceObstacle.postValue(response.body())
                Log.i("Reponse :",response.body().toString())
            }
            else{
                Log.i("Error :", response.message())
            }
        }
    }


    fun updatePerformanceObstacles(id: Int, updatedPerformances: Performance_obstacles) {
        viewModelScope.launch {
            try {
                val response = parkourApi.putPerformanceObstacles(id, updatedPerformances)
                if (response.isSuccessful) {
                    Log.i("Success", "Parcours mis à jour : ${response.body()}")
                } else {
                    Log.e("Error", "Erreur lors de la mise à jour : ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Exception", "Erreur : ${e.message}")
            }
        }
    }

}