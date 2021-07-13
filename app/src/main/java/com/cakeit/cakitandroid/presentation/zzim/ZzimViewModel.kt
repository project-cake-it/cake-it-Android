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
import com.cakeit.cakitandroid.domain.usecase.SearchDesignUseCase
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
    val cakeShopItems : LiveData<ArrayList<CakeShopData>> get() = _cakeShopItems

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

    fun sendParamsForZzimShopList() {
        Log.d("songjem", "sendParamsForZzimShopList")
        ZzimShopListUseCase.execute(
            ZzimShopListUseCase.Request(""),
            onSuccess = {
                Log.d("songjem", "zzimShop onSuccess")
                var cakeShops = ArrayList<CakeShopData>()
                for(i in 0 .. it.data.size-1 ) {
                    if(it.data[i].shopImages[0] == null) Log.d("songjem", "image is null")
                    else if(it.data[i].hashtags == null ) Log.d("songjem", "tag is null")
                    else if(it.data[i].sizes == null)  Log.d("songjem", "size is null")
                    else cakeShops.add(CakeShopData(it.data[i].id, it.data[i].name, it.data[i].address, it.data[i].shopImages[0].shopImageUrl, it.data[i].hashtags!!, it.data[i].sizes!!))
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
}