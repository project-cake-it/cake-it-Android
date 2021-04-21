package com.cakeit.cakitandroid.presentation.shop

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseActivity
import com.cakeit.cakitandroid.databinding.ActivityShopBinding
import com.google.android.material.tabs.TabLayout

class ShopActivity : BaseActivity<ActivityShopBinding, ShopViewModel>() {

    private val adapter by lazy { ContentsPagerAdapter(supportFragmentManager, 2) }
    private lateinit var binding : ActivityShopBinding
    private lateinit var shopViewModel: ShopViewModel

    companion object {
        const val TAG: String = "ShopActivityTAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataBinding()
        binding = getViewDataBinding()
        binding.viewModel = getViewModel()

        binding.tlShopTablayout.getTabAt(0)?.setText(R.string.shop_design_fragment_name)
        binding.tlShopTablayout.getTabAt(1)?.setText(R.string.shop_inform_fragment_name)

        /*binding.vpShopViewpager.addOnPageChangeListener(object  : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                binding.tlShopTablayout.getTabAt(0)?.setText(R.string.shop_design_fragment_name)
                binding.tlShopTablayout.getTabAt(1)?.setText(R.string.shop_inform_fragment_name)

                when(position){
                    0   ->    binding.tlShopTablayout.getTabAt(0)?.setText(R.string.shop_design_fragment_name)
                    1   ->    binding.tlShopTablayout.getTabAt(1)?.setText(R.string.shop_inform_fragment_name)
                }
            }
        })*/
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_shop
    }

    override fun getViewModel(): ShopViewModel {
        // 뷰페이저 어댑터 연결
        binding.vpShopViewpager.adapter = ShopActivity@adapter

        // 탭 레이아웃에 뷰페이저 연결
        binding.tlShopTablayout.setupWithViewPager(binding.vpShopViewpager)

        shopViewModel = ViewModelProvider(this, ShopViewModel.Factory(application, supportFragmentManager, TabLayout.ViewPagerOnTabSelectedListener(binding.vpShopViewpager)
        , TabLayout.TabLayoutOnPageChangeListener(binding.tlShopTablayout)
        )).get(ShopViewModel::class.java)

        return shopViewModel
    }
}