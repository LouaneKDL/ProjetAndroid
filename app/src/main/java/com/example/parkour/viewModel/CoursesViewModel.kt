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

    private val _postCourse = MutableLiveData<Courses>()
    val postCourse: LiveData<Courses> = _postCourse

    fun postCourse(courses: Courses){
        viewModelScope.launch {
            val response = parkourApi.postCourse(courses)
            if(response.isSuccessful){
                _postCourse.postValue(response.body())
                Log.i("Reponse :",response.body().toString())
            }
            else{
                Log.i("Error :", response.message())
            }
        }
    }

    private val _postObstacleCourse = MutableLiveData<Obstacles>()
    val postObstaclesCourse: LiveData<Obstacles> = _postObstacleCourse

    fun postObstacleToCourseById(id: Int, obstacles: Obstacles){
        viewModelScope.launch {
            val response = parkourApi.postObstacleToCourseById(id,obstacles)
            if(response.isSuccessful){
                _postObstacleCourse.postValue(response.body())
                Log.i("Reponse :",response.body().toString())
            }
            else{
                Log.i("Error :", response.message())
            }
        }
    }

    private val _updateObstaclePositionCourse = MutableLiveData<Obstacles>()
    val updateObstaclePositionCourse: LiveData<Obstacles> = _updateObstaclePositionCourse

    fun postObstacleInCertainPositionByCourseId(id: Int, obstacles: Obstacles){
        viewModelScope.launch {
            val response = parkourApi.postObstacleInCertainPositionByCourseId(id,obstacles)
            if(response.isSuccessful){
                _updateObstaclePositionCourse.postValue(response.body())
                Log.i("Reponse :",response.body().toString())
            }
            else{
                Log.i("Error :", response.message())
            }
        }
    }

    fun updateCourse(id: Int, updatedCourse: Courses) {
        viewModelScope.launch {
            try {
                val response = parkourApi.putCourses(id, updatedCourse)
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

    fun deleteCourses(id:Int){
        viewModelScope.launch {
            try {
                val response = parkourApi.deleteCourse(id)
                if (response.isSuccessful) {
                    Log.i("Success", "Course delete : ${response.body()}")
                } else {
                    Log.e("Error", "Erreur lors du delete : ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Exception", "Erreur : ${e.message}")
            }
        }
    }

    fun deleteObstacleFromCourse(id:Int, idObstacles:Int){
        viewModelScope.launch {
            try {
                val response = parkourApi.deleteObstacleFromCourse(id, idObstacles)
                if (response.isSuccessful) {
                    Log.i("Success", "Obstacle delete : ${response.body()}")
                } else {
                    Log.e("Error", "Erreur lors du delete : ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Exception", "Erreur : ${e.message}")
            }
        }
    }



}