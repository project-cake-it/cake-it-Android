package com.cakeit.cakitandroid.di.api

import com.cakeit.cakitandroid.di.api.responses.PostSocialLoginResponse
import retrofit2.Call
import retrofit2.http.*

//SWAGGER : http://13.124.173.58:8080/swagger-ui.html#/
const val BASE_API_URL = "/api/v2"

interface CakeApi {
    @POST("${BASE_API_URL}/login")
    fun postSocialLogin(
            @Field("code") code : String,
            @Field("socialType") socialType : String
    ) : Call<PostSocialLoginResponse>
}