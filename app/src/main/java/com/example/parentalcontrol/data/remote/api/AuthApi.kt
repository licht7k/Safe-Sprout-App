package com.example.parentalcontrol.data.remote.api

import RegisterParentResponse
import com.example.parentalcontrol.data.remote.dto.AuthResponse
import com.example.parentalcontrol.data.remote.dto.LoginParentRequest
import com.example.parentalcontrol.data.remote.dto.MeResponse
import com.example.parentalcontrol.data.remote.dto.RegisterParentRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import okhttp3.ResponseBody
interface AuthApi {

    @POST("parent/register")
    suspend fun registerParent(@Body body: RegisterParentRequest): RegisterParentResponse

//    @POST("api/parent/register")
//    suspend fun registerParent(@Body body: RegisterParentRequest): ResponseBody


    @POST("parent/login")
    suspend fun loginParent(@Body body: LoginParentRequest): AuthResponse

    @GET("me")
    suspend fun me(@Header("Authorization") bearer: String): MeResponse
}

