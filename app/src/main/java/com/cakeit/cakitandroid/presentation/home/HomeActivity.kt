package com.cakeit.cakitandroid.presentation.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager.widget.ViewPager
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseActivity
import com.cakeit.cakitandroid.databinding.ActivityHomeBinding
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity  : BaseActivity<ActivityHomeBinding, HomeViewModel>(), View.OnClickListener {

    private lateinit var binding : ActivityHomeBinding
    private lateinit var homeViewModel: HomeViewModel

    lateinit var promotionPagerAdapter : PromotionPagerAdapter
    lateinit var popularCakeListAdapter: PopularCakeAdapter

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

        setListener()
        initRecycler()
        getPromotion()
        getPopularCake()

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
        homeViewModel.popularCakeData.observe(this, Observer { datas ->
            if(datas != null)
            {
                popularCakeListAdapter.setRefresh(datas)
                Log.d("TEST",datas.toString())
            }
            else {
                Log.d("nulkong", "get popularCakeList size == 0")
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

    fun setListener()
    {
        tv_home_hide_theme.setOnClickListener(this)
        rl_home_view_more.setOnClickListener(this)
    }

    fun getPromotion()
    {
        homeViewModel.getPromotion()
    }

    fun getPopularCake()
    {
        homeViewModel.getPopularCake()
    }

    fun initRecycler() {
        popularCakeListAdapter = PopularCakeAdapter(applicationContext)
        popularCakeListAdapter.setOnItemClickListener(this)

        rv_home_cake_list.adapter = popularCakeListAdapter
        rv_home_cake_list.layoutManager = GridLayoutManager(applicationContext, 2)

    }

    override fun onClick(v: View?) {
        when(v!!.id)
        {
            R.id.tv_home_hide_theme -> {
                tv_home_hide_theme.visibility = View.GONE
                rl_home_company1_theme.visibility = View.GONE
                rl_home_view_more.visibility = View.VISIBLE
                ll_home_third_theme_line.visibility = View.GONE
                ll_home_fourth_theme_line.visibility = View.GONE
            }

            R.id.rl_home_view_more -> {
                tv_home_hide_theme.visibility = View.VISIBLE
                rl_home_company1_theme.visibility = View.VISIBLE
                rl_home_view_more.visibility = View.GONE
                ll_home_third_theme_line.visibility = View.VISIBLE
                ll_home_fourth_theme_line.visibility = View.VISIBLE
            }
        }
    }
}