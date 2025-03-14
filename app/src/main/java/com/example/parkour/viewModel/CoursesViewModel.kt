package com.example.parkour.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkour.api.RetrofitInstance
import com.example.parkour.model.Courses
import kotlinx.coroutines.launch

class CoursesViewModel : ViewModel() {

    private val parkourApi = RetrofitInstance.parkourApi

    private val _courses = MutableLiveData<List<Courses>>()
    val courses: LiveData<List<Courses>> = _courses

    fun getData(){
        viewModelScope.launch {
            val response = parkourApi.getCourses()
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