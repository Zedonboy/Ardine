package com.declantech_softwares.ardine.services

import android.util.LruCache
import com.declantech_softwares.ardine.types.CourseContent
import com.declantech_softwares.ardine.types.CourseMaterial
import com.declantech_softwares.ardine.types.CourseOutline
import java.util.*

class AppCache {
    val courseMatCache = WeakHashMap<Long, CourseMaterial>()
    val outlineCache = WeakHashMap<Long, List<CourseOutline>>()
    val contentCache = WeakHashMap<Long, CourseContent>()
}