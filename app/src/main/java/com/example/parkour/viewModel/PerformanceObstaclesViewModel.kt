package com.example.parkour.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkour.api.RetrofitInstance
import com.example.parkour.model.Performance_obstacles
import com.example.parkour.model.Performance_obstaclesPost
import com.example.parkour.model.Performance_obstaclesRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * ViewModel for managing performance obstacle data
 */
class PerformanceObstaclesViewModel : ViewModel() {

    private val parkourApi = RetrofitInstance.parkourApi

    private val _performanceObstacles = MutableLiveData<List<Performance_obstacles>>()
    val performanceObstacles: LiveData<List<Performance_obstacles>> = _performanceObstacles

    /**
     * Fetches the list of performance obstacles from the API.
     */
    fun getData() {
        viewModelScope.launch {
            val response = parkourApi.getPerformanceObstacles()
            if(response.isSuccessful){
                _performanceObstacles.postValue(response.body())
                Log.i("Performance Obstacles :",response.body().toString())
            }
            else{
                Log.i("Error :", response.message())
            }
        }
    }


    private val _performanceObstacle = MutableLiveData<Performance_obstacles>()
    val performanceObstacle: LiveData<Performance_obstacles> = _performanceObstacle

    /**
     * Fetches a performance obstacle by its ID.
     *
     * @param id The ID of the performance obstacle to fetch.
     */
    fun getPerfomanceById(id: Int) {
        viewModelScope.launch {
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

    /**
     * Posts a new performance obstacle.
     *
     * @param performanceObstacles The performance obstacle data to post.
     */
    fun postPerformanceObstacles(performanceObstacles: Performance_obstaclesPost){
        viewModelScope.launch{
            val response = parkourApi.postPerformanceObstacles(performanceObstacles)
            if(response.isSuccessful){
                _postPerformanceObstacle.postValue(response.body())
                Log.i("Reponse :",response.body().toString())
                delay(500)
                getData()
            }
            else{
                Log.i("Error :", response.message())
            }
        }
    }

    /**
     * Updates an existing performance obstacle.
     *
     * @param id The ID of the performance obstacle to update.
     * @param updatedPerformances The updated performance obstacle data.
     */
    fun updatePerformanceObstacles(id: Int, updatedPerformances: Performance_obstaclesRequest) {
        viewModelScope.launch {
            try {
                val response = parkourApi.putPerformanceObstacles(id, updatedPerformances)
                if (response.isSuccessful) {
                    Log.i("Success", "Parcours mis à jour : ${response.body()}")
                    delay(1000)
                    getPerfomanceById(id)
                    getData()
                } else {
                    Log.e("Error", "Erreur lors de la mise à jour : ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Exception", "Erreur : ${e.message}")
            }
        }
    }

}