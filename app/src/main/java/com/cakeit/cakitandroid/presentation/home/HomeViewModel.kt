package com.cakeit.cakitandroid.presentation.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cakeit.cakitandroid.base.BaseViewModel
import com.cakeit.cakitandroid.di.api.responses.PromotionResponseData
import com.cakeit.cakitandroid.domain.usecase.PromotionUseCase

class HomeViewModel (application: Application) : BaseViewModel<Any?>(application) {

    private val  _promotionsData = MutableLiveData<ArrayList<PromotionResponseData>>()
    val promotionsData : LiveData<ArrayList<PromotionResponseData>> get() = _promotionsData

    fun getPromotion() {
        PromotionUseCase.execute(
            onSuccess = {
                var promotionsData : ArrayList<PromotionResponseData> = it.data
                _promotionsData.value = promotionsData
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