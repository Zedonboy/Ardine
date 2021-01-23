package com.declantech_softwares.ardine.ui.main

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.declantech_softwares.ardine.AppCoroutineScope
import com.declantech_softwares.ardine.services.AppCache
import com.declantech_softwares.ardine.services.AppDatabase
import com.declantech_softwares.ardine.services.CourseAPIService
import com.declantech_softwares.ardine.types.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val courseApiService : CourseAPIService,
                    private val scope : AppCoroutineScope,
                    private val appCntx : AppContext,
                    private val appCache : AppCache,
                    private val appDB : AppDatabase) : ViewModel() {
    val loadingCourseData = ObservableBoolean(false)
    val loadingCourseOultines = ObservableBoolean(false)
    private val _courseMatList = MutableLiveData<MutableList<CourseMaterial>>()
    private val _currentCourseOutline = MutableLiveData<List<CourseOutline>>()
    private var _pastQuestion = emptyList<Any>()

    val currentPastQuestions : List<Any>
    get() = _pastQuestion

    val courseMatList : LiveData<MutableList<CourseMaterial>>
    get() = _courseMatList

    val currentCourseOutline : LiveData<List<CourseOutline>>
    get() = _currentCourseOutline



    fun getLatestCourses(basedOn : List<String>? = null) {
       loadingCourseData.set(true)
        courseApiService.getLatestCourses(basedOn).enqueue(object : Callback<List<CourseMaterial>>{
            override fun onFailure(call: Call<List<CourseMaterial>>, t: Throwable) {
                loadingCourseData.set(false)
            }

            override fun onResponse(call: Call<List<CourseMaterial>>, response: Response<List<CourseMaterial>>) {
                loadingCourseData.set(false)
                if(response.isSuccessful){
                    val list = _courseMatList.value ?: mutableListOf()
                    list.addAll(response.body()!!)
                    _courseMatList.value = list
                }
            }

        })
    }

    fun search(searchItem : String){
        loadingCourseData.set(true)
        courseApiService.search(searchItem).enqueue(object : Callback<List<CourseMaterial>>{
            override fun onFailure(call: Call<List<CourseMaterial>>, t: Throwable) {
                loadingCourseData.set(false)
            }

            override fun onResponse(call: Call<List<CourseMaterial>>, response: Response<List<CourseMaterial>>) {
                loadingCourseData.set(false)
                if(response.isSuccessful) {
                    val list = _courseMatList.value ?: mutableListOf()
                    list.addAll(response.body()!!)
                    _courseMatList.value = list
                }
            }

        })
    }

    fun getCourseOutlines(courseId : Long){
        loadingCourseOultines.set(true)
        val list = appCache.outlineCache[courseId]
        if(list.isNullOrEmpty()){
            courseApiService.getCourseOutlines(courseId).enqueue(object : Callback<List<CourseOutline>>{
                override fun onFailure(call: Call<List<CourseOutline>>, t: Throwable) {
                    loadingCourseOultines.set(false)
                }

                override fun onResponse(call: Call<List<CourseOutline>>, response: Response<List<CourseOutline>>) {
                    loadingCourseOultines.set(false)
                    if(response.isSuccessful){
                        TODO("Sort courseoutlines according to serial no")
                        _currentCourseOutline.value = response.body()!!
                        appCache.outlineCache[courseId] = response.body()!!
                    }
                }

            })
        } else {
            _currentCourseOutline.value = list
        }

    }

    fun getCoursePastQuestions(courseId: Int) {

    }

    fun getCourseContent(outlineId : Int, cb : AppCallback<CourseContent>){

    }

    fun loginUser(cntrct : LoginContract, cb : AppCallback<LoginResponse>){

    }

    fun registerUser(cntrct: RegisterContract, cb: AppCallback<AppUser>){

    }

    @PremiumAware
    fun saveToDatabase(courseData : CourseMaterial, cb: AppCallback<CourseMaterial>){
        if(appCntx.isUserPremium.get()){
            _saveToDB(courseData)
        } else if(courseData.premium) {
            cb.onFail(Error("This is a premium course"))
        } else {
            val appdao = appDB.appDao()
            val count = appdao.countCourses()
            if (count >= 5){
                cb.onFail(Error("You have passed saved course limit"))
            } else {
                _saveToDB(courseData)
            }
        }

    }

    fun deleteCourseOutline(courseMaterial: CourseMaterial, cb : AppCallback<CourseMaterial>) {

    }

    private fun _saveToDB(courseData : CourseMaterial){
        scope.launch{
            val defer1 = async { courseApiService.getCourseOutlines(courseData.id).execute().body() ?: emptyList() }
            val defer2 = async { courseApiService.getCourseContentsByCourseMat(courseData.id).execute().body() ?: emptyList() }
            val courseOutList = defer1.await()
            val courseContentList = defer2.await()
            if(courseOutList.isNotEmpty() && courseContentList.isNotEmpty()) {
                val appdao = appDB.appDao()
                appdao.insertCourseOutline(*courseOutList.toTypedArray())
                appdao.insertCourseContent(*courseContentList.toTypedArray())
                appdao.insertCourse(courseData)
                withContext(Dispatchers.Main) {
                    courseData.saved.set(true)
                }
            }
            withContext(Dispatchers.Main){
                courseData.savingToDB.set(false)
            }
        }
    }

    fun MutableLiveData<*>.notify(){
        this.value = this.value
    }
}
