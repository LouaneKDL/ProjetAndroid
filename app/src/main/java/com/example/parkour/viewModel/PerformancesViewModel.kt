package com.example.parkour.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkour.api.RetrofitInstance
import com.example.parkour.model.Performance_obstacles
import com.example.parkour.model.Performances
import com.example.parkour.model.PerformancesRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * ViewModel for managing performance data
 */
class PerformancesViewModel : ViewModel(){

    private val parkourApi = RetrofitInstance.parkourApi

    private val _performances = MutableLiveData<List<Performances>>()
    val performances: LiveData<List<Performances>> = _performances

    /**
     * Fetches the list of performances from the API.
     */
    fun getData() {
        viewModelScope.launch {
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

    /**
     * Fetches a performance by its ID.
     *
     * @param id The ID of the performance to fetch.
     */
    fun getPerformanceById(id: Int) {
        viewModelScope.launch {
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

    /**
     * Fetches detailed performance data for a specific performance.
     *
     * @param id The ID of the performance.
     */
    fun getPerformanceDetailsById(id: Int) {
        viewModelScope.launch {
            val response = parkourApi.getDetailsByPerformancesById(id)
            if(response.isSuccessful){
                _details.postValue(response.body())
                Log.i("Reponse :",response.body().toString())
                getData()
            }
            else{
                Log.i("Error :", response.message())
            }
        }
    }

    private val _postPerformances = MutableLiveData<Performances>()
    val postPerformance: LiveData<Performances> = _postPerformances

    /**
     * Posts a new performance.
     *
     * @param performances The performance data to post.
     */
    fun postPerformances(performances: PerformancesRequest){
        viewModelScope.launch{
            val response = parkourApi.postPerformances(performances)
            if(response.isSuccessful){
                _postPerformances.postValue(response.body())
                //Log.i("Success", "Parcours AJOUT2 : ${response.body()}")
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
     * Updates an existing performance.
     *
     * @param id The ID of the performance to update.
     * @param updatedPerformances The updated performance data.
     */

    fun updatePerformance(id: Int, updatedPerformances: PerformancesRequest) {
        viewModelScope.launch {
            try {
                val response = parkourApi.putPerformances(id, updatedPerformances)
                if (response.isSuccessful) {
                    Log.i("Success", "Performances mises à jour : ${response.body()}")
                    delay(500)
                    getData()
                } else {
                    Log.e("Error", "Erreur lors de la mise à jour : ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Exception", "Erreur : ${e.message}")
            }
        }
    }

    /**
     * Deletes a performance by its ID.
     *
     * @param id The ID of the performance to delete.
     */
    fun deletePerformance(id: Int) {
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