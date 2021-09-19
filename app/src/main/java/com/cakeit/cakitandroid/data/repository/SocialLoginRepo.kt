package com.cakeit.cakitandroid.data.repository

import android.util.Log
import com.cakeit.cakitandroid.di.api.ApiClient
import com.cakeit.cakitandroid.di.api.data.PostSocialLoginData
import com.cakeit.cakitandroid.di.api.responses.SocialLoginResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object SocialLoginRepo {
    val TAG = this.javaClass.simpleName
    fun sendAuthCode(authCode : String, socialType : String) : Single<SocialLoginResponse>? {
        val postData = PostSocialLoginData(authCode, socialType)
        Log.d(TAG, "3 $authCode, $socialType, $postData")

        return ApiClient.provideCakeApi()
            .postSocialLogin(postData)
            .map {
                if (it.status.div(100) == 2) {
                    it
                } else {
                    Log.e(TAG, "error) ${it}")
                    throw Exception(it.toString())
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .singleOrError()
    }
}