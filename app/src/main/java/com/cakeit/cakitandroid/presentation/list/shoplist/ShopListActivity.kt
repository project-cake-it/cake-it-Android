package com.cakeit.cakitandroid.presentation.list.shoplist

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseActivity
import com.cakeit.cakitandroid.data.source.local.entity.CakeShopData
import com.cakeit.cakitandroid.databinding.ActivityShopListBinding
import com.cakeit.cakitandroid.domain.model.CakeSizeAndrPrice
import com.cakeit.cakitandroid.presentation.list.TodayDecorator
import com.cakeit.cakitandroid.presentation.list.MinDecorator
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import kotlinx.android.synthetic.main.activity_shop_list.*
import java.util.*
import kotlin.collections.ArrayList


class ShopListActivity : BaseActivity<ActivityShopListBinding, ShopListViewModel>(),
    View.OnClickListener {

    lateinit var shopListViewModel: ShopListViewModel
    lateinit var shopListBinding: ActivityShopListBinding

    private lateinit var shopListAdapter: ShopListAdapter
    private lateinit var shopDefaultFilterAdapter: ShopDefaultFilterAdapter
    private lateinit var shopRegionFilterAdapter: ShopRegionFilterAdapter
    private val shopTagItems : ArrayList<String> = ArrayList()
    private val shopSizeAndPriceItems : ArrayList<CakeSizeAndrPrice> = ArrayList()
    private lateinit var filterItems : ArrayList<String>
    private lateinit var regionItems : ArrayList<String>

    private val DEFAULT_FILTER_CODE : Int = 0
    private val REGION_FILTER_CODE : Int = 1
    private val REFRESH_FILTER_CODE : Int = 2

    private val regionList = listOf<String>("전체", "강남구", "관악구", "광진구", "마포구", "서대문구"
    , "송파구", "노원구", "성북구", "중구", "중랑구")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding()
        shopListBinding = getViewDataBinding()
        shopListBinding.viewModel = getViewModel()

        shopTagItems.add("태그1")
        shopTagItems.add("태그2")
        shopSizeAndPriceItems.add(CakeSizeAndrPrice("미니", 18000))
        shopSizeAndPriceItems.add(CakeSizeAndrPrice("1호", 34000))

        btn_filter_refresh_shop_list.setOnClickListener(this)
        btn_filter_default_shop_list.setOnClickListener(this)
        btn_filter_pickup_region_shop_list.setOnClickListener(this)
        btn_filter_pickup_date_shop_list.setOnClickListener(this)

        initRecyclerview()

        // 현재 Year
        fun getCurrentYear(): Int = Calendar.getInstance().get(Calendar.YEAR)
        // 현재 Month
        fun getCurrentMonth(): Int = Calendar.getInstance().get(Calendar.MONTH) + 1
        // 현재 Day
        fun getCurrentDay(): Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

//        cv_pickup_calendar_shop_list.state().edit()
//            .setMinimumDate(
//                CalendarDay.from(
//                    getCurrentYear(),
//                    getCurrentMonth(),
//                    getCurrentDay() + 1
//                )
//            )
//            .commit()

        cv_pickup_calendar_shop_list.addDecorators(
            MinDecorator(
                applicationContext
            ),
            TodayDecorator(
                applicationContext
            )
        )

        // 달력 날짜 선택
        cv_pickup_calendar_shop_list.setOnDateChangedListener(OnDateSelectedListener { widget, date, selected ->

            var selectedDate = date.month.toString() + "월 " + date.day.toString() + "일"
            tv_filter_pickup_date_title_shop_list.text = selectedDate
            Log.d("song", "selectedDate = " + selectedDate)
            visiblePickUpCalendar(false)
            btn_filter_pickup_date_shop_list.isSelected = false
        })
    }

    // 임시 메서드
    fun insertTempData() {
        shopListViewModel.insertCakeShop(CakeShopData(0,"케이크 매장명1", "서울시 노원구 마들로 31"))
        shopListViewModel.insertCakeShop(CakeShopData(1, "케이크 매장명2", "서울시 서초구 마들로 31"))
        shopListViewModel.insertCakeShop(CakeShopData(2, "케이크 매장명3", "서울시 중구 마들로 31"))
        shopListViewModel.insertCakeShop(CakeShopData(3, "케이크 매장명4", "서울시 강북구 마들로 31"))
        shopListViewModel.insertCakeShop(CakeShopData(4, "케이크 매장명5", "서울시 도봉구 마들로 31"))
        shopListViewModel.insertCakeShop(CakeShopData(5, "케이크 매장명6", "서울시 강남구 마들로 31"))
        shopListViewModel.insertCakeShop(CakeShopData(6, "케이크 매장명7", "서울시 성북구 마들로 31"))
        shopListViewModel.insertCakeShop(CakeShopData(7, "케이크 매장명8", "서울시 마포구 마들로 31"))
    }

    fun initRecyclerview() {
        shopListAdapter = ShopListAdapter().apply {
            listener = object : ShopListAdapter.OnShopItemClickListener {
                override fun onShopItemClick(position: Int) {
                    Toast.makeText(applicationContext, "shop list item" + position + " is clicked", Toast.LENGTH_LONG).show()
                }
            }
        }

        shopDefaultFilterAdapter = ShopDefaultFilterAdapter().apply {
            listener = object : ShopDefaultFilterAdapter.OnShopFilterItemClickListener {
                override fun onShopFilterItemClick(position: Int) {
                    Toast.makeText(applicationContext, "filter item" + position + " is clicked", Toast.LENGTH_LONG).show()
                    tv_filter_default_title_shop_list.text = filterItems[position]
                    visibleFilterList(false, DEFAULT_FILTER_CODE)
                    btn_filter_default_shop_list.isSelected = false
                }
            }
        }

        shopRegionFilterAdapter = ShopRegionFilterAdapter().apply {
            listener = object : ShopRegionFilterAdapter.OnShopFilterItemClickListener {
                override fun onShopFilterItemClick(position: Int) {
                    Toast.makeText(applicationContext, "region item" + position + " is clicked", Toast.LENGTH_LONG).show()
                }
            }
        }

        rv_shop_list_shop_list.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@ShopListActivity)
            adapter = shopListAdapter
        }

        rv_filter_default_list_shop_list.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@ShopListActivity)
            adapter = shopDefaultFilterAdapter
        }

        rv_filter_region_list_shop_list.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@ShopListActivity)
            adapter = shopRegionFilterAdapter
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_shop_list
    }

    override fun getViewModel(): ShopListViewModel {
        shopListViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(ShopListViewModel::class.java)
        shopListViewModel.getCakeShopList().observe(this, Observer {
            shopListAdapter.setShopListItems(it)
        })
        return shopListViewModel
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btn_filter_refresh_shop_list -> {
                visibleFilterList(false, REFRESH_FILTER_CODE)
                visiblePickUpCalendar(false)
                tv_filter_default_title_shop_list.text = getString(R.string.shop_list_filter_recommend)
                tv_filter_pickup_date_title_shop_list.text = getString(R.string.shop_list_filter_pickup_availability_date)
                tv_filter_pickup_date_title_shop_list.text = getString(R.string.shop_list_filter_pickup_availability_date)
            }
            R.id.btn_filter_default_shop_list -> {
                if(btn_filter_default_shop_list.isSelected) {
                    visibleFilterList(false, DEFAULT_FILTER_CODE)
                }
                else {
                    setFilterItem(0)
                    visibleFilterList(true, DEFAULT_FILTER_CODE)
                }
                btn_filter_default_shop_list.isSelected = !btn_filter_default_shop_list.isSelected
            }
            R.id.btn_filter_pickup_region_shop_list -> {
                if(btn_filter_pickup_region_shop_list.isSelected) {
                    visibleFilterList(false, REGION_FILTER_CODE)
                     }
                else {
                    setFilterItem(1)
                    visibleFilterList(true, REGION_FILTER_CODE)
                }
                btn_filter_pickup_region_shop_list.isSelected = !btn_filter_pickup_region_shop_list.isSelected
            }
            R.id.btn_filter_pickup_date_shop_list -> {
                if(btn_filter_pickup_date_shop_list.isSelected) {
                    visiblePickUpCalendar(false)
                }
                else {
                    setFilterItem(2)
                    visiblePickUpCalendar(true)
                }
                btn_filter_pickup_date_shop_list.isSelected = !btn_filter_pickup_date_shop_list.isSelected
            }
        }
    }

    // 필터 리스트 세팅
    fun setFilterItem(position : Int) {

        when(position) {
            // 기본순, 찜순, 가격 낮은 순 필터
            0 -> {
                filterItems = ArrayList<String>()
                filterItems.add("기본순")
                filterItems.add("찜순")
                filterItems.add("가격 낮은 순")
                shopDefaultFilterAdapter.setDefaultListItems(filterItems)
            }
            // 픽업 지역 필터
            1 -> {
                regionItems = ArrayList<String>()
                for(i in 0 .. regionList.size - 1) {
                    regionItems.add(regionList[i])
                }
                shopRegionFilterAdapter.setRegionListItems(regionItems)
            }
            // 픽업 가능 날짜 필터
            2 -> {
            }
        }
    }

    fun visibleFilterList(choice : Boolean, filterCode : Int) {

        if(filterCode == REFRESH_FILTER_CODE) {
            cl_filter_content_shop_list.visibility = View.GONE
            rv_filter_default_list_shop_list.visibility = View.GONE
            rv_filter_region_list_shop_list.visibility = View.GONE

            btn_filter_default_shop_list.isSelected =  false
            btn_filter_pickup_region_shop_list.isSelected =  false

            tv_filter_default_title_shop_list.setTextColor(Color.parseColor("#242424"))
            btn_filter_default_compact_shop_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
            tv_filter_pickup_region_title_shop_list.setTextColor(Color.parseColor("#242424"))
            btn_filter_pickup_region_compact_shop_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
        }
        else {
            if(filterCode == DEFAULT_FILTER_CODE) {
                if(choice == true) {
                    btn_filter_pickup_region_shop_list.isSelected =  false
                    cl_filter_content_shop_list.visibility = View.VISIBLE
                    rv_filter_default_list_shop_list.visibility = View.VISIBLE

                    rv_filter_region_list_shop_list.visibility = View.GONE
                    tv_filter_default_title_shop_list.setTextColor(Color.parseColor("#577399"))
                    btn_filter_default_compact_shop_list.setBackground(ContextCompat.getDrawable(this, R.drawable.chevron_compact_up))

                    tv_filter_pickup_region_title_shop_list.setTextColor(Color.parseColor("#242424"))
                    btn_filter_pickup_region_compact_shop_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
                }
                else {
                    cl_filter_content_shop_list.visibility = View.GONE
                    rv_filter_default_list_shop_list.visibility = View.GONE

                    tv_filter_default_title_shop_list.setTextColor(Color.parseColor("#242424"))
                    btn_filter_default_compact_shop_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
                }
            }
            else {
                if(choice == true) {
                    btn_filter_default_shop_list.isSelected =  false
                    cl_filter_content_shop_list.visibility = View.VISIBLE
                    rv_filter_region_list_shop_list.visibility = View.VISIBLE

                    rv_filter_default_list_shop_list.visibility = View.GONE
                    tv_filter_pickup_region_title_shop_list.setTextColor(Color.parseColor("#577399"))
                    btn_filter_pickup_region_compact_shop_list.setBackground(ContextCompat.getDrawable(this, R.drawable.chevron_compact_up))

                    tv_filter_default_title_shop_list.setTextColor(Color.parseColor("#242424"))
                    btn_filter_default_compact_shop_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
                }
                else {
                    cl_filter_content_shop_list.visibility = View.GONE
                    rv_filter_region_list_shop_list.visibility = View.GONE

                    tv_filter_pickup_region_title_shop_list.setTextColor(Color.parseColor("#242424"))
                    btn_filter_pickup_region_compact_shop_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
                }
            }
        }
        btn_filter_pickup_date_shop_list.isSelected =  false
        cv_pickup_calendar_shop_list.visibility = View.GONE
        tv_filter_pickup_date_title_shop_list.setTextColor(Color.parseColor("#242424"))
        btn_filter_pickup_date_compact_shop_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
    }

    fun visiblePickUpCalendar(choice: Boolean) {
        if(choice == true) {
            cl_filter_content_shop_list.visibility = View.VISIBLE
            cv_pickup_calendar_shop_list.visibility = View.VISIBLE

            tv_filter_pickup_date_title_shop_list.setTextColor(Color.parseColor("#577399"))
            btn_filter_pickup_date_compact_shop_list.setBackground(ContextCompat.getDrawable(this, R.drawable.chevron_compact_up))
        }
        else {
            cl_filter_content_shop_list.visibility = View.GONE
            cv_pickup_calendar_shop_list.visibility = View.GONE

            tv_filter_pickup_date_title_shop_list.setTextColor(Color.parseColor("#242424"))
            btn_filter_pickup_date_compact_shop_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
        }
        btn_filter_default_shop_list.isSelected =  false
        btn_filter_pickup_region_shop_list.isSelected =  false
        rv_filter_default_list_shop_list.visibility = View.GONE
        rv_filter_region_list_shop_list.visibility = View.GONE
        tv_filter_default_title_shop_list.setTextColor(Color.parseColor("#242424"))
        btn_filter_default_compact_shop_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
        tv_filter_pickup_region_title_shop_list.setTextColor(Color.parseColor("#242424"))
        btn_filter_pickup_region_compact_shop_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
    }
}