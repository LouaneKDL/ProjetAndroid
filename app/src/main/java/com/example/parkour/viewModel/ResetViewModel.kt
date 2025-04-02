package com.example.parkour.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkour.api.RetrofitInstance
import kotlinx.coroutines.launch

/**
 * ViewModel for handling the reset operation.
 */
class ResetViewModel : ViewModel() {

    private val parkourApi = RetrofitInstance.parkourApi

    /**
     * Executes a reset operation by calling the API.
     *
     * This method sends a request to reset the data or state managed by the API.
     * It logs the success or failure of the operation.
     */
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
