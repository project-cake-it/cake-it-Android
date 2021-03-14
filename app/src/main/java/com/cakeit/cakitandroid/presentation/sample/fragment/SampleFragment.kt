package com.cakeit.cakitandroid.presentation.main.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseFragment
import com.cakeit.cakitandroid.databinding.FragmentSampleBinding
import com.cakeit.cakitandroid.presentation.sample.fragment.SampleFragmentViewModel

class SampleFragment : BaseFragment<FragmentSampleBinding, SampleFragmentViewModel>() {

    lateinit var binding : FragmentSampleBinding
    lateinit var sampleFragmentViewModel : SampleFragmentViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = getViewDataBinding()
        binding.viewModel = getViewModel()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_sample
    }

    override fun getViewModel(): SampleFragmentViewModel {
        sampleFragmentViewModel = ViewModelProvider(this).get(SampleFragmentViewModel::class.java)
        return sampleFragmentViewModel
    }
}