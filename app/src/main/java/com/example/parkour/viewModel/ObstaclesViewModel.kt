package com.example.parkour.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkour.api.RetrofitInstance
import com.example.parkour.model.ObstacleNoDate
import com.example.parkour.model.Obstacles
import kotlinx.coroutines.launch

/**
 * ViewModel for managing obstacle data
 */
class ObstaclesViewModel : ViewModel() {

    private val parkourApi = RetrofitInstance.parkourApi

    private val _obstacles = MutableLiveData<List<Obstacles>>()
    val obstacles: LiveData<List<Obstacles>> = _obstacles

    /**
     * Fetches the list of obstacles from the API.
     */
    fun getData() {
        viewModelScope.launch {
            val response = parkourApi.getObstacles()
            if (response.isSuccessful) {
                _obstacles.postValue(response.body())
                Log.i("Response :", response.body().toString())
            } else {
                Log.i("Error :", response.message())
            }
        }
    }

    private val _obstacle = MutableLiveData<Obstacles>()
    val obstacle: LiveData<Obstacles> = _obstacle

    /**
     * Fetches an obstacle by its ID.
     *
     * @param id The ID of the obstacle to fetch.
     */
    fun getObstacleById(id: Int) {
        viewModelScope.launch {
            val response = parkourApi.getObstacleById(id)
            if (response.isSuccessful) {
                _obstacle.postValue(response.body())
                Log.i("Response :", response.body().toString())
            } else {
                Log.i("Error :", response.message())
            }
        }
    }

    private val _postObstacle = MutableLiveData<Obstacles>()
    val postObstacle: LiveData<Obstacles> = _postObstacle

    /**
     * Posts a new obstacle.
     *
     * @param obstacles The obstacle data to post.
     */
    fun postObstacle(obstacles: ObstacleNoDate) {
        viewModelScope.launch {
            val response = parkourApi.postObstacle(obstacles)
            if (response.isSuccessful) {
                _postObstacle.postValue(response.body())
                Log.i("Response :", response.body().toString())
                getData()
            } else {
                Log.i("Error :", response.message())
            }
        }
    }

    /**
     * Updates an existing obstacle.
     *
     * @param id The ID of the obstacle to update.
     * @param updatedObstacles The updated obstacle data.
     */
    fun updateObstacles(id: Int, updatedObstacles: Obstacles) {
        viewModelScope.launch {
            try {
                val response = parkourApi.putObstacles(id, updatedObstacles)
                if (response.isSuccessful) {
                    Log.i("Success", "Obstacle updated: ${response.body()}")
                } else {
                    Log.e("Error", "Error updating: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Exception", "Error: ${e.message}")
            }
        }
    }

    /**
     * Deletes an obstacle by its ID.
     *
     * @param id The ID of the obstacle to delete.
     */
    fun deleteObstacle(id: Int) {
        viewModelScope.launch {
            try {
                val response = parkourApi.deleteObstacle(id)
                if (response.isSuccessful) {
                    Log.i("Success", "Obstacle deleted: ${response.body()}")
                } else {
                    Log.e("Error", "Error deleting: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Exception", "Error: ${e.message}")
            }
        }
    }
}
