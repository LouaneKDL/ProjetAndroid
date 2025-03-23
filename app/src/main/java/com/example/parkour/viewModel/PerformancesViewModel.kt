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

class PerformancesViewModel : ViewModel(){

    private val parkourApi = RetrofitInstance.parkourApi

    private val _performances = MutableLiveData<List<Performances>>()
    val performances: LiveData<List<Performances>> = _performances

    fun getData(){
        viewModelScope.launch{
            val response = parkourApi.getPerformances()
            if(response.isSuccessful){
                _performances.postValue(response.body())
                Log.i("Reponse :",response.body().toString())
            }
            else{
                Log.i("Error :", response.message())
            }
        }
    }

    private val _performance = MutableLiveData<Performances>()
    val performance: LiveData<Performances> = _performance

    fun getPerformanceById(id:Int){
        viewModelScope.launch{
            val response = parkourApi.getPerformancesById(id)
            if(response.isSuccessful){
                _performance.postValue(response.body())
                Log.i("Reponse :",response.body().toString())
            }
            else{
                Log.i("Error :", response.message())
            }
        }
    }

    private val _details = MutableLiveData<List<Performance_obstacles>>()
    val details: LiveData<List<Performance_obstacles>> = _details

    fun getPerformanceDetailsById(id:Int){
        viewModelScope.launch{
            val response = parkourApi.getDetailsByPerformancesById(id)
            if(response.isSuccessful){
                _details.postValue(response.body())
                Log.i("Reponse :",response.body().toString())
            }
            else{
                Log.i("Error :", response.message())
            }
        }
    }





}