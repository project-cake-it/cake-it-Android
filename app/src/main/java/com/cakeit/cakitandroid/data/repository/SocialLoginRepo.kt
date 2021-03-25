package com.cakeit.cakitandroid.data.repository

import android.util.Log
import com.cakeit.cakitandroid.data.source.remote.entity.SocialLoginResponseEntity
import com.cakeit.cakitandroid.di.api.ApiClient
import com.cakeit.cakitandroid.di.api.CakeItServerResponse
import com.cakeit.cakitandroid.di.api.data.PostSocialLoginData
import com.cakeit.cakitandroid.domain.model.SocialLoginResponseModel
import com.cakeit.cakitandroid.domain.usecase.SocialLoginUsecase
import retrofit2.Response

object SocialLoginRepo {
    val TAG = this.javaClass.simpleName
    fun sendAuthCode(authCode : String, socialType : String) : SocialLoginResponseModel{
        val postData = PostSocialLoginData(authCode, socialType)
        Log.d(SocialLoginUsecase.TAG, "3 $authCode, $socialType, $postData")

        val response = ApiClient.provideCakeApi()
                .postSocialLogin(postData).execute()

        //서버에서 무조건 status를 200으로 주기때문에 is Successful은 그냥 먹고 들어간다
        //내부 status code로 ㄲ
       if(response.isSuccessful && (response.body()?.status?.div(100)) == 2){
            Log.d(SocialLoginUsecase.TAG, "2")
            var datas = response.body()

            Log.d(TAG, datas.toString())

           //TODO : Data Repo -> Model Mapping

            return SocialLoginResponseModel("회원가입 성공입니다", "testToken")
        } else {
            Log.d(TAG, response.body().toString())
            throw Exception(response.body().toString())
        }
    }
}