package com.example.parkour.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.example.parkour.api.RetrofitInstance
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkour.model.Competition
import com.example.parkour.model.Competitors
import com.example.parkour.model.Courses
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

    private val _competition = MutableLiveData<Competition>()
    val competition: LiveData<Competition> = _competition

    fun getCompetitionById(id:Int){
        viewModelScope.launch {
            val response = parkourApi.getCompetitionById(id)
            if(response.isSuccessful){
                _competition.postValue(response.body())
                Log.i("Reponse :",response.body().toString())
            }
            else{
                Log.i("Error :", response.message())
            }
        }
    }

    private val _competitors = MutableLiveData<List<Competitors>>()
    val competitors: LiveData<List<Competitors>> = _competitors

    fun getInscriptionsByCompetitionId(id: Int){
        viewModelScope.launch {
            val response = parkourApi.getInscriptionsByCompetitionId(id)
            if(response.isSuccessful){
                _competitors.postValue(response.body())
                Log.i("Reponse :",response.body().toString())
            }
            else{
                Log.i("Error :", response.message())
            }
        }
    }

    private val _courses = MutableLiveData<List<Courses>>()
    val courses: LiveData<List<Courses>> = _courses

    fun getCoursesByCompetitionId(id:Int){
        viewModelScope.launch {
            val response = parkourApi.getCoursesByCompetitionId(id)
            if(response.isSuccessful){
                _courses.postValue(response.body())
                Log.i("Reponse :",response.body().toString())
            }
            else{
                Log.i("Error :", response.message())
            }
        }
    }

}