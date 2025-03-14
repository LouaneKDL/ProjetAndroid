package com.example.parkour.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.example.parkour.api.RetrofitInstance
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkour.model.Competition
import kotlinx.coroutines.launch

class CompetitionViewModel : ViewModel(){

    private val parkourApi = RetrofitInstance.parkourApi

    private val _competitions = MutableLiveData<List<Competition>>()
    val competitions: LiveData<List<Competition>> = _competitions

    fun getData(){
        viewModelScope.launch {
            val response = parkourApi.getCompetitions()
            if(response.isSuccessful){
                _competitions.postValue(response.body())
                Log.i("Reponse :",response.body().toString())
            }
            else{
                Log.i("Error :", response.message())
            }
        }
    }

}