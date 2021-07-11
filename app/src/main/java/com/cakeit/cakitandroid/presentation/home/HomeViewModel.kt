package com.cakeit.cakitandroid.presentation.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cakeit.cakitandroid.base.BaseViewModel
import com.cakeit.cakitandroid.di.api.responses.PromotionResponseData
import com.cakeit.cakitandroid.domain.usecase.PromotionUseCase

class HomeViewModel (application: Application) : BaseViewModel<Any?>(application) {

    private val  _homeData = MutableLiveData<ArrayList<PromotionResponseData>>()
    val homeData : LiveData<ArrayList<PromotionResponseData>> get() = _homeData

    fun getPromotion() {
        PromotionUseCase.execute(
            onSuccess = {
                var homeData : ArrayList<PromotionResponseData> = it.data
                _homeData.value = homeData
                Log.d("nulkong", "Home Network onSuccess")
            },
            onError = {
                Log.d("nulkong", "Home Network onError")
            },
            onFinished = {
                Log.d("nulkong", "Home Network Finished")
            }
        )
    }
}