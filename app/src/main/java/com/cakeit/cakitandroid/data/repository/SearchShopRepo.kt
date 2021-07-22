package com.cakeit.cakitandroid.data.repository

import android.util.Log
import com.cakeit.cakitandroid.di.api.ApiClient
import com.cakeit.cakitandroid.di.api.responses.KeywordSearchResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object SearchShopRepo {
    val TAG = this.javaClass.simpleName
    fun sendParams(keyword : String, name : String, locList : ArrayList<String>, theme : String?, sizeList : ArrayList<String>,
                   colorList : ArrayList<String>, categoryList : ArrayList<String>
                   , order : String) : Single<KeywordSearchResponse>? {

        return ApiClient.provideCakeApi()
            .getKeywordSearch(name, locList, theme, sizeList, colorList, categoryList, order)
            .map {
                Log.d(TAG, "getSearchShop status = " + it.status)
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