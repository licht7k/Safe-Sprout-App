package com.example.parentalcontrol.data.repository

import RegisterParentResponse
import com.example.parentalcontrol.data.local.TokenStore
import com.example.parentalcontrol.data.remote.api.AuthApi
import com.example.parentalcontrol.data.remote.dto.LoginParentRequest
import com.example.parentalcontrol.data.remote.dto.RegisterParentRequest
import com.example.parentalcontrol.data.remote.network.ApiClient.authApi
import com.example.parentalcontrol.data.remote.network.TokenProvider
import retrofit2.HttpException

import android.util.Log
import java.io.IOException

class AuthRepository(
    private val api: AuthApi,
    private val tokenStore: TokenStore
) {


    suspend fun loginParent(email: String, password: String): Result<String> {
        return runCatching {
            val res = api.loginParent(
                LoginParentRequest(
                    email = email,
                    password = password
                )
            )
            TokenProvider.token = res.token
            tokenStore.saveToken(res.token)
            res.token
        }
    }

    suspend fun registerParent(
        name: String,
        email: String,
        password: String
    ): Result<String> {
        return try {
            val res = api.registerParent(
                RegisterParentRequest(
                    name = name,
                    email = email,
                    password = password
                )
            )
            TokenProvider.token = res.token
            tokenStore.saveToken(res.token)
            Result.success(res.token)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Result.failure(
                Exception("HTTP ${e.code()}: ${errorBody ?: "Unknown server error"}")
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

//    suspend fun registerParent(name: String, email: String, password: String): Result<String> {
//        return try {
//            val body = api.registerParent(
//                RegisterParentRequest(
//                    name = name,
//                    email = email,
//                    password = password
//                )
//            )
//
//            val raw = body.string() // <-- reads the whole response as text
//            Log.d("REGISTER_RAW", raw)
//
//            // Return something just so app continues. We’ll fix later.
//            Result.success("RAW_LOGGED")
//
//        } catch (e: HttpException) {
//            val err = e.response()?.errorBody()?.string()
//            Log.e("REGISTER_HTTP", "HTTP ${e.code()} errorBody=$err", e)
//            Result.failure(Exception("HTTP ${e.code()} - check Logcat"))
//
//        } catch (e: IOException) {
//            Log.e("REGISTER_NET", "Network error", e)
//            Result.failure(Exception("Network error - check Logcat"))
//
//        } catch (e: Exception) {
//            Log.e("REGISTER_ERR", "Unexpected error", e)
//            Result.failure(Exception("Unexpected error - check Logcat"))
//        }
//    }




    suspend fun loadSavedTokenIntoMemory() {
        // simplest: read current value once
        val token = tokenStore.getTokenOnce()
        TokenProvider.token = token
    }

    suspend fun logout() {
        TokenProvider.token = null
        tokenStore.clearToken()
    }
}
