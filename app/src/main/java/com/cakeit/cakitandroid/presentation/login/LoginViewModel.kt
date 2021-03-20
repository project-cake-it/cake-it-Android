package com.cakeit.cakitandroid.presentation.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.cakeit.cakitandroid.base.BaseViewModel
import com.cakeit.cakitandroid.di.api.ApiClient
import com.cakeit.cakitandroid.di.api.CakeApi
import com.cakeit.cakitandroid.di.api.data.PostSocialLoginData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlin.coroutines.CoroutineContext


class LoginViewModel(application: Application) : BaseViewModel<Any?>(application) {
    val registerSuccess = MutableLiveData<Byte>() // 기본 -1(실패시 포함), 성공시 카카오, 네이버, 구글, 애플 순으로 0, 1, 2, 3

    init {
        registerSuccess.value = -1
    }

    fun sendAuthCodeToServer(authCode : String, socialType : String) {
        try {
            val response = ApiClient.provideCakeApi().postSocialLogin(PostSocialLoginData(authCode, socialType)).execute()
            Log.d(TAG, "sendAuthCode :: ${response}")
        } catch (e : Exception){
            Log.d(TAG, "sendAuthCode error ${e.message}")
//            (TAG, "sendAuthCode error ${e.message}")
        }
    }
}
