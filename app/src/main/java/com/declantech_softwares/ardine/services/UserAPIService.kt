package com.declantech_softwares.ardine.services

import com.declantech_softwares.ardine.types.LoginResponse
import retrofit2.Call
import retrofit2.http.POST

interface UserAPIService {
    @POST()
    fun loginUser(email : String, pass : String) : Call<LoginResponse>
}