package com.cakeit.cakitandroid.presentation.zzim

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseActivity
import com.cakeit.cakitandroid.databinding.ActivityZzimBinding
import com.google.android.material.tabs.TabLayout

class ZzimActivity : BaseActivity<ActivityZzimBinding, ZzimViewModel>() {

    private val adapter by lazy { ZzimContentsPagerAdapter(supportFragmentManager, 2) }
    private lateinit var binding : ActivityZzimBinding
    private lateinit var zzimViewModel: ZzimViewModel

    companion object {
        const val TAG: String = "ZzimActivityTAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataBinding()
        binding = getViewDataBinding()
        binding.vm = getViewModel()

        setTabLayout()
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_zzim
    }


    override fun getViewModel(): ZzimViewModel {
        // 뷰페이저 어댑터 연결
        binding.vpZzimViewpager.adapter = adapter

        // 탭 레이아웃에 뷰페이저 연결
        binding.tlZzimTabLayout.setupWithViewPager(binding.vpZzimViewpager)

        zzimViewModel = ViewModelProvider(this, ZzimViewModel.Factory(application, supportFragmentManager, TabLayout.ViewPagerOnTabSelectedListener(binding.vpZzimViewpager)
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