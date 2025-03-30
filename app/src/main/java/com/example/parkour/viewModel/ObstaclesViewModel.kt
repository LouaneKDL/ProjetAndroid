package com.example.parkour.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkour.api.RetrofitInstance
import com.example.parkour.model.Obstacles
import com.example.parkour.model.Performances
import kotlinx.coroutines.launch

class ObstaclesViewModel : ViewModel() {

    private val parkourApi = RetrofitInstance.parkourApi

    private val _obstacles = MutableLiveData<List<Obstacles>>()
    val obstacles: LiveData<List<Obstacles>> = _obstacles

    fun getData() {
        viewModelScope.launch {
            val response = parkourApi.getObstacles()
            if (response.isSuccessful) {
                _obstacles.postValue(response.body())
                Log.i("Reponse :", response.body().toString())
            } else {
                Log.i("Error :", response.message())
            }
        }
    }

    private val _obstacle = MutableLiveData<Obstacles>()
    val obstacle: LiveData<Obstacles> = _obstacle

    fun getObstacleById(id: Int) {
        viewModelScope.launch {
            val response = parkourApi.getObstacleById(id)
            if (response.isSuccessful) {
                _obstacle.postValue(response.body())
                Log.i("Reponse :", response.body().toString())
            } else {
                Log.i("Error :", response.message())
            }
        }
    }

    private val _postObstacle = MutableLiveData<Obstacles>()
    val postObstacle: LiveData<Obstacles> = _postObstacle

    fun postObstacle(obstacles: Obstacles){
        Log.d("DEBUG", "PostObstacle: ${obstacles.name} - ${obstacles.picture}")
        viewModelScope.launch{
            val response = parkourApi.postObstacle(obstacles)
            if(response.isSuccessful){
                _postObstacle.postValue(response.body())
                Log.i("Reponse :",response.body().toString())
            }
            else{
                Log.i("Error :", response.message())
            }
        }
    }

    fun updateObstacles(id: Int, updatedObstacles: Obstacles) {
        viewModelScope.launch {
            try {
                val response = parkourApi.putObstacles(id, updatedObstacles)
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

    fun deleteObstacle(id:Int){
        viewModelScope.launch {
            try {
                val response = parkourApi.deleteObstacle(id)
                if (response.isSuccessful) {
                    Log.i("Success", "Obstacle delete : ${response.body()}")
                } else {
                    Log.e("Error", "Erreur lors du delete : ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Exception", "Erreur : ${e.message}")
            }
        }
    }

}