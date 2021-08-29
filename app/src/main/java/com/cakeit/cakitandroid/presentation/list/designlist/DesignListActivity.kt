package com.cakeit.cakitandroid.presentation.list.designlist

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
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
import com.cakeit.cakitandroid.presentation.design.DesignDetailActivity
import com.cakeit.cakitandroid.presentation.list.designlist.filter.*
import kotlinx.android.synthetic.main.activity_design_list.*
import kotlinx.android.synthetic.main.fragment_search_design.*
import kotlin.collections.ArrayList

class DesignListActivity : BaseActivity<ActivityDesignListBinding, DesignListViewModel>(),
    View.OnClickListener {

    lateinit var designListViewModel: DesignListViewModel
    lateinit var designListBinding: ActivityDesignListBinding

    lateinit var designListAdapter: DesignListAdapter
    lateinit var designChoiceTagAdapter : DesignChoiceTagAdapter
    lateinit var designDefaultFilterAdapter: DesignDefaultFilterAdapter
    lateinit var designRegionFilterAdapter: DesignRegionFilterAdapter
    lateinit var designSizeFilterAdapter: DesignSizeFilterAdapter
    lateinit var designColorFilterAdapter: DesignColorFilterAdapter
    lateinit var designCategoryFilterAdapter: DesignCategoryFilterAdapter

    private lateinit var regionItems: ArrayList<String>
    private lateinit var colorItems: ArrayList<String>
    private lateinit var categoryItems: ArrayList<String>
    lateinit var choiceTagItems: ArrayList<ChoiceTag>
    private lateinit var filterItems: ArrayList<String>

    private var clickedPosition = -1

    private val filterList = listOf<String>("기본순", "찜순", "가격 낮은 순", "인기 많은 순")
    private val filterTransList = listOf<String>("default", "zzim", "cheap", "best")
    private val regionList = listOf<String>("전체", "강남구", "관악구", "광진구", "마포구", "서대문구"
            , "송파구", "노원구", "성북구", "중구", "중랑구")
    private var designSizeItems = ArrayList<CakeDesignSize>()
    private val colorList = listOf<String>("전체", "화이트", "블랙", "핑크", "옐로우", "레드", "블루", "퍼플", "기타")
    private val categoryList = listOf<String>("없음", "문구", "이미지", "캐릭터", "개성")
    var listSelected = mutableListOf<Boolean>(false, false, false, false, false)

    lateinit var selectedLocList : ArrayList<String>
    lateinit var selectedSizeList : ArrayList<String>
    lateinit var selectedColorList : ArrayList<String>
    lateinit var selectedCategoryList : ArrayList<String>
    var selectedTheme : String? = null
    var selectedOrder : String? = null
    lateinit var cakeDesignIds : ArrayList<Long>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding()
        designListBinding = getViewDataBinding()
        designListBinding.viewModel = getViewModel()

        choiceTagItems = ArrayList()

        view_background_design_list.setOnClickListener(this)
        btn_filter_refresh_design_list.setOnClickListener(this)
        btn_filter_default_design_list.setOnClickListener(this)
        btn_filter_pickup_region_design_list.setOnClickListener(this)
        btn_filter_size_design_list.setOnClickListener(this)
        btn_filter_color_design_list.setOnClickListener(this)
        btn_filter_category_design_list.setOnClickListener(this)
        btn_back_design_list.setOnClickListener(this)

        initRecyclerview()
        selectedTheme = intent.getStringExtra("theme")
        if(selectedTheme.equals("입사")) tv_design_title_design_list.text = "입사/승진"
        else if(selectedTheme.equals("퇴사")) tv_design_title_design_list.text = "복직/퇴사"
        else if(selectedTheme.equals("동아리")) tv_design_title_design_list.text = "동아리/모임"
        else tv_design_title_design_list.text = selectedTheme

        designListViewModel.cakeDesignItems.observe(this, Observer { datas ->
            cakeDesignIds = ArrayList<Long>()
            if(datas.size > 0) {
                for(data in datas) {
                    cakeDesignIds.add(data.designIndex!!)
                }
            }
            else {
                Log.d("songjem", "get designList size == 0")
            }
            designListAdapter.setDesignListItems(datas)
         })
        getDesignList()
        designListActivity = this
    }

    fun getDesignList() {

        selectedLocList = ArrayList<String>()
        selectedSizeList = ArrayList<String>()
        selectedColorList = ArrayList<String>()
        selectedCategoryList = ArrayList<String>()

        if(selectedOrder.equals(filterList[0])) selectedOrder = null
        else if(selectedOrder.equals(filterList[1])) selectedOrder = "zzim"
        else if(selectedOrder.equals(filterList[2])) selectedOrder = "cheap"
        else if(selectedOrder.equals(filterList[3])) selectedOrder = "best"

        Log.d("songjem", "theme = " + selectedTheme)
        Log.d("songjem", "locList = " + selectedLocList.toString())
        Log.d("songjem", "sizeList = " + selectedSizeList.toString())
        Log.d("songjem", "colorList = " + selectedColorList.toString())
        Log.d("songjem", "categoryList = " + selectedCategoryList.toString())
        Log.d("songjem", "order = " + selectedOrder)
        designListViewModel.sendParamsForDesignList(selectedTheme, selectedLocList, selectedSizeList, selectedColorList, selectedCategoryList, selectedOrder)
    }

    fun initRecyclerview() {

        designListAdapter = DesignListAdapter(applicationContext)
        designListAdapter.setOnItemClickListener(this)
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

    fun getDesignListByNetwork(choiceTagItems : ArrayList<ChoiceTag>) {
        // 데이터 초기화
        selectedLocList = ArrayList<String>()
        selectedSizeList = ArrayList<String>()
        selectedColorList = ArrayList<String>()
        selectedCategoryList = ArrayList<String>()

        // 데이터 가져오기
        for(i in 0.. choiceTagItems.size - 1) {

            // ORDER
            if(choiceTagItems[i].filterCode == 0) {
                selectedLocList.add(filterTransList[choiceTagItems[i].choiceCode])
            }
            // 지역
            else if(choiceTagItems[i].filterCode == 1) {
                // 전체
                if(choiceTagItems[i].choiceCode == 0) {
                    for(i in 1.. choiceTagItems.size - 1) {
                        selectedLocList.add(choiceTagItems[i].choiceName)
                    }
                }
                else selectedLocList.add(choiceTagItems[i].choiceName)
            }
            // 크기
            else if(choiceTagItems[i].filterCode == 2) {
                // 전체
                if(choiceTagItems[i].choiceCode == 0) {
                    for(i in 1.. choiceTagItems.size - 1) {
                        selectedSizeList.add(choiceTagItems[i].choiceName)
                    }
                }
                else selectedSizeList.add(choiceTagItems[i].choiceName)
            }
            // 색깔
            else if(choiceTagItems[i].filterCode == 3) {
                // 전체
                if(choiceTagItems[i].choiceCode == 0) {
                    for(i in 1.. colorList.size - 1) {
                        selectedColorList.add(colorList[i])
                    }
                }
                else selectedColorList.add(colorList[choiceTagItems[i].choiceCode])
            }
            // 카테고리
            else if(choiceTagItems[i].filterCode == 4) {
                // 전체
                if(choiceTagItems[i].choiceCode == 0) {
                    for(i in 1.. categoryList.size - 1) {
                        selectedCategoryList.add(categoryList[i])
                    }
                }
                else selectedCategoryList.add(categoryList[choiceTagItems[i].choiceCode])
            }
        }
        if((selectedLocList.size + selectedSizeList.size + selectedColorList.size + selectedCategoryList.size) == 0) sv_choice_tag_design_list.visibility = View.GONE

        Log.d("songjem", "theme = " + selectedTheme)
        Log.d("songjem", "locList = " + selectedLocList.toString())
        Log.d("songjem", "sizeList = " + selectedSizeList.toString())
        Log.d("songjem", "colorList = " + selectedColorList.toString())
        Log.d("songjem", "categoryList = " + selectedCategoryList.toString())
        Log.d("songjem", "order = " + selectedOrder)
        designListViewModel.sendParamsForDesignList(selectedTheme, selectedLocList, selectedSizeList, selectedColorList, selectedCategoryList, selectedOrder)
    }
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_back_design_list -> {
                super.onBackPressed()
            }
            R.id.view_background_design_list -> {
                Log.d("songjem", "background is touched")
                view_background_design_list.visibility = View.INVISIBLE
                rv_design_list_design_list.visibility = View.VISIBLE
                if(clickedPosition == 0) {
                    btn_filter_default_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_btn_effect))
                    btn_filter_default_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_compact))

                    listSelected[0] = true
                    defaultFilterOff()
                    selectedOrder = designDefaultFilterAdapter.getClickedItem()
                    tv_filter_default_title_design_list.text = selectedOrder

                    if(selectedOrder.equals(filterList[0])) selectedOrder = null
                    else if(selectedOrder.equals(filterList[1])) selectedOrder = "zzim"
                    else if(selectedOrder.equals(filterList[2])) selectedOrder = "cheap"
                    else if(selectedOrder.equals(filterList[3])) selectedOrder = "best"

                    getDesignListByNetwork(choiceTagItems)
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

                    if(tagList.size > 0) sv_choice_tag_design_list.visibility = View.VISIBLE
                    else sv_choice_tag_design_list.visibility = View.GONE

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
                    getDesignListByNetwork(choiceTagItems)
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

                    if(tagList.size > 0) sv_choice_tag_design_list.visibility = View.VISIBLE
                    else sv_choice_tag_design_list.visibility = View.GONE

                    // 전체 선택
                    if(tagList[0] == 0) {
                        for(i in 1.. designSizeItems.size-1) {
                            choiceTagItems.add(ChoiceTag(2, i, designSizeItems[i].sizeName))
                        }
                    }
                    else {
                        for(i in 0.. tagList.size-1) {
                            choiceTagItems.add(ChoiceTag(2, tagList[i], designSizeItems[tagList[i]].sizeName))
                        }
                    }

                    designChoiceTagAdapter.setChoiceTagItem(choiceTagItems)

                    getDesignListByNetwork(choiceTagItems)
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
                    if(tagList.size > 0) sv_choice_tag_design_list.visibility = View.VISIBLE
                    else sv_choice_tag_design_list.visibility = View.GONE

                    // 전체 선택
                    if(tagList[0] == 0) {
                        for(i in 1.. colorList.size-1) {
                            choiceTagItems.add(ChoiceTag(3, i, colorList[i]))
                        }
                    }
                    else {
                        for(i in 0.. tagList.size-1) {
                            choiceTagItems.add(ChoiceTag(3, tagList[i], colorList[tagList[i]]))
                        }
                    }

                    designChoiceTagAdapter.setChoiceTagItem(choiceTagItems)
                    getDesignListByNetwork(choiceTagItems)
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
                    if(tagList.size > 0) sv_choice_tag_design_list.visibility = View.VISIBLE
                    else sv_choice_tag_design_list.visibility = View.GONE

                    // 전체 선택
                    if(tagList[0] == 0) {
                        for(i in 1.. categoryList.size-1) {
                            choiceTagItems.add(ChoiceTag(4, i, categoryList[i]))
                        }
                    }
                    else {
                        for(i in 0.. tagList.size-1) {
                            choiceTagItems.add(ChoiceTag(4, tagList[i], categoryList[tagList[i]]))
                        }
                    }

                    designChoiceTagAdapter.setChoiceTagItem(choiceTagItems)
                    getDesignListByNetwork(choiceTagItems)
                }
            }
            R.id.btn_filter_refresh_design_list -> {
                view_background_design_list.visibility = View.INVISIBLE
                rv_design_list_design_list.visibility = View.VISIBLE

                for(i in 0 .. 4) {
                    listSelected[i] = false
                }
                cl_filter_content_design_list.visibility = View.GONE
                rv_filter_default_list_design_list.visibility = View.GONE

                clearDefault()
                clearRegion()
                clearSize()
                clearColor()
                clearCategory()

                choiceTagItems = ArrayList()
                designChoiceTagAdapter.setChoiceTagItem(choiceTagItems)
                getDesignListByNetwork(choiceTagItems)
            }
            R.id.btn_filter_default_design_list -> {
                if(!btn_filter_default_design_list.isSelected) {
                    setFilterItem(0)
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
                    setFilterItem(1)
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
                    setFilterItem(2)
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
                    setFilterItem(3)
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
                    setFilterItem(4)
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
            else -> {
                var position: Int = rv_design_list_design_list.getChildAdapterPosition(view!!)
                val intent = Intent(this, DesignDetailActivity::class.java)
                Log.d("songjem", "position = " + position + ", cakeDesignID = " + cakeDesignIds[position])
                intent.putExtra("designId", cakeDesignIds[position].toInt())
                startActivity(intent)
            }
        }
    }
    // 기본순 초기화
    fun clearDefault() {
        btn_filter_default_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_btn_effect_before))
        btn_filter_default_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_compact_before))
        btn_filter_default_design_list.isSelected = false
        btn_filter_default_compact_design_list.isSelected = false
        tv_filter_default_title_design_list.setTextColor(Color.parseColor("#000000"))
        tv_filter_default_title_design_list.text = "기본순"
        designDefaultFilterAdapter.checkedPosition.clear()
        designDefaultFilterAdapter.checkedPosition.add(0)
    }
    // 장소 선택 초기화
    fun clearRegion() {
        btn_filter_pickup_region_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_btn_effect_before))
        btn_filter_pickup_region_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_compact_before))
        btn_filter_pickup_region_design_list.isSelected = false
        btn_filter_pickup_region_compact_design_list.isSelected = false
        tv_filter_pickup_region_title_design_list.setTextColor(Color.parseColor("#000000"))
        tv_filter_pickup_region_title_design_list.text = "픽업 지역"
        designRegionFilterAdapter.checkedPosition.clear()
        designRegionFilterAdapter.checkedPosition.add(0)
    }
    // 크기 선택 초기화
    fun clearSize() {
        btn_filter_size_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_btn_effect_before))
        btn_filter_size_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_compact_before))
        btn_filter_size_design_list.isSelected = false
        btn_filter_size_compact_design_list.isSelected = false
        tv_filter_size_title_design_list.setTextColor(Color.parseColor("#000000"))
        tv_filter_size_title_design_list.text = "크기"
        designSizeFilterAdapter.checkedPosition.clear()
        designSizeFilterAdapter.checkedPosition.add(0)
    }
    // 색깔 선택 초기화
    fun clearColor() {
        btn_filter_color_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_btn_effect_before))
        btn_filter_color_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_compact_before))
        btn_filter_color_design_list.isSelected = false
        btn_filter_color_compact_design_list.isSelected = false
        tv_filter_color_title_design_list.setTextColor(Color.parseColor("#000000"))
        tv_filter_color_title_design_list.text = "색깔"
        designColorFilterAdapter.checkedPosition.clear()
        designColorFilterAdapter.checkedPosition.add(0)
    }
    // 카테고리 초기화
    fun clearCategory() {
        btn_filter_category_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_btn_effect_before))
        btn_filter_category_compact_design_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_compact_before))
        btn_filter_category_design_list.isSelected = false
        btn_filter_category_compact_design_list.isSelected = false
        tv_filter_category_title_design_list.setTextColor(Color.parseColor("#000000"))
        tv_filter_category_title_design_list.text = "카테고리"
        designCategoryFilterAdapter.checkedPosition.clear()
        designCategoryFilterAdapter.checkedPosition.add(0)
    }

    // 기본순 필터링 ON
    fun defaultFilterOn() {
        setFilterItem(0)
        tv_filter_default_title_design_list.setTextColor(Color.parseColor("#df7373"))
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
        tv_filter_pickup_region_title_design_list.setTextColor(Color.parseColor("#df7373"))

        cl_filter_content_design_list.visibility = View.VISIBLE
        rv_filter_region_list_design_list.visibility = View.VISIBLE
        btn_filter_pickup_region_design_list.isSelected = true
        btn_filter_pickup_region_compact_design_list.isSelected = true

        clickedPosition = 1
    }

    // 지역별 필터링 OFF
    fun regionFilterOff() {
        Log.d("songjem", "regionFilterOff")
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
        tv_filter_size_title_design_list.setTextColor(Color.parseColor("#df7373"))

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
        tv_filter_color_title_design_list.setTextColor(Color.parseColor("#df7373"))

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
        tv_filter_category_title_design_list.setTextColor(Color.parseColor("#df7373"))

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
                for (i in 0..filterList.size - 1) {
                    filterItems.add(filterList[i])
                }
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

    companion object {
        lateinit var designListActivity : DesignListActivity
    }
}