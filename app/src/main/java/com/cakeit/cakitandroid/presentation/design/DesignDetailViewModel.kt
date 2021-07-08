package com.cakeit.cakitandroid.presentation.design

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cakeit.cakitandroid.base.BaseViewModel
import com.cakeit.cakitandroid.di.api.responses.DesignDetailData
import com.cakeit.cakitandroid.di.api.responses.DesignDetailResponseData
import com.cakeit.cakitandroid.domain.usecase.DesignDetailUseCase

class DesignDetailViewModel(application: Application) : BaseViewModel<Any?>(application) {
    private val  _designDetailData = MutableLiveData<DesignDetailData>()
    val designDetailData : LiveData<DesignDetailData> get() = _designDetailData

    fun sendDesignIdForDesignDetail(designId : Int) {
        DesignDetailUseCase.execute(
            DesignDetailUseCase.Request(designId),
            onSuccess = {
                var designData : DesignDetailResponseData = it.data
                _designDetailData.value = designData.design
                Log.d("nulkong", "DesignDetail Network onSuccess")
            },
            onError = {
                Log.d("nulkong", "DesignDetail Network onError")
            },
            onFinished = {
                Log.d("nulkong", "DesignDetail Network Finished")
            }
        )
    }
}