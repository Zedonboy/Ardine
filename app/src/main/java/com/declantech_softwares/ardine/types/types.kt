package com.declantech_softwares.ardine.types

import androidx.databinding.ObservableBoolean
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CourseMaterial(@PrimaryKey val id : Long, val lat : Double, val lon : Double,
                          val name : String, val desc : String, val premium : Boolean = false){
    val saved = ObservableBoolean(false)
    val savingToDB = ObservableBoolean(false)
}

@Entity
data class CourseOutline(@PrimaryKey val id : Long, val name : String, val desc : String)
@Entity
data class CourseContent(@PrimaryKey val id: Long, val content : String, val title : String?)
data class LoginResponse(val jwt : String, val user : AppUser)
data class LoginContract(var identifier : String? = null, var password : String? = null )
fun LoginContract.valid() : Boolean{
    return !(this.identifier.isNullOrEmpty() || this.password.isNullOrEmpty())
}
data class RegisterContract(var name : String? = null,var identifier: String? = null, var password: String? = null)
fun RegisterContract.valid() : Boolean{
    return !(this.name.isNullOrEmpty() || this.identifier.isNullOrEmpty() || this.password.isNullOrEmpty())
}

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class PremiumAware