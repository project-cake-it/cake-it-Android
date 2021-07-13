package com.cakeit.cakitandroid.presentation.zzim

import android.app.Application
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseFragment
import com.cakeit.cakitandroid.databinding.FragmentZzimBinding
import com.cakeit.cakitandroid.presentation.main.MainActivity
import com.google.android.material.tabs.TabLayout

class ZzimFragment : BaseFragment<FragmentZzimBinding, ZzimViewModel>() {

    private val adapter by lazy { ZzimContentsPagerAdapter(childFragmentManager, 2) }
    private lateinit var binding : FragmentZzimBinding
    private lateinit var zzimViewModel: ZzimViewModel

    companion object {
        const val TAG: String = "ZzimActivityTAG"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = getViewDataBinding()
        binding.vm = getViewModel()

        setTabLayout()
    }
    override fun getLayoutId(): Int {
        return R.layout.fragment_zzim
    }


    override fun getViewModel(): ZzimViewModel {
        // 뷰페이저 어댑터 연결
        binding.vpZzimViewpager.adapter = adapter

        // 탭 레이아웃에 뷰페이저 연결
        binding.tlZzimTabLayout.setupWithViewPager(binding.vpZzimViewpager)

        zzimViewModel = ViewModelProvider(this, ZzimViewModel.Factory(
            activity!!.application, childFragmentManager, TabLayout.ViewPagerOnTabSelectedListener(binding.vpZzimViewpager)
            , TabLayout.TabLayoutOnPageChangeListener(binding.tlZzimTabLayout)
        )).get(ZzimViewModel::class.java)

        return zzimViewModel
    }

    fun setTabLayout()
    {
        binding.tlZzimTabLayout.getTabAt(0)?.setText(R.string.zzim_design_fragment_name)
        binding.tlZzimTabLayout.getTabAt(1)?.setText(R.string.zzim_shop_fragment_name)

    }
}