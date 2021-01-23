package com.declantech_softwares.ardine.services

import com.declantech_softwares.ardine.types.CourseContent
import com.declantech_softwares.ardine.types.CourseMaterial
import com.declantech_softwares.ardine.types.CourseOutline
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CourseAPIService {
    @GET("/course-material")
    fun search(@Query("search_term") searchTerm : String) : Call<List<CourseMaterial>>

    fun getCourseOutlines(courseMatID: Long) : Call<List<CourseOutline>>

    @GET("/course-material")
    fun  getLatestCourses(@Query("basedOn")basedOn : List<String>? = null) : Call<List<CourseMaterial>>
    
    fun getCourseContentsByCourseMat(courseMatID : Long) : Call<List<CourseContent>>
}