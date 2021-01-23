package com.declantech_softwares.ardine

import android.app.Application
import androidx.room.Room
import com.declantech_softwares.ardine.services.AppCache
import com.declantech_softwares.ardine.services.AppDatabase
import com.declantech_softwares.ardine.services.CourseAPIService
import com.declantech_softwares.ardine.types.AppContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.CoroutineContext

const val apiUrl = ""
typealias AppCoroutineScope = CoroutineScope
class App : Application(), CoroutineScope {
    val appModule = module {
        single { val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(apiUrl)
            .build()
            retrofit.create(CourseAPIService::class.java)
        }

        single {
            val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "data.db").build()
            db
        }
        single {
            this@App as AppCoroutineScope
        }
        single {
            AppContext()
        }
        single {
            AppCache()
        }
    }
    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Default
}