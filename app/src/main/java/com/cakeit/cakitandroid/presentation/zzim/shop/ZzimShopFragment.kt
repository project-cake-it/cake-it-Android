package com.cakeit.cakitandroid.presentation.zzim.shop

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseFragment
import com.cakeit.cakitandroid.databinding.FragmentZzimShopBinding
import com.cakeit.cakitandroid.presentation.zzim.ZzimViewModel

class ZzimShopFragment : BaseFragment<FragmentZzimShopBinding, ZzimViewModel>() {

    lateinit var binding : FragmentZzimShopBinding
    lateinit var zzimViewModel : ZzimViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = getViewDataBinding()
        binding.vm = getViewModel()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_zzim_shop
    }

    override fun getViewModel(): ZzimViewModel {
        zzimViewModel = ViewModelProvider(requireActivity()).get(ZzimViewModel::class.java)
        return zzimViewModel
    }
}