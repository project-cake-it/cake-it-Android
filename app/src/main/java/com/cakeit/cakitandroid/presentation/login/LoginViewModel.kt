package com.cakeit.cakitandroid.presentation.login

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings.Global.getString
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseViewModel
import com.cakeit.cakitandroid.domain.usecase.SocialLoginUseCase
import com.kakao.sdk.auth.model.OAuthToken


class LoginViewModel(application: Application) : BaseViewModel<Any?>(application) {
    var registerMessage = "Reset livedata"
    val registerState = MutableLiveData<Boolean>(false)
    var accessToken = MutableLiveData<String>()
    var currentApplication = application
//    val toastMessage = MutableLiveData<String>() // TODO(view에서 showToast하기 위한 live data. 이거 base에 넣는게 낫지 않나?)

    fun sendGoogleCodeToServer(authCode: String) {

        Log.e(TAG, "${authCode},")

        sendAuthCodeToServer(authCode, "GOOGLE")
    }

    fun sendKakaoCodeToServer(oAuthToken: OAuthToken) {
        val authCode = oAuthToken.accessToken
        sendAuthCodeToServer(authCode, "KAKAO")
    }

    fun sendNaverCodeToServer(authCode: String) {
        sendAuthCodeToServer(authCode, "NAVER")
    }

    fun sendAuthCodeToServer(authCode : String, socialType : String) {
        registerMessage = "Reset livedata"
        registerState.value = false

        SocialLoginUseCase.execute(
            SocialLoginUseCase.Request(authCode, socialType),
            onSuccess = {
                accessToken.value = it.accessToken
                registerMessage = it.message
                registerState.value =
                    it.message == "회원가입 성공입니다" || it.message == "로그인 성공입니다"

                val sharedPref = currentApplication.applicationContext.getSharedPreferences("userAccount", Context.MODE_PRIVATE)
                with (sharedPref.edit()) {
                    putString("accessToken", accessToken.value)
                    putString("socialType", socialType)
                    apply()
                }
            },
            onError = {
                registerMessage = it.message.toString()
                Log.d(TAG, "onFailure, $registerMessage")
                it.printStackTrace()
                registerState.value = false
            },
            onFinished = {
                Log.d(TAG, "Finished")
            }
        )
    }
}

