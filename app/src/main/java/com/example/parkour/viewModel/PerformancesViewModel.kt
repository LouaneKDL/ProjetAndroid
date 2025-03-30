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

    private val _postPerformances = MutableLiveData<Performances>()
    val postPerformance: LiveData<Performances> = _postPerformances

    fun postPerformances(performances: Performances){
        viewModelScope.launch{
            val response = parkourApi.postPerformances(performances)
            if(response.isSuccessful){
                _postPerformances.postValue(response.body())
                Log.i("Reponse :",response.body().toString())
            }
            else{
                Log.i("Error :", response.message())
            }
        }
    }


    fun updatePerformance(id: Int, updatedPerformances: Performances) {
        viewModelScope.launch {
            try {
                val response = parkourApi.putPerformances(id, updatedPerformances)
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


    fun deletePerformance(id:Int){
        viewModelScope.launch {
            try {
                val response = parkourApi.deletePerformance(id)
                if (response.isSuccessful) {
                    Log.i("Success", "Performance delete : ${response.body()}")
                } else {
                    Log.e("Error", "Erreur lors du delete : ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Exception", "Erreur : ${e.message}")
            }
        }
    }





}