package com.cakeit.cakitandroid.presentation.login

import android.app.Application
import android.util.Log
import androidx.annotation.UiThread
import androidx.lifecycle.MutableLiveData
import com.cakeit.cakitandroid.base.BaseViewModel
import com.cakeit.cakitandroid.domain.usecase.SocialLoginUsecase
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.kakao.sdk.auth.model.OAuthToken
import kotlinx.coroutines.*


class LoginViewModel(application: Application) : BaseViewModel<Any?>(application) {
    var registerMessage = "Reset livedata"
    val registerState = MutableLiveData<Boolean>(false)

//    val toastMessage = MutableLiveData<String>() // TODO(view에서 showToast하기 위한 live data. 이거 base에 넣는게 낫지 않나?)

    fun sendGoogleCodeToServer(account: GoogleSignInAccount?) {
        val authCode = account?.idToken!!
        sendAuthCodeToServer(authCode, "GOOGLE")
    }

    fun sendKakaoCodeToServer(oAuthToken: OAuthToken) {
        val authCode = oAuthToken.accessToken
        sendAuthCodeToServer(authCode, "KAKAO")
    }

    fun sendAuthCodeToServer(authCode : String, socialType : String) {
        registerMessage = "Reset livedata"
        registerState.value = false

        SocialLoginUsecase.run(
                onSuccess = {
                    registerMessage = it.message
                    registerState.value =
                            it.message == "회원가입 성공입니다" || it.message == "로그인 성공입니다"
                },
                onFailure = {
                    registerMessage = it.message.toString()
                    Log.d(TAG, "onFailure, $registerMessage")
                    it.printStackTrace()
                    registerState.value = false
                },
                onFinished = {
                    Log.d(TAG, "Finished")
                },
                args = *arrayOf(authCode, socialType)
        )
    }
}
