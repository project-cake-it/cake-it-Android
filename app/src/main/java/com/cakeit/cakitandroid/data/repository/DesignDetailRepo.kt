package com.cakeit.cakitandroid.data.repository

import android.util.Log
import com.cakeit.cakitandroid.di.api.ApiClient
import com.cakeit.cakitandroid.di.api.responses.DesignDetailResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object DesignDetailRepo {
    val TAG = this.javaClass.simpleName
    fun sendDesignId(designId : Int) : Single<DesignDetailResponse>? {

        return ApiClient.provideCakeApi()
            .getDesignDetail(designId)
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