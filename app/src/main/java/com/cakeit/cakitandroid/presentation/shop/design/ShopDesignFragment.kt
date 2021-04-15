package com.cakeit.cakitandroid.presentation.shop.design

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseFragment
import com.cakeit.cakitandroid.databinding.FragmentShopDesignBinding

class ShopDesignFragment : BaseFragment<FragmentShopDesignBinding, ShopDesignFragmentViewModel>() {

    lateinit var binding : FragmentShopDesignBinding
    lateinit var shopDesignFragmentViewModel : ShopDesignFragmentViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("songjem", "here shopdesign")
        binding = getViewDataBinding()
        binding.viewModel = getViewModel()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_shop_design
    }

    override fun getViewModel(): ShopDesignFragmentViewModel {
        shopDesignFragmentViewModel = ViewModelProvider(this).get(ShopDesignFragmentViewModel::class.java)
        return shopDesignFragmentViewModel
    }
}