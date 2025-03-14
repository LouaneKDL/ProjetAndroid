package com.example.parkour.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkour.api.RetrofitInstance
import com.example.parkour.model.Competitors
import kotlinx.coroutines.launch

class CompetitorsViewModel : ViewModel() {

    private val parkourApi = RetrofitInstance.parkourApi

    private val _competitors = MutableLiveData<List<Competitors>>()
    val competitors: LiveData<List<Competitors>> = _competitors

    fun getData(){
        viewModelScope.launch{
            val response = parkourApi.getCompetitors()
            if(response.isSuccessful){
                _competitors.postValue(response.body())
                Log.i("Reponse :",response.body().toString())
            }
            else{
                Log.i("Error :", response.message())
            }
        }
    }

}