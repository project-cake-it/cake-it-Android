package com.cakeit.cakitandroid.data.repository

import android.util.Log
import com.cakeit.cakitandroid.data.source.remote.entity.SocialLoginResponseEntity
import com.cakeit.cakitandroid.di.api.ApiClient
import com.cakeit.cakitandroid.di.api.data.PostSocialLoginData
import com.cakeit.cakitandroid.domain.model.SocialLoginResponseModel
import com.cakeit.cakitandroid.domain.usecase.SocialLoginUsecase

object SocialLoginRepo {
    val TAG = this.javaClass.simpleName

    fun sendAuthCode(authCode : String, socialType : String) : SocialLoginResponseModel{
        val postData = PostSocialLoginData(authCode, socialType)
        Log.d(SocialLoginUsecase.TAG, "3 $authCode, $socialType, $postData")
        val response = ApiClient.provideCakeApi()
            .postSocialLogin(postData).execute()
        Log.d(SocialLoginUsecase.TAG, "4")
        if(response.isSuccessful){
            var datas = response.body()

            Log.d(TAG, datas.toString())

            return SocialLoginResponseModel("회원가입 성공입니다", "testToken")
        } else {
            throw Exception(response.toString())
        }
    }
}