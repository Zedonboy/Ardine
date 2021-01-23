package com.declantech_softwares.ardine.services

import androidx.room.Database
import androidx.room.RoomDatabase
import com.declantech_softwares.ardine.types.CourseContent
import com.declantech_softwares.ardine.types.CourseMaterial
import com.declantech_softwares.ardine.types.CourseOutline

@Database(entities = [CourseMaterial::class, CourseContent::class, CourseOutline::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao() : AppDAO
}