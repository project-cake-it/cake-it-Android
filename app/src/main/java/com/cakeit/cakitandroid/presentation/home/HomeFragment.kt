package com.cakeit.cakitandroid.presentation.home

import android.annotation.SuppressLint
import android.content.Intent
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
import com.cakeit.cakitandroid.di.api.responses.DesignListResponseData
import com.cakeit.cakitandroid.presentation.design.DesignDetailActivity
import com.cakeit.cakitandroid.presentation.list.designlist.DesignListActivity
import com.cakeit.cakitandroid.presentation.shop.design.DesignGridAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_home_promotion.*
import kotlin.properties.Delegates

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(), View.OnClickListener {

    private lateinit var binding : FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel

    lateinit var promotionPagerAdapter : PromotionPagerAdapter
    lateinit var popularCakeListAdapter: PopularCakeAdapter

    var designId by Delegates.notNull<Int>()
    var promotionDesignId = ArrayList<Int>()
    var popularDesignId = ArrayList<Int>()

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
                    promotionDesignId.add(promotion.designId)
                }

                promotionPagerAdapter = PromotionPagerAdapter(context!!,  data)
                promotionPagerAdapter.setOnItemClickListener(object : PromotionPagerAdapter.OnItemClickListener{
                    override fun OnClick(view: View, position: Int) {
                        val intent = Intent(context, DesignDetailActivity::class.java)
                        intent.putExtra("designId", designId)
                        startActivity(intent)
                    }
                })

                tv_home_promotion_cnt.text = " / ${datas.size}"
                vp_home_promotion.adapter = promotionPagerAdapter

                vp_home_promotion.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                    override fun onPageScrolled(
                        position: Int,
                        positionOffset: Float,
                        positionOffsetPixels: Int
                    ) {
                        tv_home_promotion_current_num.text = (position+1).toString()
                        designId = promotionDesignId[position]
                    }

                    override fun onPageSelected(position: Int) {
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
                for(data in datas)
                {
                    popularDesignId.add(data.id)
                }
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
        rl_home_theme_birthday.setOnClickListener(this)
        rl_home_theme_anniv.setOnClickListener(this)
        rl_home_theme_wedding.setOnClickListener(this)
        rl_home_company1_theme.setOnClickListener(this)
        rl_home_theme_retire.setOnClickListener(this)
        rl_home_theme_discharge.setOnClickListener(this)
        rl_home_theme_club.setOnClickListener(this)
        rl_home_theme_etc.setOnClickListener(this)
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
        rv_home_cake_list.setNestedScrollingEnabled(false);
    }

    override fun onClick(v: View?) {
        Log.d("TEST","SUCCESS")
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

            R.id.rl_home_theme_birthday -> {
                var intent = Intent(context, DesignListActivity::class.java)
                intent.putExtra("theme", "생일")
                startActivity(intent)
            }

            R.id.rl_home_theme_anniv -> {
                var intent = Intent(context, DesignListActivity::class.java)
                intent.putExtra("theme", "기념일")
                startActivity(intent)
            }

            R.id.rl_home_theme_wedding -> {
                var intent = Intent(context, DesignListActivity::class.java)
                intent.putExtra("theme", "결혼")
                startActivity(intent)
            }

            R.id.rl_home_company1_theme -> {
                var intent = Intent(context, DesignListActivity::class.java)
                intent.putExtra("theme", "입사")
                startActivity(intent)
            }

            R.id.rl_home_theme_retire -> {
                var intent = Intent(context, DesignListActivity::class.java)
                intent.putExtra("theme", "퇴사")
                startActivity(intent)
            }

            R.id.rl_home_theme_discharge -> {
                var intent = Intent(context, DesignListActivity::class.java)
                intent.putExtra("theme", "전역")
                startActivity(intent)
            }

            R.id.rl_home_theme_club -> {
                var intent = Intent(context, DesignListActivity::class.java)
                intent.putExtra("theme", "동아리")
                startActivity(intent)
            }

            R.id.rl_home_theme_etc -> {
                var intent = Intent(context, DesignListActivity::class.java)
                intent.putExtra("theme", "기타")
                startActivity(intent)
            }

            else -> {
                var position: Int = rv_home_cake_list.getChildAdapterPosition(v)
                val intent = Intent(context, DesignDetailActivity::class.java)
                intent.putExtra("designId", popularDesignId[position])
                startActivity(intent)
            }
        }
    }
}