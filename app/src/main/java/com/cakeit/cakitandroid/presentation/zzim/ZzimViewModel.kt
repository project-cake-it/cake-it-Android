package com.cakeit.cakitandroid.presentation.zzim

import android.app.Application
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cakeit.cakitandroid.base.BaseViewModel
import com.cakeit.cakitandroid.data.source.remote.entity.CakeShopData
import com.cakeit.cakitandroid.di.api.responses.DesignDetailData
import com.cakeit.cakitandroid.domain.usecase.SearchDesignUseCase
import com.cakeit.cakitandroid.domain.usecase.ZzimDesignsUseCase
import com.cakeit.cakitandroid.domain.usecase.ZzimShopListUseCase
import com.google.android.material.tabs.TabLayout

class ZzimViewModel(application: Application,
                    fm: FragmentManager,
                    viewPagerOnTabSelectedListener: TabLayout.ViewPagerOnTabSelectedListener,
                    tabLayoutOnPageChangeListener: TabLayout.TabLayoutOnPageChangeListener) : BaseViewModel<Any?>(application) {

    private val viewPagerAdapter: FragmentStatePagerAdapter
    private val viewPagerOnTabSelectedListener: TabLayout.ViewPagerOnTabSelectedListener
    private val tabLayoutOnPageChangeListener: TabLayout.TabLayoutOnPageChangeListener

    private val _cakeShopItems = MutableLiveData<ArrayList<CakeShopData>>()
    private val _designDatas = MutableLiveData<ArrayList<DesignDetailData>>()
    val cakeShopItems : LiveData<ArrayList<CakeShopData>> get() = _cakeShopItems
    val desigDatas : LiveData<ArrayList<DesignDetailData>> get() = _designDatas

    class Factory(
        val application: Application, val fm: FragmentManager, val viewPagerOnTabSelectedListener: TabLayout.ViewPagerOnTabSelectedListener
        , val tabLayoutOnPageChangeListener: TabLayout.TabLayoutOnPageChangeListener
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return ZzimViewModel(application, fm, viewPagerOnTabSelectedListener, tabLayoutOnPageChangeListener) as T
        }
    }

    init {
        this.viewPagerOnTabSelectedListener = viewPagerOnTabSelectedListener
        this.tabLayoutOnPageChangeListener = tabLayoutOnPageChangeListener
        viewPagerAdapter = ZzimContentsPagerAdapter(fm, 2)
    }

    fun sendParamsForZzimShopList(authorization : String) {
        ZzimShopListUseCase.execute(
            ZzimShopListUseCase.Request("", authorization),
            onSuccess = {
                Log.d("songjem", "zzimShop onSuccess")
                var cakeShops = ArrayList<CakeShopData>()
                for(i in 0 .. it.data.size-1 ) {
                    cakeShops.add(CakeShopData(it.data[i].id, it.data[i].name, it.data[i].address, it.data[i].shopImages, it.data[i].hashtags!!, it.data[i].sizes!!))
                }
                _cakeShopItems.value = cakeShops

            },
            onError = {
                Log.d("songjem", "zzimShop onError")
            },
            onFinished = {
                Log.d("songjem", "zzimShop Finished")
            }
        )
    }

    fun getZzimDesign(authorization : String)
    {
        ZzimDesignsUseCase.execute(
            ZzimDesignsUseCase.Request(authorization),
            onSuccess = {
                var designDatas : ArrayList<DesignDetailData> = it.data
                _designDatas.value = designDatas
                Log.d("nulkong", "ZzimDesigns Network onSuccess")
            },
            onError = {
                Log.d("nulkong", "ZzimDesigns Network onError")
            },
            onFinished = {
                Log.d("nulkong", "ZzimDesigns Network Finished")
            }
        )
    }
}