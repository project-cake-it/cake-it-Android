package com.cakeit.cakitandroid.di.api

import com.cakeit.cakitandroid.di.api.data.PostSocialLoginData
import com.cakeit.cakitandroid.di.api.responses.PostSocialLoginResponse
import retrofit2.Call
import retrofit2.http.*

//SWAGGER : http://13.124.173.58:8080/swagger-ui.html#/
const val BASE_API_URL = "/api/v2"

interface CakeApi {
    @Headers("Content-Type: application/json")
    @POST("${BASE_API_URL}/login")
    fun postSocialLogin(
            @Body body : PostSocialLoginData
    ) : Call<PostSocialLoginResponse>
}