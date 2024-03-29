package com.cakeit.cakitandroid.di.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    // Cake It BaseURL
    var BASE_URL = "http://cake-it.shop:8080"
    private var retrofit: Retrofit? = null

    fun provideCakeApi(): CakeApi = getRetrofit().create(CakeApi::class.java)

    fun getRetrofit(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(createOkHttpClient())
                .build()
        }
        return retrofit!!
    }
}

private fun createOkHttpClient(): OkHttpClient {
    val builder = OkHttpClient.Builder()
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY
    builder.addInterceptor(interceptor)
    return builder.build()
}