package com.cakeit.cakitandroid.presentation.shoplist

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseActivity
import com.cakeit.cakitandroid.databinding.ActivityShopListBinding

class ShopListActivity : BaseActivity<ActivityShopListBinding, ShopListViewModel>() {

    lateinit var shopListViewModel: ShopListViewModel
    lateinit var shopListBinding: ActivityShopListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding()
        shopListBinding = getViewDataBinding()
        shopListBinding.viewModel = shopListViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_shop_list
    }

    override fun getViewModel(): ShopListViewModel {
        shopListViewModel = ViewModelProvider(this).get(ShopListViewModel::class.java)
        return shopListViewModel
    }
}