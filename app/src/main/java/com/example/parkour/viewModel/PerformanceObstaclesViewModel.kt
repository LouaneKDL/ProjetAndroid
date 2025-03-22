package com.example.parkour.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkour.api.RetrofitInstance
import com.example.parkour.model.Competitors
import com.example.parkour.model.Performance_obstacles
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
}