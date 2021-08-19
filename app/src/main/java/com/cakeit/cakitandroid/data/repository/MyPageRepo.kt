package com.cakeit.cakitandroid.data.repository

import android.util.Log
import com.cakeit.cakitandroid.di.api.ApiClient
import com.cakeit.cakitandroid.di.api.responses.AnnouncementListResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object MyPageRepo {

    fun getAnnouncementList() : Single<AnnouncementListResponse>? {
//        val postData = PostSocialLoginData(authCode, socialType)
        Log.e("sungmin", "getAnnouncementList")

        return ApiClient.provideCakeApi()
            .getAnnouncementList()
            .map {
                if (it.status.div(100) == 2) {
                    it
                } else {
                    Log.e("getAnnouncementListR", "error ${it}")
                    throw Exception(it.toString())
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .singleOrError()
    }
}