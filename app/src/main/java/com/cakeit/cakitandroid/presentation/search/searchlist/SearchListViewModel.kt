package com.cakeit.cakitandroid.presentation.search.searchlist

import android.app.Application
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cakeit.cakitandroid.base.BaseViewModel
import com.google.android.material.tabs.TabLayout

class SearchListViewModel(application: Application,
                          fm: FragmentManager,
                          viewPagerOnTabSelectedListener: TabLayout.ViewPagerOnTabSelectedListener,
                          tabLayoutOnPageChangeListener: TabLayout.TabLayoutOnPageChangeListener, keyword : String) : BaseViewModel<Any?>(application) {

    private val viewPagerAdapter: FragmentStatePagerAdapter
    private val viewPagerOnTabSelectedListener: TabLayout.ViewPagerOnTabSelectedListener
    private val tabLayoutOnPageChangeListener: TabLayout.TabLayoutOnPageChangeListener
    private var keyword : String

    class Factory(val application: Application, val fm: FragmentManager, val viewPagerOnTabSelectedListener : TabLayout.ViewPagerOnTabSelectedListener
                  , val tabLayoutOnPageChangeListener : TabLayout.TabLayoutOnPageChangeListener, val keyword: String
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SearchListViewModel(application, fm, viewPagerOnTabSelectedListener, tabLayoutOnPageChangeListener, keyword) as T
        }
    }

    init {
        this.viewPagerOnTabSelectedListener = viewPagerOnTabSelectedListener
        this.tabLayoutOnPageChangeListener = tabLayoutOnPageChangeListener
        this.keyword = keyword
        viewPagerAdapter = SearchListPagerAdapter(fm, 2, keyword)
    }
}