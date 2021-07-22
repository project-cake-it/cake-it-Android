package com.cakeit.cakitandroid.presentation.search.searchlist

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseActivity
import com.cakeit.cakitandroid.databinding.ActivitySearchListBinding
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_search_list.*

class SearchListActivity : BaseActivity<ActivitySearchListBinding, SearchListViewModel>() {

    private val adapter by lazy { SearchListPagerAdapter(supportFragmentManager, 2, keyword) }
    private lateinit var binding : ActivitySearchListBinding
    private lateinit var searchListViewModel: SearchListViewModel
    var keyword : String = ""

    companion object {
        const val TAG: String = "SearchListActivityTAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        keyword = intent.getStringExtra("keyword")!!
        dataBinding()
        binding = getViewDataBinding()
        binding.viewModel = getViewModel()

        Log.d("songjem", "songjem 2, keyword = " + keyword)
        setTabLayout()

        btn_searchlist_back.setOnClickListener {
            finish()
        }
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_search_list
    }

    override fun getViewModel(): SearchListViewModel {
        // 뷰페이저 어댑터 연결
        binding.vpSearchlistViewpager.adapter = adapter

        // 탭 레이아웃에 뷰페이저 연결
        binding.tlSearchlistTabLayout.setupWithViewPager(binding.vpSearchlistViewpager)

        searchListViewModel = ViewModelProvider(this, SearchListViewModel.Factory(application, supportFragmentManager, TabLayout.ViewPagerOnTabSelectedListener(binding.vpSearchlistViewpager)
            , TabLayout.TabLayoutOnPageChangeListener(binding.tlSearchlistTabLayout)
        , keyword)).get(SearchListViewModel::class.java)

        return searchListViewModel
    }

    fun setTabLayout()
    {
        binding.tlSearchlistTabLayout.getTabAt(0)?.setText(R.string.searchlist_design_fragment_name)
        binding.tlSearchlistTabLayout.getTabAt(1)?.setText(R.string.searchlist_shop_fragment_name)
    }
}