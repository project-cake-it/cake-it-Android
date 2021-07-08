package com.cakeit.cakitandroid.presentation.shop

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseActivity
import com.cakeit.cakitandroid.databinding.ActivityShopDetailBinding
import com.cakeit.cakitandroid.di.api.responses.ShopDetailResponseData
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_shop_detail.*
import kotlinx.android.synthetic.main.activity_test.*
import kotlin.properties.Delegates

class ShopDetailActivity : BaseActivity<ActivityShopDetailBinding, ShopDetailViewModel>() {

    private val adapter by lazy { ContentsPagerAdapter(supportFragmentManager, 2) }
    private lateinit var binding : ActivityShopDetailBinding
    private lateinit var shopDetailViewModel: ShopDetailViewModel

    var shopId by Delegates.notNull<Int>()

    companion object {
        const val TAG: String = "ShopActivityTAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataBinding()
        binding = getViewDataBinding()
        binding.vm = getViewModel()

        shopDetailViewModel.shopDetailData.observe(this, Observer { datas ->
            if(datas != null) {
                var sizeDataAll : String = ""
                for (i in datas.sizes.indices)
                {
                    sizeDataAll += datas.sizes[i].name
                    if(datas.sizes[i].size.isNotEmpty()) sizeDataAll += "(${datas.sizes[i].size})"
                    sizeDataAll += "/${datas.sizes[i].price}원"
                    if(i < datas.sizes.size) sizeDataAll += "\n"
                }
                tv_cake_detail_size_price_contents.text = sizeDataAll
            }
            else {
                Log.d("nulkong", "get shopDetail size == 0")
            }
        })

        setTabLayout()
        sendShopIdToServer()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_shop_detail
    }

    override fun getViewModel(): ShopDetailViewModel {
        // 뷰페이저 어댑터 연결
        binding.vpShopDetailViewpager.adapter = ShopActivity@adapter

        // 탭 레이아웃에 뷰페이저 연결
        binding.tlShopDetailTabLayout.setupWithViewPager(binding.vpShopDetailViewpager)

        shopDetailViewModel = ViewModelProvider(this, ShopDetailViewModel.Factory(application, supportFragmentManager, TabLayout.ViewPagerOnTabSelectedListener(binding.vpShopDetailViewpager)
        , TabLayout.TabLayoutOnPageChangeListener(binding.tlShopDetailTabLayout)
        )).get(ShopDetailViewModel::class.java)

        return shopDetailViewModel
    }

    fun setTabLayout()
    {
        binding.tlShopDetailTabLayout.getTabAt(0)?.setText(R.string.shop_design_fragment_name)
        binding.tlShopDetailTabLayout.getTabAt(1)?.setText(R.string.shop_inform_fragment_name)
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

    fun sendShopIdToServer()
    {
        shopId = 1;

        shopDetailViewModel.sendShopIdForShopDetail(shopId)
    }
}