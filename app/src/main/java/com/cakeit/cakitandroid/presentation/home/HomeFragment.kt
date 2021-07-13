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
import com.cakeit.cakitandroid.base.BaseFragment
import com.cakeit.cakitandroid.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(), View.OnClickListener {

    private lateinit var binding : FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel

    lateinit var promotionPagerAdapter : PromotionPagerAdapter
    lateinit var popularCakeListAdapter: PopularCakeAdapter

    companion object {
        const val TAG: String = "HomeTabTAG"
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = getViewDataBinding()
        binding.vm = getViewModel()

        setListener()
        initRecycler()
        getPromotion()
        getPopularCake()

        homeViewModel.promotionsData.observe(viewLifecycleOwner, Observer { datas ->
            if(datas != null)
            {
                var data = ArrayList<String>()
                for(promotion in datas)
                {
                    data.add(promotion.imageUrl)
                }

                promotionPagerAdapter = PromotionPagerAdapter(context!!,  data)

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
        homeViewModel.popularCakeData.observe(viewLifecycleOwner, Observer { datas ->
            if(datas != null)
            {
                popularCakeListAdapter.setRefresh(datas)
            }
            else {
                Log.d("nulkong", "get popularCakeList size == 0")
            }
        })
    }
    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun getViewModel(): HomeViewModel {
        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
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
        popularCakeListAdapter = PopularCakeAdapter(context!!)
        popularCakeListAdapter.setOnItemClickListener(this)

        rv_home_cake_list.adapter = popularCakeListAdapter
        rv_home_cake_list.layoutManager = GridLayoutManager(context!!, 2)

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