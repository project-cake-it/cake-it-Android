package com.cakeit.cakitandroid.di.api
import com.cakeit.cakitandroid.di.api.data.PostSocialLoginData
import com.cakeit.cakitandroid.di.api.responses.DesignDetailResponse
import com.cakeit.cakitandroid.di.api.responses.ShopDetailResponse
import com.cakeit.cakitandroid.di.api.responses.SocialLoginResponse
import io.reactivex.Flowable
import retrofit2.http.*

//SWAGGER : http://13.124.173.58:8080/swagger-ui.html#/
const val BASE_API_URL = "/api/v2"

interface CakeApi {
    @Headers("Content-Type: application/json")
    @POST("${BASE_API_URL}/login")
    fun postSocialLogin(
            @Body body : PostSocialLoginData
    ) : Flowable<SocialLoginResponse>

    @GET("${BASE_API_URL}/shops/{shopId}")
    fun getShopDetail(
        @Path("shopId") shopId : Int
    ) : Flowable<ShopDetailResponse>

    @GET("${BASE_API_URL}/designs/{designId}")
    fun getDesignDetail(
        @Path("designId") designId : Int
    ) : Flowable<DesignDetailResponse>
}