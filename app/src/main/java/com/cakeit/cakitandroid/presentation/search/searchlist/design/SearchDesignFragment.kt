package com.cakeit.cakitandroid.presentation.search.searchlist.design

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseFragment
import com.cakeit.cakitandroid.databinding.FragmentSearchDesignBinding

class SearchDesignFragment : BaseFragment<FragmentSearchDesignBinding, SearchDesignViewModel>() {

    lateinit var binding : FragmentSearchDesignBinding
    lateinit var searchDesignViewModel : SearchDesignViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = getViewDataBinding()
        binding.viewModel = getViewModel()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_search_design
    }

    override fun getViewModel(): SearchDesignViewModel {
        searchDesignViewModel = ViewModelProvider(this).get(SearchDesignViewModel::class.java)
        return searchDesignViewModel
    }
}