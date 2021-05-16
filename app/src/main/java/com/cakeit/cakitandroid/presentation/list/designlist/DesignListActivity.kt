package com.cakeit.cakitandroid.presentation.list.designlist

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseActivity
import com.cakeit.cakitandroid.data.source.local.entity.CakeDesignData
import com.cakeit.cakitandroid.data.source.local.entity.CakeDesignSize
import com.cakeit.cakitandroid.databinding.ActivityDesignListBinding
import com.cakeit.cakitandroid.domain.model.CakeSizeAndrPrice
import com.cakeit.cakitandroid.presentation.list.designlist.filter.*
import kotlinx.android.synthetic.main.activity_design_list.*
import kotlin.collections.ArrayList

class DesignListActivity : BaseActivity<ActivityDesignListBinding, DesignListViewModel>(),
    View.OnClickListener {

    lateinit var designListViewModel: DesignListViewModel
    lateinit var designListBinding: ActivityDesignListBinding

    private lateinit var designListAdapter: DesignListAdapter
    private lateinit var designDefaultFilterAdapter: DesignDefaultFilterAdapter
    private lateinit var designRegionFilterAdapter: DesignRegionFilterAdapter
    private lateinit var designSizeFilterAdapter: DesignSizeFilterAdapter
    private lateinit var designColorFilterAdapter: DesignColorFilterAdapter
    private lateinit var designCategoryFilterAdapter: DesignCategoryFilterAdapter

    private val designTagItems : ArrayList<String> = ArrayList()
    private val designSizeAndPriceItems : ArrayList<CakeSizeAndrPrice> = ArrayList()
    private lateinit var filterItems : ArrayList<String>
    private lateinit var regionItems : ArrayList<String>
    private lateinit var colorItems : ArrayList<String>
    private lateinit var categoryItems : ArrayList<String>

    private val DEFAULT_FILTER_CODE : Int = 0
    private val REGION_FILTER_CODE : Int = 1
    private val SIZE_FITLER_CODE : Int = 2
    private val COLOR_FILTER_CODE : Int = 3
    private val CATEGORY_FILTER_CODE : Int = 4
    private val REFRESH_FILTER_CODE : Int = 5

    private val regionList = listOf<String>("전체", "강남구", "관악구", "광진구", "마포구", "서대문구"
    , "송파구", "노원구", "성북구", "중구", "중랑구")
    private var designSizeItems = ArrayList<CakeDesignSize>()
    private val colorList = listOf<String>("전체", "화이트", "블랙", "핑크", "옐로우", "레드", "블루", "퍼플", "기타")
    private val categoryList = listOf<String>("전체", "문구", "이미지", "캐릭터", "개성")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding()
        designListBinding = getViewDataBinding()
        designListBinding.viewModel = getViewModel()

        designTagItems.add("태그1")
        designTagItems.add("태그2")
        designSizeAndPriceItems.add(CakeSizeAndrPrice("미니", 18000))
        designSizeAndPriceItems.add(CakeSizeAndrPrice("1호", 34000))

        btn_filter_refresh_design_list.setOnClickListener(this)
        btn_filter_default_design_list.setOnClickListener(this)
        btn_filter_pickup_region_design_list.setOnClickListener(this)
        btn_filter_size_design_list.setOnClickListener(this)
        btn_filter_color_design_list.setOnClickListener(this)
        btn_filter_category_design_list.setOnClickListener(this)

        initRecyclerview()

    }

    // 임시 메서드
    fun insertTempData() {
        designListViewModel.insertCakeDesign(CakeDesignData(0,"케이크 매장명1", "서울시 노원구 마들로 31"))
        designListViewModel.insertCakeDesign(CakeDesignData(1, "케이크 매장명2", "서울시 서초구 마들로 31"))
        designListViewModel.insertCakeDesign(CakeDesignData(2, "케이크 매장명3", "서울시 중구 마들로 31"))
        designListViewModel.insertCakeDesign(CakeDesignData(3,  "케이크 매장명4", "서울시 강북구 마들로 31"))
        designListViewModel.insertCakeDesign(CakeDesignData(4, "케이크 매장명5", "서울시 도봉구 마들로 31"))
        designListViewModel.insertCakeDesign(CakeDesignData(5, "케이크 매장명6", "서울시 강남구 마들로 31"))
        designListViewModel.insertCakeDesign(CakeDesignData(6, "케이크 매장명7", "서울시 성북구 마들로 31"))
        designListViewModel.insertCakeDesign(CakeDesignData(7, "케이크 매장명8", "서울시 마포구 마들로 31"))
    }

    fun initRecyclerview() {
        designListAdapter = DesignListAdapter().apply {
            listener = object : DesignListAdapter.OnDesignItemClickListener {
                override fun onDesignItemClick(position: Int) {
                    Toast.makeText(applicationContext, "design list item" + position + " is clicked", Toast.LENGTH_LONG).show()
                }
            }
        }

        designDefaultFilterAdapter = DesignDefaultFilterAdapter()
            .apply {
            listener = object : DesignDefaultFilterAdapter.OnDesignFilterItemClickListener {
                override fun onDesignFilterItemClick(position: Int) {
                    Toast.makeText(applicationContext, "filter item" + position + " is clicked", Toast.LENGTH_LONG).show()
                    tv_filter_default_title_design_list.text = filterItems[position]
                    visibleFilterList(false, DEFAULT_FILTER_CODE)
                    btn_filter_default_design_list.isSelected = false
                }
            }
        }

        designRegionFilterAdapter = DesignRegionFilterAdapter()
            .apply {
            listener = object : DesignRegionFilterAdapter.OnDesignFilterItemClickListener {
                override fun onDesignFilterItemClick(position: Int) {
                    Toast.makeText(applicationContext, "region item" + position + " is clicked", Toast.LENGTH_LONG).show()
                }
            }
        }

        designSizeFilterAdapter = DesignSizeFilterAdapter()
            .apply {
                listener = object : DesignSizeFilterAdapter.OnDesignSizeItemClickListener {
                    override fun onDesignSizeFilterItemClick(position: Int) {
                        Toast.makeText(applicationContext, "size item" + position + " is clicked", Toast.LENGTH_LONG).show()
                    }
                }
            }

        designColorFilterAdapter = DesignColorFilterAdapter()
            .apply {
                listener = object : DesignColorFilterAdapter.OnDesignColorItemClickListener {
                    override fun onDesignColorFilterItemClick(position: Int) {
                        Toast.makeText(applicationContext, "color item" + position + " is clicked", Toast.LENGTH_LONG).show()
                    }
                }
            }

        designCategoryFilterAdapter = DesignCategoryFilterAdapter()
            .apply {
                listener = object : DesignCategoryFilterAdapter.OnDesignCategoryItemClickListener {
                    override fun onDesignCategoryFilterItemClick(position: Int) {
                        Toast.makeText(applicationContext, "cateogory item" + position + " is clicked", Toast.LENGTH_LONG).show()
                        tv_filter_category_title_design_list.text = categoryItems[position]
                        visibleFilterList(false, CATEGORY_FILTER_CODE)
                        btn_filter_category_design_list.isSelected = false
                    }
                }
            }

        rv_design_list_design_list.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@DesignListActivity)
            adapter = designListAdapter
        }

        rv_filter_default_list_design_list.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@DesignListActivity)
            adapter = designDefaultFilterAdapter
        }

        rv_filter_region_list_design_list.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@DesignListActivity)
            adapter = designRegionFilterAdapter
        }

        rv_filter_size_list_design_list.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@DesignListActivity)
            adapter = designSizeFilterAdapter
        }

        rv_filter_color_list_design_list.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@DesignListActivity)
            adapter = designColorFilterAdapter
        }

        rv_filter_category_list_design_list.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@DesignListActivity)
            adapter = designCategoryFilterAdapter
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_design_list
    }

    override fun getViewModel(): DesignListViewModel {
        designListViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(DesignListViewModel::class.java)
        designListViewModel.getCakeDesignList().observe(this, Observer {
            designListAdapter.setDesignListItems(it)
        })
        return designListViewModel
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btn_filter_refresh_design_list -> {
                visibleFilterList(false, REFRESH_FILTER_CODE)
                tv_filter_default_title_design_list.text = "초기화"
                tv_filter_pickup_region_title_design_list.text = "지역"
                tv_filter_size_title_design_list.text = "크기"
                tv_filter_color_title_design_list.text = "색깔"
                tv_filter_category_title_design_list.text = "카테고리"
            }
            R.id.btn_filter_default_design_list -> {
                if(btn_filter_default_design_list.isSelected) {
                    visibleFilterList(false, DEFAULT_FILTER_CODE)
                }
                else {
                    setFilterItem(0)
                    visibleFilterList(true, DEFAULT_FILTER_CODE)
                }
                btn_filter_default_design_list.isSelected = !btn_filter_default_design_list.isSelected
            }
            R.id.btn_filter_pickup_region_design_list -> {
                if(btn_filter_pickup_region_design_list.isSelected) {
                    visibleFilterList(false, REGION_FILTER_CODE)
                     }
                else {
                    setFilterItem(1)
                    visibleFilterList(true, REGION_FILTER_CODE)
                }
                btn_filter_pickup_region_design_list.isSelected = !btn_filter_pickup_region_design_list.isSelected
            }
            R.id.btn_filter_size_design_list -> {
                if(btn_filter_size_design_list.isSelected) {
                    visibleFilterList(false, SIZE_FITLER_CODE)
                }
                else {
                    setFilterItem(2)
                    visibleFilterList(true, SIZE_FITLER_CODE)
                }
                btn_filter_size_design_list.isSelected = !btn_filter_size_design_list.isSelected
            }
            R.id.btn_filter_color_design_list -> {
                if(btn_filter_pickup_region_design_list.isSelected) {
                    visibleFilterList(false, COLOR_FILTER_CODE)
                }
                else {
                    setFilterItem(3)
                    visibleFilterList(true, COLOR_FILTER_CODE)
                }
                btn_filter_color_design_list.isSelected = !btn_filter_color_design_list.isSelected
            }
            R.id.btn_filter_category_design_list -> {
                if(btn_filter_pickup_region_design_list.isSelected) {
                    visibleFilterList(false, CATEGORY_FILTER_CODE)
                }
                else {
                    setFilterItem(4)
                    visibleFilterList(true, CATEGORY_FILTER_CODE)
                }
                btn_filter_category_design_list.isSelected = !btn_filter_category_design_list.isSelected
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
                designDefaultFilterAdapter.setDefaultListItems(filterItems)
            }
            // 픽업 지역 필터
            1 -> {
                regionItems = ArrayList<String>()
                for(i in 0 .. regionList.size - 1) {
                    regionItems.add(regionList[i])
                }
                designRegionFilterAdapter.setRegionListItems(regionItems)
            }
            // 크기 필터
            2 -> {
                designSizeItems = ArrayList<CakeDesignSize>()
                designSizeItems.add(CakeDesignSize("전체", ""))
                designSizeItems.add(CakeDesignSize("미니", "미니 설명"))
                designSizeItems.add(CakeDesignSize("1호", "1호 설명"))
                designSizeItems.add(CakeDesignSize("2호", "2호 설명"))
                designSizeItems.add(CakeDesignSize("3호", "3호 설명"))
                designSizeItems.add(CakeDesignSize("2단", "2단 설명"))
                designSizeFilterAdapter.setDesignSizeItems(designSizeItems)
            }
            // 색깔 필터
            3 -> {
                colorItems = ArrayList<String>()
                for(i in 0 .. colorList.size - 1) {
                    colorItems.add(colorList[i])
                }
                designColorFilterAdapter.setDesignColorItems(colorItems)
            }
            // 카테고리 필터
            4 -> {
                categoryItems = ArrayList<String>()
                for(i in 0 .. categoryList.size - 1) {
                    categoryItems.add(categoryList[i])
                }
                designCategoryFilterAdapter.setDesignCategoryItems(categoryItems)
            }
        }
    }

    fun visibleFilterList(choice : Boolean, filterCode : Int) {

        if(filterCode == REFRESH_FILTER_CODE) {
            cl_filter_content_design_list.visibility = View.GONE
            rv_filter_default_list_design_list.visibility = View.GONE
            rv_filter_region_list_design_list.visibility = View.GONE
            rv_filter_size_list_design_list.visibility = View.GONE
            rv_filter_color_list_design_list.visibility = View.GONE
            rv_filter_category_list_design_list.visibility = View.GONE

            btn_filter_default_design_list.isSelected =  false
            btn_filter_pickup_region_design_list.isSelected =  false
            btn_filter_size_design_list.isSelected =  false
            btn_filter_color_design_list.isSelected =  false
            btn_filter_category_design_list.isSelected =  false

            tv_filter_default_title_design_list.setTextColor(Color.parseColor("#242424"))
            btn_filter_default_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
            tv_filter_pickup_region_title_design_list.setTextColor(Color.parseColor("#242424"))
            btn_filter_pickup_region_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
            tv_filter_size_title_design_list.setTextColor(Color.parseColor("#242424"))
            btn_filter_size_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
            tv_filter_color_title_design_list.setTextColor(Color.parseColor("#242424"))
            btn_filter_color_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
            tv_filter_category_title_design_list.setTextColor(Color.parseColor("#242424"))
            btn_filter_category_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
        }
        else {
            if(filterCode == DEFAULT_FILTER_CODE) {
                if(choice == true) {
                    btn_filter_pickup_region_design_list.isSelected =  false
                    btn_filter_size_design_list.isSelected =  false
                    btn_filter_color_design_list.isSelected =  false
                    btn_filter_category_design_list.isSelected =  false

                    cl_filter_content_design_list.visibility = View.VISIBLE
                    rv_filter_default_list_design_list.visibility = View.VISIBLE

                    rv_filter_region_list_design_list.visibility = View.GONE
                    rv_filter_size_list_design_list.visibility = View.GONE
                    rv_filter_color_list_design_list.visibility = View.GONE
                    rv_filter_category_list_design_list.visibility = View.GONE

                    tv_filter_default_title_design_list.setTextColor(Color.parseColor("#577399"))
                    btn_filter_default_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_up))

                    tv_filter_pickup_region_title_design_list.setTextColor(Color.parseColor("#242424"))
                    btn_filter_pickup_region_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
                    tv_filter_pickup_region_title_design_list.setTextColor(Color.parseColor("#242424"))
                    btn_filter_pickup_region_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
                    tv_filter_size_title_design_list.setTextColor(Color.parseColor("#242424"))
                    btn_filter_size_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
                    tv_filter_color_title_design_list.setTextColor(Color.parseColor("#242424"))
                    btn_filter_color_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
                    tv_filter_category_title_design_list.setTextColor(Color.parseColor("#242424"))
                    btn_filter_category_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))

                }
                else {
                    cl_filter_content_design_list.visibility = View.GONE
                    rv_filter_default_list_design_list.visibility = View.GONE

                    tv_filter_default_title_design_list.setTextColor(Color.parseColor("#242424"))
                    btn_filter_default_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
                }
            }
            else if(filterCode == REGION_FILTER_CODE){
                if(choice == true) {
                    btn_filter_default_design_list.isSelected =  false
                    btn_filter_size_design_list.isSelected =  false
                    btn_filter_color_design_list.isSelected =  false
                    btn_filter_category_design_list.isSelected =  false

                    cl_filter_content_design_list.visibility = View.VISIBLE
                    rv_filter_region_list_design_list.visibility = View.VISIBLE

                    rv_filter_default_list_design_list.visibility = View.GONE
                    rv_filter_size_list_design_list.visibility = View.GONE
                    rv_filter_color_list_design_list.visibility = View.GONE
                    rv_filter_category_list_design_list.visibility = View.GONE

                    tv_filter_pickup_region_title_design_list.setTextColor(Color.parseColor("#577399"))
                    btn_filter_pickup_region_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_up))

                    tv_filter_default_title_design_list.setTextColor(Color.parseColor("#242424"))
                    btn_filter_default_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
                    tv_filter_pickup_region_title_design_list.setTextColor(Color.parseColor("#242424"))
                    btn_filter_pickup_region_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
                    tv_filter_size_title_design_list.setTextColor(Color.parseColor("#242424"))
                    btn_filter_size_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
                    tv_filter_color_title_design_list.setTextColor(Color.parseColor("#242424"))
                    btn_filter_color_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
                    tv_filter_category_title_design_list.setTextColor(Color.parseColor("#242424"))
                    btn_filter_category_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))

                }
                else {
                    cl_filter_content_design_list.visibility = View.GONE
                    rv_filter_region_list_design_list.visibility = View.GONE

                    tv_filter_pickup_region_title_design_list.setTextColor(Color.parseColor("#242424"))
                    btn_filter_pickup_region_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
                }
            }
            else if(filterCode == SIZE_FITLER_CODE){
                if(choice == true) {
                    btn_filter_default_design_list.isSelected =  false
                    btn_filter_pickup_region_design_list.isSelected =  false
                    btn_filter_color_design_list.isSelected =  false
                    btn_filter_category_design_list.isSelected =  false

                    cl_filter_content_design_list.visibility = View.VISIBLE
                    rv_filter_size_list_design_list.visibility = View.VISIBLE

                    rv_filter_default_list_design_list.visibility = View.GONE
                    rv_filter_region_list_design_list.visibility = View.GONE
                    rv_filter_color_list_design_list.visibility = View.GONE
                    rv_filter_category_list_design_list.visibility = View.GONE

                    tv_filter_size_title_design_list.setTextColor(Color.parseColor("#577399"))
                    btn_filter_size_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_up))

                    tv_filter_default_title_design_list.setTextColor(Color.parseColor("#242424"))
                    btn_filter_default_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
                    tv_filter_pickup_region_title_design_list.setTextColor(Color.parseColor("#242424"))
                    btn_filter_pickup_region_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
                    tv_filter_pickup_region_title_design_list.setTextColor(Color.parseColor("#242424"))
                    btn_filter_pickup_region_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
                    tv_filter_color_title_design_list.setTextColor(Color.parseColor("#242424"))
                    btn_filter_color_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
                    tv_filter_category_title_design_list.setTextColor(Color.parseColor("#242424"))
                    btn_filter_category_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))

                }
                else {
                    cl_filter_content_design_list.visibility = View.GONE
                    rv_filter_size_list_design_list.visibility = View.GONE

                    tv_filter_size_title_design_list.setTextColor(Color.parseColor("#242424"))
                    btn_filter_size_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
                }
            }
            else if(filterCode == COLOR_FILTER_CODE){
                if(choice == true) {
                    btn_filter_default_design_list.isSelected =  false
                    btn_filter_pickup_region_design_list.isSelected =  false
                    btn_filter_size_design_list.isSelected =  false
                    btn_filter_category_design_list.isSelected =  false

                    cl_filter_content_design_list.visibility = View.VISIBLE
                    rv_filter_color_list_design_list.visibility = View.VISIBLE

                    rv_filter_default_list_design_list.visibility = View.GONE
                    rv_filter_region_list_design_list.visibility = View.GONE
                    rv_filter_size_list_design_list.visibility = View.GONE
                    rv_filter_category_list_design_list.visibility = View.GONE

                    tv_filter_color_title_design_list.setTextColor(Color.parseColor("#577399"))
                    btn_filter_size_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_up))

                    tv_filter_default_title_design_list.setTextColor(Color.parseColor("#242424"))
                    btn_filter_default_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
                    tv_filter_pickup_region_title_design_list.setTextColor(Color.parseColor("#242424"))
                    btn_filter_pickup_region_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
                    tv_filter_pickup_region_title_design_list.setTextColor(Color.parseColor("#242424"))
                    btn_filter_pickup_region_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
                    tv_filter_color_title_design_list.setTextColor(Color.parseColor("#242424"))
                    btn_filter_color_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
                    tv_filter_category_title_design_list.setTextColor(Color.parseColor("#242424"))
                    btn_filter_category_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))

                }
                else {
                    cl_filter_content_design_list.visibility = View.GONE
                    rv_filter_color_list_design_list.visibility = View.GONE

                    tv_filter_color_title_design_list.setTextColor(Color.parseColor("#242424"))
                    btn_filter_color_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
                }
            }
            else if(filterCode == CATEGORY_FILTER_CODE){
                if(choice == true) {
                    btn_filter_default_design_list.isSelected =  false
                    btn_filter_pickup_region_design_list.isSelected =  false
                    btn_filter_size_design_list.isSelected =  false
                    btn_filter_color_design_list.isSelected =  false

                    cl_filter_content_design_list.visibility = View.VISIBLE
                    rv_filter_category_list_design_list.visibility = View.VISIBLE

                    rv_filter_default_list_design_list.visibility = View.GONE
                    rv_filter_region_list_design_list.visibility = View.GONE
                    rv_filter_size_list_design_list.visibility = View.GONE
                    rv_filter_color_list_design_list.visibility = View.GONE

                    tv_filter_category_title_design_list.setTextColor(Color.parseColor("#577399"))
                    btn_filter_category_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_up))

                    tv_filter_default_title_design_list.setTextColor(Color.parseColor("#242424"))
                    btn_filter_default_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
                    tv_filter_pickup_region_title_design_list.setTextColor(Color.parseColor("#242424"))
                    btn_filter_pickup_region_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
                    tv_filter_size_title_design_list.setTextColor(Color.parseColor("#242424"))
                    btn_filter_size_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
                    tv_filter_color_title_design_list.setTextColor(Color.parseColor("#242424"))
                    btn_filter_color_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
                    tv_filter_pickup_region_title_design_list.setTextColor(Color.parseColor("#242424"))
                    btn_filter_pickup_region_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))

                }
                else {
                    cl_filter_content_design_list.visibility = View.GONE
                    rv_filter_category_list_design_list.visibility = View.GONE

                    tv_filter_category_title_design_list.setTextColor(Color.parseColor("#242424"))
                    btn_filter_category_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
                }
            }
        }
    }
}