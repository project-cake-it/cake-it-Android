package com.cakeit.cakitandroid.di.api
import com.cakeit.cakitandroid.di.api.data.PostSocialLoginData
import com.cakeit.cakitandroid.di.api.responses.*
import io.reactivex.Flowable
import retrofit2.http.*

//SWAGGER : http://13.124.173.58:8080/swagger-ui.html#/
const val BASE_API_URL = "/api/v2"

interface CakeApi {
//    @GET("/api/v1/shops/{shopId}")
//    fun getShopDetail(
//        @Path("shopId") shopId : Int
//    ) : Call<GetShopDetailResponse>


    @Headers("Content-Type: application/json")
    @POST("${BASE_API_URL}/login")
    fun postSocialLogin(
            @Body body : PostSocialLoginData
    ) : Flowable<SocialLoginResponse>

    @GET("/api/v2/designs")
    fun getDesignList(
            @Query("theme") theme : String?,
            @Query("location", encoded = true) location : List<String>?,
            @Query("size", encoded = true) size : List<String>?,
            @Query("color") color : List<String>?,
            @Query("category") category : List<String>?,
            @Query("order") order : String?
    ) : Flowable<DesignListResponse>

    @GET("/api/v2/search")
    fun getKeywordSearch(
//        @Query("keyword") keyword : String?,
        @Query("name") name : String?,
        @Query("location", encoded = true) location : List<String>?,
        @Query("theme") theme : String?,
        @Query("size", encoded = true) size : List<String>?,
        @Query("color") color : List<String>?,
        @Query("category") category : List<String>?,
        @Query("order") order : String?
    ) : Flowable<KeywordSearchResponse>

    @GET("${BASE_API_URL}/shops/{shopId}")
    fun getShopDetail(
        @Path("shopId") shopId : Int
    ) : Flowable<ShopDetailResponse>

    @GET("${BASE_API_URL}/designs/{designId}")
    fun getDesignDetail(
        @Path("designId") designId : Int
    ) : Flowable<DesignDetailResponse>

    @GET("/api/v2/shops")
    fun getShopList(
            @Query("order") theme : String?,
            @Query("location", encoded = true) location : List<String>?
    ) : Flowable<ShopListResponse>
}