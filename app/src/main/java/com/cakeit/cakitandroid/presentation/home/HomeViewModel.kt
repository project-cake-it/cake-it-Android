package com.cakeit.cakitandroid.presentation.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cakeit.cakitandroid.base.BaseViewModel
import com.cakeit.cakitandroid.di.api.responses.DesignListResponseData
import com.cakeit.cakitandroid.di.api.responses.PromotionResponseData
import com.cakeit.cakitandroid.domain.usecase.PopularCakeUseCase
import com.cakeit.cakitandroid.domain.usecase.PromotionUseCase

class HomeViewModel (application: Application) : BaseViewModel<Any?>(application) {

    private val  _promotionsData = MutableLiveData<ArrayList<PromotionResponseData>>()
    private val  _popularCakeData = MutableLiveData<ArrayList<DesignListResponseData>>()
    val promotionsData : LiveData<ArrayList<PromotionResponseData>> get() = _promotionsData
    val popularCakeData : LiveData<ArrayList<DesignListResponseData>> get() = _popularCakeData

    fun getPromotion() {
        PromotionUseCase.execute(
            onSuccess = {
                var promotionsData : ArrayList<PromotionResponseData> = it.data
                _promotionsData.value = promotionsData
                Log.d("nulkong", "Promotion Network onSuccess")
            },
            onError = {
                Log.d("nulkong", "Promotion Network onError")
            },
            onFinished = {
                Log.d("nulkong", "Promotion Network Finished")
            }
        )
    }

    fun getPopularCake() {
        PopularCakeUseCase.execute(
            PopularCakeUseCase.Request("best"),
            onSuccess = {
                var popularCakeData : ArrayList<DesignListResponseData> = it.data
                _popularCakeData.value = popularCakeData
                Log.d("nulkong", "PopularCakeList Network onSuccess")
            },
            onError = {
                Log.d("nulkong", "PopularCakeList Network onError")
            },
            onFinished = {
                Log.d("nulkong", "PopularCakeList Network Finished")
            }
        )
    }
}