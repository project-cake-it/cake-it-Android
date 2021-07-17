package com.cakeit.cakitandroid.presentation.shop

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cakeit.cakitandroid.base.BaseViewModel
import com.cakeit.cakitandroid.data.source.local.entity.CakeDesignData
import com.cakeit.cakitandroid.di.api.responses.ShopDetailData
import com.cakeit.cakitandroid.di.api.responses.ShopDetailResponseData
import com.cakeit.cakitandroid.domain.usecase.ShopDetailUseCase
import com.cakeit.cakitandroid.domain.usecase.ZzimShopDelUseCase
import com.cakeit.cakitandroid.domain.usecase.ZzimShopRegUseCase
import com.google.android.material.tabs.TabLayout

class ShopDetailViewModel(application: Application,
                    fm: FragmentManager,
                    viewPagerOnTabSelectedListener: TabLayout.ViewPagerOnTabSelectedListener,
                    tabLayoutOnPageChangeListener: TabLayout.TabLayoutOnPageChangeListener) : BaseViewModel<Any?>(application) {

    private val viewPagerAdapter: FragmentStatePagerAdapter
    private val viewPagerOnTabSelectedListener: TabLayout.ViewPagerOnTabSelectedListener
    private val tabLayoutOnPageChangeListener: TabLayout.TabLayoutOnPageChangeListener

    private val  _shopDetailData = MutableLiveData<ShopDetailData>()
    val shopDetailData : LiveData<ShopDetailData> get() = _shopDetailData

    private val  _zzim = MutableLiveData<Boolean>()
    val zzim : LiveData<Boolean> get() = _zzim

    class Factory(val application: Application, val fm: FragmentManager, val viewPagerOnTabSelectedListener : TabLayout.ViewPagerOnTabSelectedListener
                  , val tabLayoutOnPageChangeListener : TabLayout.TabLayoutOnPageChangeListener
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ShopDetailViewModel(application, fm, viewPagerOnTabSelectedListener, tabLayoutOnPageChangeListener) as T
        }
    }

    init {
        this.viewPagerOnTabSelectedListener = viewPagerOnTabSelectedListener
        this.tabLayoutOnPageChangeListener = tabLayoutOnPageChangeListener
        viewPagerAdapter = ContentsPagerAdapter(fm, 2)
    }

    fun clickZzimBtn(shopId : Int, zzim : Boolean) {
        Log.d("songjem", "zzim, shopId = " + shopId + ", zzim = " + zzim)
        // 찜되어있는 가게일 경우
        if(zzim) {
            // 찜 해제
            ZzimShopDelUseCase.execute(
                ZzimShopDelUseCase.Request(shopId),
                onSuccess = {
                    Log.d("songjem", "찜 해제 성공")
                    _zzim.value = false
                    Log.d("songjem", "Zzim Shop Delete onSuccess")
                },
                onError = {
                    Log.d("songjem", "Zzim Shop Delete onError")
                },
                onFinished = {
                    Log.d("songjem", "Zzim Shop Delete Finished")
                }
            )
        }
        else {
            // 찜 등록
            ZzimShopRegUseCase.execute(
                ZzimShopRegUseCase.Request(shopId),
                onSuccess = {
                    Log.d("songjem", "찜 등록 성공")
                    _zzim.value = true
                    Log.d("songjem", "Zzim Shop Register onSuccess")
                },
                onError = {
                    Log.d("songjem", "Zzim Shop Register onError")
                },
                onFinished = {
                    Log.d("songjem", "Zzim Shop Register Finished")
                }
            )
        }
    }

    fun sendShopIdForShopDetail(shopId : Int) {
        ShopDetailUseCase.execute(
            ShopDetailUseCase.Request(shopId),
            onSuccess = {
                var shopData : ShopDetailResponseData  = it.data
                _shopDetailData.value = shopData.shop
                Log.d("nulkong", "ShopDetail Network onSuccess")
            },
            onError = {
                Log.d("nulkong", "ShopDetail Network onError")
            },
            onFinished = {
                Log.d("nulkong", "ShopDetail Network Finished")
            }
        )
    }
}