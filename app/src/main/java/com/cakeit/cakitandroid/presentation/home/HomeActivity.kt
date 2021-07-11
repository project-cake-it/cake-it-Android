package com.cakeit.cakitandroid.presentation.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseActivity
import com.cakeit.cakitandroid.databinding.ActivityHomeBinding
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity  : BaseActivity<ActivityHomeBinding, HomeViewModel>() {

    private lateinit var binding : ActivityHomeBinding
    private lateinit var homeViewModel: HomeViewModel

    lateinit var promotionPagerAdapter : PromotionPagerAdapter

    companion object {
        const val TAG: String = "HomeActivityTAG"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        dataBinding()
        binding = getViewDataBinding()
        binding.vm = getViewModel()

        initRecycler()
        getPromotion()

        homeViewModel.promotionsData.observe(this, Observer { datas ->
            if(datas != null)
            {
                var data = ArrayList<String>()
                for(promotion in datas)
                {
                    data.add(promotion.imageUrl)
                }

                promotionPagerAdapter = PromotionPagerAdapter(applicationContext,  data)

                tv_home_promotion_cnt.text = " / ${datas.size}"
                vp_home_promotion.adapter = promotionPagerAdapter
                vp_home_promotion.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                    override fun onPageScrolled(
                        position: Int,
                        positionOffset: Float,
                        positionOffsetPixels: Int
                    ) {
                        tv_home_promotion_current_num.text = (position+1).toString()
                    }

                    override fun onPageSelected(position: Int) {
                        //해당 디자인 상세로 넘어가
                    }

                    override fun onPageScrollStateChanged(position: Int) {
                    }
                })
            }
            else {
                Log.d("nulkong", "get promotionsData size == 0")
            }
        })
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

    fun initRecycler() {
//        designGridAdapter = DesignGridAdapter(context!!)
//        designGridAdapter.setOnItemClickListener(this)
//
//        v.rv_zzim_design_item.adapter = designGridAdapter
//        v.rv_zzim_design_item.layoutManager = GridLayoutManager(context, 2)
    }
}