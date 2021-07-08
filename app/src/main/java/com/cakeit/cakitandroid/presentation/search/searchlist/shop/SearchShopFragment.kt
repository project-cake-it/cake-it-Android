package com.cakeit.cakitandroid.presentation.search.searchlist.shop

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseFragment
import com.cakeit.cakitandroid.databinding.FragmentSearchShopBinding

class SearchShopFragment : BaseFragment<FragmentSearchShopBinding, SearchShopViewModel>() {

    lateinit var binding : FragmentSearchShopBinding
    lateinit var searchShopViewModel : SearchShopViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = getViewDataBinding()
        binding.viewModel = getViewModel()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_search_shop
    }

    override fun getViewModel(): SearchShopViewModel {
        searchShopViewModel = ViewModelProvider(this).get(SearchShopViewModel::class.java)
        return searchShopViewModel
    }
}