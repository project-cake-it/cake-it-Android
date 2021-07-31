package com.cakeit.cakitandroid.presentation.design

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cakeit.cakitandroid.base.BaseViewModel
import com.cakeit.cakitandroid.di.api.responses.DesignDetailData
import com.cakeit.cakitandroid.di.api.responses.DesignDetailResponseData
import com.cakeit.cakitandroid.domain.usecase.*

class DesignDetailViewModel(application: Application) : BaseViewModel<Any?>(application) {
    private val  _designDetailData = MutableLiveData<DesignDetailData>()
    val designDetailData : LiveData<DesignDetailData> get() = _designDetailData

    private val  _zzim = MutableLiveData<Boolean>()
    val zzim : LiveData<Boolean> get() = _zzim

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

    fun clickZzimBtn(shopId : Int, zzim : Boolean) {
        Log.d("songjem", "zzim, shopId = " + shopId + ", zzim = " + zzim)
        // 찜되어있는 디자인일 경우
        if(zzim) {
            // 찜 해제
            ZzimDesignDelUseCase.execute(
                ZzimDesignDelUseCase.Request(shopId),
                onSuccess = {
                    _zzim.value = false
                    Log.d("songjem", "Zzim Design Delete onSuccess")
                },
                onError = {
                    Log.d("songjem", "Zzim Design Delete onError")
                },
                onFinished = {
                    Log.d("songjem", "Zzim Design Delete Finished")
                }
            )
        }
        else {
            // 찜 등록
            ZzimDesignRegUseCase.execute(
                ZzimDesignRegUseCase.Request(shopId),
                onSuccess = {
                    Log.d("songjem", "찜 등록 성공")
                    _zzim.value = true
                    Log.d("songjem", "Zzim Design Register onSuccess")
                },
                onError = {
                    Log.d("songjem", "Zzim Design Register onError")
                },
                onFinished = {
                    Log.d("songjem", "Zzim Design Register Finished")
                }
            )
        }
    }
}