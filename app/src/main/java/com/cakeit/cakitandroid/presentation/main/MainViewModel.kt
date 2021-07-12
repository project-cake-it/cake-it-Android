package com.cakeit.cakitandroid.presentation.main

import android.app.Application
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cakeit.cakitandroid.base.BaseViewModel
import com.cakeit.cakitandroid.presentation.zzim.ZzimContentsPagerAdapter
import com.google.android.material.tabs.TabLayout

class MainViewModel(application: Application,
                    fm: FragmentManager,
                    viewPagerOnTabSelectedListener: TabLayout.ViewPagerOnTabSelectedListener,
                    tabLayoutOnPageChangeListener: TabLayout.TabLayoutOnPageChangeListener) : BaseViewModel<Any?>(application) {

    private val viewPagerAdapter: FragmentStatePagerAdapter
    private val viewPagerOnTabSelectedListener: TabLayout.ViewPagerOnTabSelectedListener
    private val tabLayoutOnPageChangeListener: TabLayout.TabLayoutOnPageChangeListener

    class Factory(val application: Application, val fm: FragmentManager, val viewPagerOnTabSelectedListener : TabLayout.ViewPagerOnTabSelectedListener
                  , val tabLayoutOnPageChangeListener : TabLayout.TabLayoutOnPageChangeListener
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel(application, fm, viewPagerOnTabSelectedListener, tabLayoutOnPageChangeListener) as T
        }
    }

    init {
        this.viewPagerOnTabSelectedListener = viewPagerOnTabSelectedListener
        this.tabLayoutOnPageChangeListener = tabLayoutOnPageChangeListener
        viewPagerAdapter = MainPagerAdapter(fm, 5)
    }

    val isRegistered = MutableLiveData<Boolean>() // 회원가입이 되어있는가?

    fun checkIsRegistered(){
        //for testing social login
        //추후에는 pref등에서 token 있는지 체크
        isRegistered.value = false
    }
}