package com.cakeit.cakitandroid.presentation.main.test

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseActivity
import com.cakeit.cakitandroid.databinding.ActivitySampleBinding
import com.cakeit.cakitandroid.presentation.sample.SampleViewModel

class SampleActivity : BaseActivity<ActivitySampleBinding, SampleViewModel>() {

    private lateinit var binding : ActivitySampleBinding
    private lateinit var sampleViewModel: SampleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding()
        binding = getViewDataBinding()
        binding.viewModel = getViewModel()
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_sample
    }

    override fun getViewModel(): SampleViewModel {
        sampleViewModel = ViewModelProvider(this).get(SampleViewModel::class.java)
        return sampleViewModel
    }
}