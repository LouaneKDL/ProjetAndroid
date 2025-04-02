package com.example.parkour.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkour.api.RetrofitInstance
import com.example.parkour.model.CourseRequest
import com.example.parkour.model.Courses
import com.example.parkour.model.ObstacleCourse
import com.example.parkour.model.ObstaclePost
import com.example.parkour.model.Obstacles
import com.example.parkour.model.Performances
import kotlinx.coroutines.launch

/**
 * ViewModel for managing course data
 */
class CoursesViewModel : ViewModel() {

    private val parkourApi = RetrofitInstance.parkourApi

    private val _courses = MutableLiveData<List<Courses>>()
    val courses: LiveData<List<Courses>> = _courses

    /**
     * Fetches the list of courses
     */
    fun getData() {
        viewModelScope.launch {
            val response = parkourApi.getCourses()
            if (response.isSuccessful) {
                _courses.postValue(response.body())
                Log.i("Response :", response.body().toString())
            } else {
                Log.i("Error :", response.message())
            }
        }
    }

    private val _obstacles = MutableLiveData<List<ObstacleCourse>>()
    val obstacles: LiveData<List<ObstacleCourse>> = _obstacles

    /**
     * Fetches the list of obstacles for at a course
     *
     * @param id The id of the course.
     */
    fun getObstaclesByCourseId(id: Int) {
        viewModelScope.launch {
            val response = parkourApi.getObstaclesByCourseId(id)
            if (response.isSuccessful) {
                _obstacles.postValue(response.body())
                Log.i("Response :", response.body().toString())
            } else {
                Log.i("Error :", response.message())
            }
        }
    }

    private val _performances = MutableLiveData<List<Performances>>()
    val performances: LiveData<List<Performances>> = _performances

    /**
     * Fetches the list of performances for a specific course.
     *
     * @param id The ID of the course.
     */
    fun getPerformancesByCourseId(id: Int) {
        viewModelScope.launch {
            val response = parkourApi.getPerformancesByCourseId(id)
            if (response.isSuccessful) {
                _performances.postValue(response.body())
                Log.i("Response :", response.body().toString())
            } else {
                Log.i("Error :", response.message())
            }
        }
    }

    private val _course = MutableLiveData<Courses>()
    val course: LiveData<Courses> = _course

    /**
     * Fetches a course by its ID.
     *
     * @param id The ID of the course to fetch.
     */
    fun getCourseById(id: Int) {
        viewModelScope.launch {
            val response = parkourApi.getCourseById(id)
            if (response.isSuccessful) {
                _course.postValue(response.body())
                Log.i("Response :", response.body().toString())
            } else {
                Log.i("Error :", response.message())
            }
        }
    }

    private val _postCourse = MutableLiveData<Courses>()
    val postCourse: LiveData<Courses> = _postCourse

    /**
     * Posts a new course.
     *
     * @param courses The course data to post.
     */
    fun postCourse(courses: CourseRequest) {
        viewModelScope.launch {
            val response = parkourApi.postCourse(courses)
            if (response.isSuccessful) {
                _postCourse.postValue(response.body())
                Log.i("Response :", response.body().toString())
            } else {
                Log.i("Error :", response.message())
            }
        }
    }

    private val _postObstacleCourse = MutableLiveData<Obstacles>()
    val postObstaclesCourse: LiveData<Obstacles> = _postObstacleCourse

    /**
     * Adds an obstacle to a specific course.
     *
     * @param id The ID of the course.
     * @param obstacles The ID of the obstacle to add.
     */
    fun postObstacleToCourseById(id: Int, obstacles: Int) {
        viewModelScope.launch {
            val obstaclePost = ObstaclePost(obstacles)
            Log.i("Request Sent:", "ID: $id, Body: $obstaclePost")
            Log.i("API Request", "POST /api/course/$id/add_obstacle with body: $obstaclePost")
            val response = parkourApi.postObstacleToCourseById(id, obstaclePost)
            if (response.isSuccessful) {
                _postObstacleCourse.postValue(response.body())
                Log.i("Response :", response.body().toString())
                getObstaclesByCourseId(id)
            } else {
                Log.i("Error :", response.message())
            }
        }
    }

    private val _updateObstaclePositionCourse = MutableLiveData<Obstacles>()
    val updateObstaclePositionCourse: LiveData<Obstacles> = _updateObstaclePositionCourse

    /**
     * Updates the position of an obstacle within a course.
     *
     * @param id The ID of the course.
     * @param obstacles The obstacle data with the new position.
     */
    fun postObstacleInCertainPositionByCourseId(id: Int, obstacles: Obstacles) {
        viewModelScope.launch {
            val response = parkourApi.postObstacleInCertainPositionByCourseId(id, obstacles)
            if (response.isSuccessful) {
                _updateObstaclePositionCourse.postValue(response.body())
                Log.i("Response :", response.body().toString())
            } else {
                Log.i("Error :", response.message())
            }
        }
    }

    /**
     * Updates an existing course.
     *
     * @param id The ID of the course to update.
     * @param updatedCourse The updated course data.
     */
    fun updateCourse(id: Int, updatedCourse: Courses) {
        viewModelScope.launch {
            try {
                val response = parkourApi.putCourses(id, updatedCourse)
                if (response.isSuccessful) {
                    Log.i("Success", "Course updated: ${response.body()}")
                } else {
                    Log.e("Error", "Error updating: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Exception", "Error: ${e.message}")
            }
        }
    }

    /**
     * Deletes a course by its ID.
     *
     * @param id The ID of the course to delete.
     */
    fun deleteCourses(id: Int) {
        viewModelScope.launch {
            try {
                val response = parkourApi.deleteCourse(id)
                if (response.isSuccessful) {
                    Log.i("Success", "Course deleted: ${response.body()}")
                } else {
                    Log.e("Error", "Error deleting: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Exception", "Error: ${e.message}")
            }
        }
    }

    /**
     * Deletes an obstacle from a course.
     *
     * @param id The ID of the course.
     * @param idObstacles The ID of the obstacle to delete.
     */
    fun deleteObstacleFromCourse(id: Int, idObstacles: Int) {
        viewModelScope.launch {
            try {
                val response = parkourApi.deleteObstacleFromCourse(id, idObstacles)
                if (response.isSuccessful) {
                    Log.i("Success", "Obstacle deleted: ${response.body()}")
                    getObstaclesByCourseId(id)
                } else {
                    Log.e("Error", "Error deleting: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Exception", "Error: ${e.message}")
            }
        }
    }
}
