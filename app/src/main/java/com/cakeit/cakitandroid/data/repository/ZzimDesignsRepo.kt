package com.cakeit.cakitandroid.data.repository

import android.util.Log
import com.cakeit.cakitandroid.di.api.ApiClient
import com.cakeit.cakitandroid.di.api.responses.DesignListResponse
import com.cakeit.cakitandroid.di.api.responses.ShopListResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object ZzimDesignsRepo {
    val TAG = this.javaClass.simpleName

    fun getDesigns(authorization : String) : Single<DesignListResponse>? {
        return ApiClient.provideCakeApi()
            .getZzimDesigns(authorization)
            .map {
                Log.d(TAG, "getZzimDesigns status = " + it.status)
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