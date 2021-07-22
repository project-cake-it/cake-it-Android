package com.cakeit.cakitandroid.data.repository

import android.util.Log
import com.cakeit.cakitandroid.di.api.ApiClient
import com.cakeit.cakitandroid.di.api.data.PostSocialLoginData
import com.cakeit.cakitandroid.di.api.responses.ShopDetailResponse
import com.cakeit.cakitandroid.di.api.responses.SocialLoginResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object ShopDetailRepo {
    val TAG = this.javaClass.simpleName
    fun sendShopId(shopId : Int) : Single<ShopDetailResponse>? {

        return ApiClient.provideCakeApi()
            .getShopDetail(shopId)
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