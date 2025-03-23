package com.example.parkour.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkour.api.RetrofitInstance
import com.example.parkour.model.Courses
import com.example.parkour.model.Obstacles
import com.example.parkour.model.Performances
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

    private val _obstacles = MutableLiveData<List<Obstacles>>()
    val obstacles: LiveData<List<Obstacles>> = _obstacles

    fun getObstaclesByCourseId(id:Int){
        viewModelScope.launch {
            val response = parkourApi.getObstaclesByCourseId(id)
            if(response.isSuccessful){
                _obstacles.postValue(response.body())
                Log.i("Reponse :",response.body().toString())
            }
            else{
                Log.i("Error :", response.message())
            }
        }
    }

    private val _performances = MutableLiveData<List<Performances>>()
    val performances: LiveData<List<Performances>> = _performances

    fun getPerformancesByCourseId(id: Int){
        viewModelScope.launch {
            val response = parkourApi.getPerformancesByCourseId(id)
            if(response.isSuccessful){
                _performances.postValue(response.body())
                Log.i("Reponse :",response.body().toString())
            }
            else{
                Log.i("Error :", response.message())
            }
        }
    }

    private val _course = MutableLiveData<Courses>()
    val course: LiveData<Courses> = _course

    fun getCourseById(id:Int){
        viewModelScope.launch {
            val response = parkourApi.getCourseById(id)
            if(response.isSuccessful){
                _course.postValue(response.body())
                Log.i("Reponse :",response.body().toString())
            }
            else{
                Log.i("Error :", response.message())
            }
        }
    }

}