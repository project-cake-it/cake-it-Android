package com.cakeit.cakitandroid.presentation.search.searchlist

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseActivity
import com.cakeit.cakitandroid.databinding.ActivitySearchListBinding
import com.cakeit.cakitandroid.presentation.search.SearchActivity
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_search_list.*

class SearchListActivity : BaseActivity<ActivitySearchListBinding, SearchListViewModel>() {

    private val adapter by lazy { SearchListPagerAdapter(supportFragmentManager, 2) }
    private lateinit var binding : ActivitySearchListBinding
    private lateinit var searchListViewModel: SearchListViewModel

    companion object {
        const val TAG: String = "SearchListActivityTAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataBinding()
        binding = getViewDataBinding()
        binding.viewModel = getViewModel()

        setTabLayout()

        btn_searchlist_back.setOnClickListener {
            var intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
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
        )).get(SearchListViewModel::class.java)

        return searchListViewModel
    }

    fun setTabLayout()
    {
        binding.tlSearchlistTabLayout.getTabAt(0)?.setText(R.string.searchlist_design_fragment_name)
        binding.tlSearchlistTabLayout.getTabAt(1)?.setText(R.string.searchlist_shop_fragment_name)
    }
}