package com.cakeit.cakitandroid.presentation.login

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.cakeit.cakitandroid.base.BaseViewModel
import com.cakeit.cakitandroid.domain.usecase.SocialLoginUseCase
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.kakao.sdk.auth.model.OAuthToken
import com.nhn.android.naverlogin.OAuthLogin
import java.lang.Error
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import org.json.JSONException

import android.widget.Toast

import org.json.JSONObject

import android.os.AsyncTask





class LoginViewModel(application: Application) : BaseViewModel<Any?>(application) {
    var registerMessage = "Reset livedata"
    val registerState = MutableLiveData<Boolean>(false)

//    val toastMessage = MutableLiveData<String>() // TODO(view에서 showToast하기 위한 live data. 이거 base에 넣는게 낫지 않나?)

    fun sendGoogleCodeToServer(account: GoogleSignInAccount?) {

        Log.e(TAG, "${account!!.id},${account!!.idToken},${account!!.account},${account!!.displayName},${account!!.serverAuthCode},")

        val authCode = account?.id!!

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
                registerMessage = it.message
                registerState.value =
                    it.message == "회원가입 성공입니다" || it.message == "로그인 성공입니다"
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

