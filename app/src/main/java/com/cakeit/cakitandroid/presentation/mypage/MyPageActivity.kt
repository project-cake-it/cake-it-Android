package com.cakeit.cakitandroid.presentation.mypage

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseActivity
import com.cakeit.cakitandroid.databinding.ActivityMypageBinding

class MyPageActivity : BaseActivity<ActivityMypageBinding, MyPageViewModel>() {

    private lateinit var binding : ActivityMypageBinding
    private lateinit var viewModel: MyPageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding()
        binding = getViewDataBinding()
        binding.viewModel = getViewModel()
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_mypage
    }

    override fun getViewModel(): MyPageViewModel {
        viewModel = ViewModelProvider(this).get(MyPageViewModel::class.java)
        return viewModel
    }
}