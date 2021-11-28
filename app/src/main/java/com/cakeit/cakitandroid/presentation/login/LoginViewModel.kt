package com.cakeit.cakitandroid.presentation.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.cakeit.cakitandroid.base.BaseViewModel
import com.cakeit.cakitandroid.data.source.local.prefs.SharedPreferenceController
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
                registerMessage = "로그인 되었습니다."
                registerState.value =
                    it.message == "회원가입 성공입니다" || it.message == "로그인 성공입니다"

                SharedPreferenceController.setSocialType(currentApplication.applicationContext, socialType)
            },
            onError = {
                registerMessage = "로그인에 실패하였습니다."
                Log.d(TAG, "onFailure, $it.message.toString()")
                it.printStackTrace()
                registerState.value = false
            },
            onFinished = {
                Log.d(TAG, "Finished")
            }
        )
    }
}

