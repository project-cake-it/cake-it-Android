package com.cakeit.cakitandroid.data.repository

import android.util.Log
import com.cakeit.cakitandroid.di.api.ApiClient
import com.cakeit.cakitandroid.di.api.responses.ZzimResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object ZzimDesignRegRepo {
    val TAG = this.javaClass.simpleName
    fun sendDesignId(authorization : String, designId : Int) : Single<ZzimResponse>? {

        return ApiClient.provideCakeApi()
            .postDesignZzim(authorization, designId)
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