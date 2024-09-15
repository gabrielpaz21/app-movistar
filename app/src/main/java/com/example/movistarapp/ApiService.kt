package com.example.movistarapp

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    //I understand that another route was wanted but this one seemed simpler to me.
    @GET("users")
    fun getUsers(): Call<List<ResponseDTO>>
}