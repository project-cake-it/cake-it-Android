package com.cakeit.cakitandroid.presentation.shop.inform

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseFragment
import com.cakeit.cakitandroid.databinding.FragmentShopInformBinding

class ShopInformFragment : BaseFragment<FragmentShopInformBinding, ShopInformFragmentViewModel>() {

    lateinit var binding : FragmentShopInformBinding
    lateinit var shopInformFragmentViewModel : ShopInformFragmentViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("songjem", "here shopinfo")
        binding = getViewDataBinding()
        binding.viewModel = getViewModel()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_shop_inform
    }

    override fun getViewModel(): ShopInformFragmentViewModel {
        shopInformFragmentViewModel = ViewModelProvider(this).get(ShopInformFragmentViewModel::class.java)
        return shopInformFragmentViewModel
    }
}