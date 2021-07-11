package com.cakeit.cakitandroid.presentation.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseActivity
import com.cakeit.cakitandroid.databinding.ActivityDesignDetailBinding
import com.cakeit.cakitandroid.databinding.ActivityHomeBinding
import com.cakeit.cakitandroid.presentation.design.DesignDetailViewModel

class HomeActivity  : BaseActivity<ActivityHomeBinding, HomeViewModel>() {

    private lateinit var binding : ActivityHomeBinding
    private lateinit var homeViewModel: HomeViewModel

    companion object {
        const val TAG: String = "HomeActivityTAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        dataBinding()
        binding = getViewDataBinding()
        binding.vm = getViewModel()

        getPromotion()
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_home
    }

    override fun getViewModel(): HomeViewModel {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        return homeViewModel
    }

    fun getPromotion()
    {
        homeViewModel.getPromotion()
    }

}