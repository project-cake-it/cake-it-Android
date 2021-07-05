package com.cakeit.cakitandroid.presentation.shop

import android.app.Application
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cakeit.cakitandroid.base.BaseViewModel
import com.cakeit.cakitandroid.di.api.responses.ShopDetailData
import com.cakeit.cakitandroid.di.api.responses.ShopDetailResponseData
import com.cakeit.cakitandroid.domain.usecase.ShopDetailUseCase
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