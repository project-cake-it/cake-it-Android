package com.cakeit.cakitandroid.presentation.shop

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseActivity
import com.cakeit.cakitandroid.databinding.ActivityShopBinding

class ShopActivity : BaseActivity<ActivityShopBinding, ShopViewModel>() {

    private lateinit var binding : ActivityShopBinding
    private lateinit var shopViewModel: ShopViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding()
        binding = getViewDataBinding()
        binding.viewModel = getViewModel()
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_shop
    }

    override fun getViewModel(): ShopViewModel {
        shopViewModel = ViewModelProvider(this).get(ShopViewModel::class.java)
        return shopViewModel
    }
}