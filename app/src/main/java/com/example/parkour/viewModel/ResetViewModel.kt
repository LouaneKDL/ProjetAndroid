package com.example.parkour.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkour.api.RetrofitInstance
import com.example.parkour.model.Competition
import kotlinx.coroutines.launch

class ResetViewModel : ViewModel() {

    private val parkourApi = RetrofitInstance.parkourApi

    fun reset() {
        viewModelScope.launch {
            try {
                val response = parkourApi.reset()
                if (response.isSuccessful) {
                    Log.i("Success", "Reset effectué correctement : ${response.body()}")
                } else {
                    Log.e("Error", "Erreur lors de la mise à jour : ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Exception", "Erreur : ${e.message}")
            }
        }
    }

}