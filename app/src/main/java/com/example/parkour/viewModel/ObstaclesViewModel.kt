package com.example.parkour.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkour.api.RetrofitInstance
import com.example.parkour.model.Obstacles
import kotlinx.coroutines.launch

class ObstaclesViewModel : ViewModel() {

    private val parkourApi = RetrofitInstance.parkourApi

    private val _obstacles = MutableLiveData<List<Obstacles>>()
    val obstacles: LiveData<List<Obstacles>> = _obstacles

    fun getData(){
        viewModelScope.launch {
            val response = parkourApi.getObstacles()
            if(response.isSuccessful){
                _obstacles.postValue(response.body())
                Log.i("Reponse :",response.body().toString())
            }
            else{
                Log.i("Error :", response.message())
            }
        }
    }

}