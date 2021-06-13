package com.cakeit.cakitandroid.presentation.list.designlist

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseActivity
import com.cakeit.cakitandroid.data.source.local.entity.CakeDesignData
import com.cakeit.cakitandroid.data.source.local.entity.CakeDesignSize
import com.cakeit.cakitandroid.data.source.local.entity.ChoiceTag
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
    private lateinit var designChoiceTagAdapter : DesignChoiceTagAdapter
    private lateinit var designDefaultFilterAdapter: DesignDefaultFilterAdapter
    private lateinit var designRegionFilterAdapter: DesignRegionFilterAdapter
    private lateinit var designSizeFilterAdapter: DesignSizeFilterAdapter
    private lateinit var designColorFilterAdapter: DesignColorFilterAdapter
    private lateinit var designCategoryFilterAdapter: DesignCategoryFilterAdapter

    private val designTagItems: ArrayList<String> = ArrayList()
    private val designSizeAndPriceItems: ArrayList<CakeSizeAndrPrice> = ArrayList()
    private lateinit var filterItems: ArrayList<String>
    private lateinit var regionItems: ArrayList<String>
    private lateinit var colorItems: ArrayList<String>
    private lateinit var categoryItems: ArrayList<String>
    private lateinit var choiceTagItems: ArrayList<ChoiceTag>

    private var clickedPosition = -1;

    private val regionList = listOf<String>("전체", "강남구", "관악구", "광진구", "마포구", "서대문구"
            , "송파구", "노원구", "성북구", "중구", "중랑구")
    private var designSizeItems = ArrayList<CakeDesignSize>()
    private val colorList = listOf<String>("전체", "화이트", "블랙", "핑크", "옐로우", "레드", "블루", "퍼플", "기타")
    private val categoryList = listOf<String>("전체", "문구", "이미지", "캐릭터", "개성")
    private var listSelected = mutableListOf<Boolean>(false, false, false, false, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding()
        designListBinding = getViewDataBinding()
        designListBinding.viewModel = getViewModel()

        designTagItems.add("태그1")
        designTagItems.add("태그2")
        designSizeAndPriceItems.add(CakeSizeAndrPrice("미니", 18000))
        designSizeAndPriceItems.add(CakeSizeAndrPrice("1호", 34000))
        choiceTagItems = ArrayList()

        view_background_design_list.setOnClickListener(this)
        btn_filter_refresh_design_list.setOnClickListener(this)
        btn_filter_default_design_list.setOnClickListener(this)
        btn_filter_pickup_region_design_list.setOnClickListener(this)
        btn_filter_size_design_list.setOnClickListener(this)
        btn_filter_color_design_list.setOnClickListener(this)
        btn_filter_category_design_list.setOnClickListener(this)

        initRecyclerview()
//        insertTempData()
    }

    // 임시 아이템 추가
    fun setItem() {
        var cakeDesignDatas = ArrayList<CakeDesignData>()
        cakeDesignDatas.add(CakeDesignData(0, "강동구", "1호 13cm", "케이크샵1", 32000))
        cakeDesignDatas.add(CakeDesignData(0, "노원구", "1호 13cm", "케이크샵1", 32000))
        cakeDesignDatas.add(CakeDesignData(0, "서초구", "1호 13cm", "케이크샵1", 32000))
        cakeDesignDatas.add(CakeDesignData(0, "강남구", "1호 13cm", "케이크샵1", 32000))
        cakeDesignDatas.add(CakeDesignData(0, "종로구", "1호 13cm", "케이크샵1", 32000))
        cakeDesignDatas.add(CakeDesignData(0, "마포구", "1호 13cm", "케이크샵1", 32000))
        cakeDesignDatas.add(CakeDesignData(0, "도봉구", "1호 13cm", "케이크샵1", 32000))
        designListAdapter.setDesignListItems(cakeDesignDatas)
    }

    // 임시 메서드
    fun insertTempData() {
        designListViewModel.insertCakeDesign(CakeDesignData(100, "노원구", "1호, 13cm", "케이크 매장명1", 33000))
        designListViewModel.insertCakeDesign(CakeDesignData(20, "강남구", "1호, 13cm", "케이크 매장명1", 33000))
        designListViewModel.insertCakeDesign(CakeDesignData(345, "서초구", "1호, 13cm", "케이크 매장명1", 33000))
        designListViewModel.insertCakeDesign(CakeDesignData(41, "마포구", "1호, 13cm", "케이크 매장명1", 33000))
        designListViewModel.insertCakeDesign(CakeDesignData(53, "종로구", "1호, 13cm", "케이크 매장명1", 33000))
        designListViewModel.insertCakeDesign(CakeDesignData(65, "강북구", "1호, 13cm", "케이크 매장명1", 33000))
        designListViewModel.insertCakeDesign(CakeDesignData(66, "강북구", "1호, 13cm", "케이크 매장명1", 33000))
        designListViewModel.insertCakeDesign(CakeDesignData(67, "강북구", "1호, 13cm", "케이크 매장명1", 33000))
        designListViewModel.insertCakeDesign(CakeDesignData(68, "강북구", "1호, 13cm", "케이크 매장명1", 33000))
        designListViewModel.insertCakeDesign(CakeDesignData(69, "강북구", "1호, 13cm", "케이크 매장명1", 33000))
        designListViewModel.insertCakeDesign(CakeDesignData(10, "강북구", "1호, 13cm", "케이크 매장명1", 33000))
        designListViewModel.insertCakeDesign(CakeDesignData(25, "강북구", "1호, 13cm", "케이크 매장명1", 33000))
    }

    fun initRecyclerview() {
//        designListAdapter = DesignListAdapter().apply {
//            listener = object : DesignListAdapter.OnDesignItemClickListener {
//                override fun onDesignItemClick(position: Int) {
//                    Toast.makeText(applicationContext, "design list item" + position + " is clicked", Toast.LENGTH_LONG).show()
//                }
//            }
//        }

        designListAdapter = DesignListAdapter()

        designChoiceTagAdapter = DesignChoiceTagAdapter().apply {

        }

        designDefaultFilterAdapter = DesignDefaultFilterAdapter()
                .apply {
                    listener = object : DesignDefaultFilterAdapter.OnDesignFilterItemClickListener {
                        override fun onDesignFilterItemClick(position: Int) {
                            Toast.makeText(applicationContext, "filter item" + position + " is clicked", Toast.LENGTH_LONG).show()
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
                        }
                    }
                }

        rv_choice_tag_design_list.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@DesignListActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = designChoiceTagAdapter
        }

        rv_design_list_design_list.run {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@DesignListActivity, 2)
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

    fun deleteChoiceTag(index : Int) {

        var backUpTagItems = ArrayList<ChoiceTag>()

        for(i in 0 .. choiceTagItems.size-1) {
            if(choiceTagItems[i].filterCode != index) {
                backUpTagItems.add(choiceTagItems[i])
            }
        }
        choiceTagItems = backUpTagItems
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.view_background_design_list -> {
                Log.d("songjem", "background is touched")
                view_background_design_list.visibility = View.INVISIBLE
                rv_design_list_design_list.visibility = View.VISIBLE
                if(clickedPosition == 0) {
                    btn_filter_default_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_btn_effect))
                    btn_filter_default_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_compact))

                    listSelected[0] = true
                    defaultFilterOff()
                    tv_filter_default_title_design_list.text = designDefaultFilterAdapter.getClickedItem()

                }
                else if(clickedPosition == 1) {
                    btn_filter_pickup_region_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_btn_effect))
                    btn_filter_pickup_region_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_compact))

                    listSelected[1] = true
                    regionFilterOff()

                    // 기존 장소 값(초이스) 지우기
                    deleteChoiceTag(1)

                    // 추가한 리스트 가져와서 리스트에 넣어야 함
                    var tagList = designRegionFilterAdapter.getChoiceTagIndex()

                    // 전체 선택
                    if(tagList[0] == 0) {
                        for(i in 1.. regionList.size-1) {
                            choiceTagItems.add(ChoiceTag(1, i, regionList[i]))
                        }
                    }
                    else {
                        for(i in 0.. tagList.size-1) {
                            choiceTagItems.add(ChoiceTag(1, tagList[i], regionList[tagList[i]]))
                        }
                    }

                    designChoiceTagAdapter.setChoiceTagItem(choiceTagItems)

                }
                else if(clickedPosition == 2) {
                    btn_filter_size_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_btn_effect))
                    btn_filter_size_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_compact))

                    listSelected[2] = true
                    sizeFilterOff()

                    // 기존 장소 값(초이스) 지우기
                    deleteChoiceTag(2)
                    // 추가한 리스트 가져와서 리스트에 넣어야 함
                    var tagList = designSizeFilterAdapter.getChoiceTagIndex()

                    // 전체 선택
                    if(tagList[0] == 0) {
                        for(i in 1.. designSizeItems.size-1) {
                            choiceTagItems.add(ChoiceTag(1, i, designSizeItems[i].sizeName))
                        }
                    }
                    else {
                        for(i in 0.. tagList.size-1) {
                            choiceTagItems.add(ChoiceTag(2, tagList[i], designSizeItems[tagList[i]].sizeName))
                        }
                    }

                    designChoiceTagAdapter.setChoiceTagItem(choiceTagItems)


                }
                else if(clickedPosition == 3) {
                    btn_filter_color_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_btn_effect))
                    btn_filter_color_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_compact))

                    listSelected[3] = true
                    colorFilterOff()

                    // 기존 장소 값(초이스) 지우기
                    deleteChoiceTag(3)
                    // 추가한 리스트 가져와서 리스트에 넣어야 함
                    var tagList = designColorFilterAdapter.getChoiceTagIndex()

                    // 전체 선택
                    if(tagList[0] == 0) {
                        for(i in 1.. colorList.size-1) {
                            choiceTagItems.add(ChoiceTag(1, i, colorList[i]))
                        }
                    }
                    else {
                        for(i in 0.. tagList.size-1) {
                            choiceTagItems.add(ChoiceTag(3, tagList[i], colorList[tagList[i]]))
                        }
                    }

                    designChoiceTagAdapter.setChoiceTagItem(choiceTagItems)

                }
                else if(clickedPosition == 4) {
                    btn_filter_category_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_btn_effect))
                    btn_filter_category_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_compact))

                    listSelected[4] = true
                    categoryFilterOff()

                    // 기존 장소 값(초이스) 지우기
                    deleteChoiceTag(4)
                    // 추가한 리스트 가져와서 리스트에 넣어야 함
                    var tagList = designCategoryFilterAdapter.getChoiceTagIndex()

                    // 전체 선택
                    if(tagList[0] == 0) {
                        for(i in 1.. categoryList.size-1) {
                            choiceTagItems.add(ChoiceTag(1, i, categoryList[i]))
                        }
                    }
                    else {
                        for(i in 0.. tagList.size-1) {
                            choiceTagItems.add(ChoiceTag(4, tagList[i], categoryList[tagList[i]]))
                        }
                    }

                    designChoiceTagAdapter.setChoiceTagItem(choiceTagItems)
                }
            }
            R.id.btn_filter_refresh_design_list -> {
                view_background_design_list.visibility = View.INVISIBLE
                rv_design_list_design_list.visibility = View.VISIBLE

                for(i in 0 .. 4) {
                    listSelected[i] = false
                    cl_filter_content_design_list.visibility = View.GONE
                    rv_filter_default_list_design_list.visibility = View.GONE

                    btn_filter_default_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_btn))
                    btn_filter_default_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_compact_before))
                    btn_filter_default_design_list.isSelected = false
                    btn_filter_default_compact_design_list.isSelected = false
                    tv_filter_default_title_design_list.setTextColor(Color.parseColor("#000000"))
                    tv_filter_default_title_design_list.text = "기본순"
                    
                    btn_filter_pickup_region_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_btn))
                    btn_filter_pickup_region_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_compact_before))
                    btn_filter_pickup_region_design_list.isSelected = false
                    btn_filter_pickup_region_compact_design_list.isSelected = false
                    tv_filter_pickup_region_title_design_list.setTextColor(Color.parseColor("#000000"))
                    tv_filter_pickup_region_title_design_list.text = "픽업 지역"
                    
                    btn_filter_size_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_btn))
                    btn_filter_size_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_compact_before))
                    btn_filter_size_design_list.isSelected = false
                    btn_filter_size_compact_design_list.isSelected = false
                    tv_filter_size_title_design_list.setTextColor(Color.parseColor("#000000"))
                    tv_filter_size_title_design_list.text = "크기"

                    btn_filter_color_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_btn))
                    btn_filter_color_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_compact_before))
                    btn_filter_color_design_list.isSelected = false
                    btn_filter_color_compact_design_list.isSelected = false
                    tv_filter_color_title_design_list.setTextColor(Color.parseColor("#000000"))
                    tv_filter_color_title_design_list.text = "색깔"
                        
                    btn_filter_category_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_btn))
                    btn_filter_category_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_compact_before))
                    btn_filter_category_design_list.isSelected = false
                    btn_filter_category_compact_design_list.isSelected = false
                    tv_filter_category_title_design_list.setTextColor(Color.parseColor("#000000"))
                    tv_filter_category_title_design_list.text = "카테고리"

                }

                choiceTagItems = ArrayList()
                designChoiceTagAdapter.setChoiceTagItem(choiceTagItems)
            }
            R.id.btn_filter_default_design_list -> {
                if(!btn_filter_default_design_list.isSelected) {

                    regionFilterOff()
                    sizeFilterOff()
                    colorFilterOff()
                    categoryFilterOff()
                    defaultFilterOn()
                    view_background_design_list.visibility = View.VISIBLE
                    rv_design_list_design_list.visibility = View.GONE
                }
                else {
                    defaultFilterOff()
                    view_background_design_list.visibility = View.INVISIBLE
                    rv_design_list_design_list.visibility = View.VISIBLE
                }
            }
            R.id.btn_filter_pickup_region_design_list -> {
                if(!btn_filter_pickup_region_design_list.isSelected) {
                    defaultFilterOff()
                    sizeFilterOff()
                    colorFilterOff()
                    categoryFilterOff()
                    regionFilterOn()
                    view_background_design_list.visibility = View.VISIBLE
                    rv_design_list_design_list.visibility = View.GONE
                }
                else {
                    regionFilterOff()
                    view_background_design_list.visibility = View.INVISIBLE
                    rv_design_list_design_list.visibility = View.VISIBLE
                }
            }
            R.id.btn_filter_size_design_list -> {
                if(!btn_filter_size_design_list.isSelected) {
                    defaultFilterOff()
                    regionFilterOff()
                    colorFilterOff()
                    categoryFilterOff()
                    sizeFilterOn()
                    view_background_design_list.visibility = View.VISIBLE
                    rv_design_list_design_list.visibility = View.GONE
                }
                else {
                    sizeFilterOff()
                    view_background_design_list.visibility = View.INVISIBLE
                    rv_design_list_design_list.visibility = View.VISIBLE
                }
            }
            R.id.btn_filter_color_design_list -> {
                if(!btn_filter_color_design_list.isSelected) {
                    defaultFilterOff()
                    regionFilterOff()
                    sizeFilterOff()
                    categoryFilterOff()
                    colorFilterOn()
                    view_background_design_list.visibility = View.VISIBLE
                    rv_design_list_design_list.visibility = View.GONE
                }
                else {
                    colorFilterOff()
                    view_background_design_list.visibility = View.INVISIBLE
                    rv_design_list_design_list.visibility = View.VISIBLE
                }
            }
            R.id.btn_filter_category_design_list -> {
                if(!btn_filter_category_design_list.isSelected) {
                    defaultFilterOff()
                    regionFilterOff()
                    sizeFilterOff()
                    colorFilterOff()
                    categoryFilterOn()
                    view_background_design_list.visibility = View.VISIBLE
                    rv_design_list_design_list.visibility = View.GONE
                }
                else {
                    categoryFilterOff()
                    view_background_design_list.visibility = View.INVISIBLE
                    rv_design_list_design_list.visibility = View.VISIBLE
                }
            }
        }
    }

    fun disabledItemBtn() {
//        design

    }

    // 기본순 필터링 ON
    fun defaultFilterOn() {
        setFilterItem(0)
        tv_filter_default_title_design_list.setTextColor(Color.parseColor("#577399"))
        cl_filter_content_design_list.visibility = View.VISIBLE
        rv_filter_default_list_design_list.visibility = View.VISIBLE
        btn_filter_default_design_list.isSelected = true
        btn_filter_default_compact_design_list.isSelected = true

        clickedPosition = 0
    }

    // 기본순 필터링 OFF
    fun defaultFilterOff() {
        cl_filter_content_design_list.visibility = View.GONE
        rv_filter_default_list_design_list.visibility = View.GONE
        btn_filter_default_design_list.isSelected = false
        btn_filter_default_compact_design_list.isSelected = false

        if(listSelected[0] == false){
            tv_filter_default_title_design_list.setTextColor(Color.parseColor("#000000"))
        }
        else {
            tv_filter_default_title_design_list.setTextColor(Color.parseColor("#ffffff"))
        }
    }

    // 지역별 필터링 ON
    fun regionFilterOn() {
        setFilterItem(1)
        tv_filter_pickup_region_title_design_list.setTextColor(Color.parseColor("#577399"))

        cl_filter_content_design_list.visibility = View.VISIBLE
        rv_filter_region_list_design_list.visibility = View.VISIBLE
        btn_filter_pickup_region_design_list.isSelected = true
        btn_filter_pickup_region_compact_design_list.isSelected = true

        clickedPosition = 1
    }

    // 지역별 필터링 OFF
    fun regionFilterOff() {
        cl_filter_content_design_list.visibility = View.GONE
        rv_filter_region_list_design_list.visibility = View.GONE
        btn_filter_pickup_region_design_list.isSelected = false
        btn_filter_pickup_region_compact_design_list.isSelected = false

        if(listSelected[1] == false){
            tv_filter_pickup_region_title_design_list.setTextColor(Color.parseColor("#000000"))
        }
        else {
            tv_filter_pickup_region_title_design_list.setTextColor(Color.parseColor("#ffffff"))
        }
    }

    // 크기별 필터링 ON
    fun sizeFilterOn() {
        setFilterItem(2)
        tv_filter_size_title_design_list.setTextColor(Color.parseColor("#577399"))

        cl_filter_content_design_list.visibility = View.VISIBLE
        rv_filter_size_list_design_list.visibility = View.VISIBLE
        btn_filter_size_design_list.isSelected = true
        btn_filter_size_compact_design_list.isSelected = true

        clickedPosition = 2
    }

    // 크기별 필터링 OFF
    fun sizeFilterOff() {
        cl_filter_content_design_list.visibility = View.GONE
        rv_filter_size_list_design_list.visibility = View.GONE
        btn_filter_size_design_list.isSelected = false
        btn_filter_size_compact_design_list.isSelected = false

        if(listSelected[2] == false){
            tv_filter_size_title_design_list.setTextColor(Color.parseColor("#000000"))
        }
        else {
            tv_filter_size_title_design_list.setTextColor(Color.parseColor("#ffffff"))
        }
    }

    // 색깔별 필터링 ON
    fun colorFilterOn() {
        setFilterItem(3)
        tv_filter_color_title_design_list.setTextColor(Color.parseColor("#577399"))

        cl_filter_content_design_list.visibility = View.VISIBLE
        rv_filter_color_list_design_list.visibility = View.VISIBLE
        btn_filter_color_design_list.isSelected = true
        btn_filter_color_compact_design_list.isSelected = true

        clickedPosition = 3
    }

    // 색깔별 필터링 OFF
    fun colorFilterOff() {
        cl_filter_content_design_list.visibility = View.GONE
        rv_filter_color_list_design_list.visibility = View.GONE
        btn_filter_color_design_list.isSelected = false
        btn_filter_color_compact_design_list.isSelected = false

        if(listSelected[3] == false){
            tv_filter_color_title_design_list.setTextColor(Color.parseColor("#000000"))
        }
        else {
            tv_filter_color_title_design_list.setTextColor(Color.parseColor("#ffffff"))
        }
    }

    // 카테고리별 필터링 ON
    fun categoryFilterOn() {
        setFilterItem(4)
        tv_filter_category_title_design_list.setTextColor(Color.parseColor("#577399"))

        cl_filter_content_design_list.visibility = View.VISIBLE
        rv_filter_category_list_design_list.visibility = View.VISIBLE
        btn_filter_category_design_list.isSelected = true
        btn_filter_category_compact_design_list.isSelected = true

        clickedPosition = 4
    }

    // 카테고리별 필터링 OFF
    fun categoryFilterOff() {
        cl_filter_content_design_list.visibility = View.GONE
        rv_filter_category_list_design_list.visibility = View.GONE
        btn_filter_category_design_list.isSelected = false
        btn_filter_category_compact_design_list.isSelected = false

        if(listSelected[4] == false){
            tv_filter_category_title_design_list.setTextColor(Color.parseColor("#000000"))
        }
        else {
            tv_filter_category_title_design_list.setTextColor(Color.parseColor("#ffffff"))
        }
    }

    // 필터 리스트 세팅
    fun setFilterItem(position: Int) {

        when (position) {
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
                for (i in 0..regionList.size - 1) {
                    regionItems.add(regionList[i])
                }
                designRegionFilterAdapter.setRegionListItems(regionItems)
            }
            // 크기 필터
            2 -> {
                designSizeItems = ArrayList<CakeDesignSize>()
                designSizeItems.add(CakeDesignSize(0, "전체", ""))
                designSizeItems.add(CakeDesignSize(1, "미니", "미니 설명"))
                designSizeItems.add(CakeDesignSize(2, "1호", "1호 설명"))
                designSizeItems.add(CakeDesignSize(3, "2호", "2호 설명"))
                designSizeItems.add(CakeDesignSize(4, "3호", "3호 설명"))
                designSizeItems.add(CakeDesignSize(5, "2단", "2단 설명"))
                designSizeFilterAdapter.setDesignSizeItems(designSizeItems)
            }
            // 색깔 필터
            3 -> {
                colorItems = ArrayList<String>()
                for (i in 0..colorList.size - 1) {
                    colorItems.add(colorList[i])
                }
                designColorFilterAdapter.setDesignColorItems(colorItems)
            }
            // 카테고리 필터
            4 -> {
                categoryItems = ArrayList<String>()
                for (i in 0..categoryList.size - 1) {
                    categoryItems.add(categoryList[i])
                }
                designCategoryFilterAdapter.setDesignCategoryItems(categoryItems)
            }
        }
    }
}