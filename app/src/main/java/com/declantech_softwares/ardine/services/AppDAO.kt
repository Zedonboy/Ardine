package com.declantech_softwares.ardine.services

import androidx.room.*
import com.declantech_softwares.ardine.types.CourseContent
import com.declantech_softwares.ardine.types.CourseMaterial
import com.declantech_softwares.ardine.types.CourseOutline


@Dao
interface AppDAO {
    // Course Material
    @Query("SELECT * FROM coursematerial")
    suspend fun loadAllCourses() : List<CourseMaterial>
    @Query("SELECT COUNT(*) FROM coursematerial")
    fun countCourses() : Int
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(vararg course : CourseMaterial)
    @Update
    suspend fun updateCourseMat(vararg course: CourseMaterial)
    @Delete
    suspend fun deleteCourse(vararg course: CourseMaterial)

    //Course Outline
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourseOutline(vararg outline : CourseOutline)
    @Query("SELECT * FROM courseoutline")
    suspend fun loadAllCourseOulines() : List<CourseOutline>

    @Query("SELECT * FROM courseoutline WHERE 'courseId' > :id")
    fun loadOutlinesByCourseId(id : Long) : List<CourseOutline>
    @Delete
    suspend fun deleteCourseOutline(vararg outline: CourseOutline)
    @Update
    suspend fun updateCourseOutline(vararg outline: CourseOutline)

    //Course Content
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourseContent(vararg cntent : CourseContent)

    @Query("SELECT * FROM coursecontent")
    suspend fun loadAllCourseContents() : List<CourseContent>

    @Update
    suspend fun updateCourseContent(vararg cntent : CourseContent)

    @Delete
    suspend fun deleteCourseContent(vararg cntent : CourseContent)
}