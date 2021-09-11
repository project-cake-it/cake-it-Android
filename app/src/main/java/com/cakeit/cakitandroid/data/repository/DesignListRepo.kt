package com.cakeit.cakitandroid.data.repository

import android.util.Log
import com.cakeit.cakitandroid.di.api.ApiClient
import com.cakeit.cakitandroid.di.api.responses.DesignListResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object DesignListRepo {
    val TAG = this.javaClass.simpleName
    fun sendParams(theme : String?, locList : ArrayList<String>, sizeList : ArrayList<String>,
                   colorList : ArrayList<String>, categoryList : ArrayList<String>
                   , order : String?) : Single<DesignListResponse>? {

        return ApiClient.provideCakeApi()
            .getDesignList(theme, locList, sizeList, colorList, categoryList, order)
            .map {
                Log.d(TAG, "getDesignList status = " + it.status)
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