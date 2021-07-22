package com.cakeit.cakitandroid.presentation.search.searchlist.design

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
import com.cakeit.cakitandroid.base.BaseFragment
import com.cakeit.cakitandroid.data.source.local.entity.CakeDesignSize
import com.cakeit.cakitandroid.data.source.local.entity.ChoiceTag
import com.cakeit.cakitandroid.databinding.FragmentSearchDesignBinding
import com.cakeit.cakitandroid.presentation.design.DesignDetailActivity
import com.cakeit.cakitandroid.presentation.list.designlist.DesignListAdapter
import com.cakeit.cakitandroid.presentation.search.searchlist.design.filter.*
import kotlinx.android.synthetic.main.fragment_search_design.*
class SearchDesignFragment : BaseFragment<FragmentSearchDesignBinding, SearchDesignViewModel>(),  View.OnClickListener {

    lateinit var binding : FragmentSearchDesignBinding
    lateinit var searchDesignViewModel : SearchDesignViewModel

    lateinit var designListAdapter: DesignListAdapter
    lateinit var searchDesignChoiceTagAdapter : SearchDesignChoiceTagAdapter
    lateinit var searchDesignDefaultAdapter: SearchDesignDefaultAdapter
    lateinit var searchDesignRegionAdapter: SearchDesignRegionAdapter
    lateinit var searchDesignSizeAdapter: SearchDesignSizeAdapter
    lateinit var searchDesignColorAdapter: SearchDesignColorAdapter
    lateinit var searchDesignCategoryAdapter: SearchDesignCategoryAdapter

    private lateinit var regionItems: ArrayList<String>
    private lateinit var colorItems: ArrayList<String>
    private lateinit var categoryItems: ArrayList<String>
    lateinit var choiceTagItems: ArrayList<ChoiceTag>
    private lateinit var filterItems: ArrayList<String>

    private var clickedPosition = -1

    private val filterList = listOf<String>("기본순", "찜순", "가격 높은 순", "가격 낮은 순")
    private val filterTransList = listOf<String>("DEFAULT", "ZZIM", "HIGH_PRICE", "LOW_PRICE")
    private val regionList = listOf<String>("전체", "강남구", "관악구", "광진구", "마포구", "서대문구"
        , "송파구", "노원구", "성북구", "중구", "중랑구")
    private var designSizeItems = ArrayList<CakeDesignSize>()
    private val colorList = listOf<String>("전체", "화이트", "블랙", "핑크", "옐로우", "레드", "블루", "퍼플", "기타")
    private val colorTransList = listOf<String>("ALL", "WHITE", "BLACK", "PINK", "YELLOW", "RED", "BLUE", "PURPLE", "OTHER")
    private val categoryList = listOf<String>("전체", "문구", "이미지", "캐릭터", "개성")
    private val categoryTransList = listOf<String>("ALL", "WORDING", "IMAGE", "CHARACTERS", "INDIVIDUALITY")
    var listSelected = mutableListOf<Boolean>(false, false, false, false, false)

    lateinit var selecedLocList : ArrayList<String>
    lateinit var seleceSizeList : ArrayList<String>
    lateinit var selecedColorList : ArrayList<String>
    lateinit var selecedCategoryList : ArrayList<String>
    var selectedTheme : String? = null
    var selectedOrder : String = ""
    lateinit var keyword : String

    lateinit var searchCakeDesignIds : ArrayList<Long>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = getViewDataBinding()
        binding.viewModel = getViewModel()

        choiceTagItems = ArrayList()
        var extra = this.arguments
        extra = getArguments();
        keyword = extra!!.getString("keyword")!!

        view_background_search_design.setOnClickListener(this)
        btn_filter_default_search_design.setOnClickListener(this)
        btn_filter_pickup_region_search_design.setOnClickListener(this)
        btn_filter_size_search_design.setOnClickListener(this)
        btn_filter_color_search_design.setOnClickListener(this)
        btn_filter_category_search_design.setOnClickListener(this)

        initRecyclerview()

        searchDesignViewModel.cakeDesignItems.observe(viewLifecycleOwner, Observer { datas ->
            searchCakeDesignIds = ArrayList<Long>()
            if(datas.size > 0) {
                for(data in datas) {
                    searchCakeDesignIds.add(data.designIndex!!)
                }
                rl_search_design_not_empty.visibility = View.VISIBLE
                rl_search_design_empty.visibility = View.GONE
            }
            else {
                rl_search_design_not_empty.visibility = View.GONE
                rl_search_design_empty.visibility = View.VISIBLE
                Log.d("songjem", "get search designs size == 0")
            }
            designListAdapter.setDesignListItems(datas)
        })
        searchDesignFragment = this
        getSearchDesign()
    }


    fun getSearchDesign() {

        selectedTheme = "NONE"
        selecedLocList = ArrayList<String>()
        seleceSizeList = ArrayList<String>()
        selecedColorList = ArrayList<String>()
        selecedCategoryList = ArrayList<String>()
        selectedOrder = "DEFAULT"

        Log.d("songjem", "theme = " + selectedTheme)
        Log.d("songjem", "locList = " + selecedLocList.toString())
        Log.d("songjem", "sizeList = " + seleceSizeList.toString())
        Log.d("songjem", "colorList = " + selecedColorList.toString())
        Log.d("songjem", "categoryList = " + selecedCategoryList.toString())
        Log.d("songjem", "order = " + selectedOrder)
        searchDesignViewModel.sendParamsForSearchDesign(keyword, keyword, selectedTheme, selecedLocList, seleceSizeList, selecedColorList, selecedCategoryList, selectedOrder)
    }

    fun initRecyclerview() {

        designListAdapter = DesignListAdapter(context!!)
        designListAdapter.setOnItemClickListener(this)
        searchDesignChoiceTagAdapter = SearchDesignChoiceTagAdapter().apply {

        }

        searchDesignDefaultAdapter = SearchDesignDefaultAdapter()
            .apply {
                listener = object : SearchDesignDefaultAdapter.OnDesignFilterItemClickListener {
                    override fun onDesignFilterItemClick(position: Int) {
                        Toast.makeText(context!!, "filter item" + position + " is clicked", Toast.LENGTH_LONG).show()
                    }
                }
            }

        searchDesignRegionAdapter = SearchDesignRegionAdapter()
            .apply {
                listener = object : SearchDesignRegionAdapter.OnDesignFilterItemClickListener {
                    override fun onDesignFilterItemClick(position: Int) {
                        Toast.makeText(context!!, "region item" + position + " is clicked", Toast.LENGTH_LONG).show()
                    }
                }
            }

        searchDesignSizeAdapter = SearchDesignSizeAdapter()
            .apply {
                listener = object : SearchDesignSizeAdapter.OnDesignSizeItemClickListener {
                    override fun onDesignSizeFilterItemClick(position: Int) {
                        Toast.makeText(context!!, "size item" + position + " is clicked", Toast.LENGTH_LONG).show()
                    }
                }
            }

        searchDesignColorAdapter = SearchDesignColorAdapter()
            .apply {
                listener = object : SearchDesignColorAdapter.OnDesignColorItemClickListener {
                    override fun onDesignColorFilterItemClick(position: Int) {
                        Toast.makeText(context!!, "color item" + position + " is clicked", Toast.LENGTH_LONG).show()
                    }
                }
            }

        searchDesignCategoryAdapter = SearchDesignCategoryAdapter()
            .apply {
                listener = object : SearchDesignCategoryAdapter.OnDesignCategoryItemClickListener {
                    override fun onDesignCategoryFilterItemClick(position: Int) {
                        Toast.makeText(context!!, "cateogory item" + position + " is clicked", Toast.LENGTH_LONG).show()
                    }
                }
            }

        rv_choice_tag_search_design.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.HORIZONTAL, false)
            adapter = searchDesignChoiceTagAdapter
        }

        rv_design_list_search_design.run {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context!!, 2)
            adapter = designListAdapter
        }

        rv_filter_default_list_search_design.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!!)
            adapter = searchDesignDefaultAdapter
        }

        rv_filter_region_list_search_design.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!!)
            adapter = searchDesignRegionAdapter
        }

        rv_filter_size_list_search_design.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!!)
            adapter = searchDesignSizeAdapter
        }

        rv_filter_color_list_search_design.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!!)
            adapter = searchDesignColorAdapter
        }

        rv_filter_category_list_search_design.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!!)
            adapter = searchDesignCategoryAdapter
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_search_design
    }

    override fun getViewModel(): SearchDesignViewModel {
        searchDesignViewModel = ViewModelProvider(this).get(SearchDesignViewModel::class.java)
        return searchDesignViewModel
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

    fun getSearchDesignByNetwork(keyword : String, name : String, choiceTagItems : ArrayList<ChoiceTag>) {
        // 데이터 초기화
        selecedLocList = ArrayList<String>()
        seleceSizeList = ArrayList<String>()
        selecedColorList = ArrayList<String>()
        selecedCategoryList = ArrayList<String>()

        // 데이터 가져오기
        for(i in 0.. choiceTagItems.size - 1) {
            // ORDER
            if(choiceTagItems[i].filterCode == 0) {
                selecedLocList.add(filterTransList[choiceTagItems[i].choiceCode])
            }
            // 지역
            else if(choiceTagItems[i].filterCode == 1) {
                // 전체
                if(choiceTagItems[i].choiceCode == 0) {
                    for(i in 1.. choiceTagItems.size - 1) {
                        selecedLocList.add(choiceTagItems[i].choiceName)
                    }
                }
                else selecedLocList.add(choiceTagItems[i].choiceName)
            }
            // 크기
            else if(choiceTagItems[i].filterCode == 2) {
                // 전체
                if(choiceTagItems[i].choiceCode == 0) {
                    for(i in 1.. choiceTagItems.size - 1) {
                        selecedLocList.add(choiceTagItems[i].choiceName)
                    }
                }
                else selecedLocList.add(choiceTagItems[i].choiceName)
            }
            // 색깔
            else if(choiceTagItems[i].filterCode == 3) {
                // 전체
                if(choiceTagItems[i].choiceCode == 0) {
                    for(i in 1.. colorTransList.size - 1) {
                        selecedLocList.add(colorTransList[i])
                    }
                }
                else selecedLocList.add(colorTransList[choiceTagItems[i].choiceCode])
            }
            // 카테고리
            else if(choiceTagItems[i].filterCode == 4) {
                // 전체
                if(choiceTagItems[i].choiceCode == 0) {
                    for(i in 1.. categoryTransList.size - 1) {
                        selecedLocList.add(categoryTransList[i])
                    }
                }
                else selecedLocList.add(categoryTransList[choiceTagItems[i].choiceCode])
            }
        }
        Log.d("songjem", "keyword = " + keyword)
        Log.d("songjem", "theme = " + selectedTheme)
        Log.d("songjem", "locList = " + selecedLocList.toString())
        Log.d("songjem", "sizeList = " + seleceSizeList.toString())
        Log.d("songjem", "colorList = " + selecedColorList.toString())
        Log.d("songjem", "categoryList = " + selecedCategoryList.toString())
        Log.d("songjem", "order = " + selectedOrder)
        searchDesignViewModel.sendParamsForSearchDesign(keyword, keyword, selectedTheme, selecedLocList, seleceSizeList, selecedColorList, selecedCategoryList, selectedOrder)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.view_background_search_design -> {
                Log.d("songjem", "background is touched")
                view_background_search_design.visibility = View.INVISIBLE
                rv_design_list_search_design.visibility = View.VISIBLE
                if(clickedPosition == 0) {
                    btn_filter_default_search_design.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_btn_effect))
                    btn_filter_default_compact_search_design.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_compact))

                    listSelected[0] = true
                    defaultFilterOff()
                    selectedOrder = searchDesignDefaultAdapter.getClickedItem()
                    tv_filter_default_title_search_design.text = selectedOrder

                    if(selectedOrder.equals("기본순")) selectedOrder = "DEFAULT"
                    else if(selectedOrder.equals("찜순")) selectedOrder = "ZZIM"
                    else if(selectedOrder.equals("가격 높은 순")) selectedOrder = "HIGH_PRICE"
                    else if(selectedOrder.equals("가격 낮은 순")) selectedOrder = "LOW_PRICE"

                    getSearchDesignByNetwork(keyword, keyword, choiceTagItems)
                }
                else if(clickedPosition == 1) {
                    btn_filter_pickup_region_search_design.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_btn_effect))
                    btn_filter_pickup_region_compact_search_design.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_compact))

                    listSelected[1] = true
                    regionFilterOff()

                    // 기존 장소 값(초이스) 지우기
                    deleteChoiceTag(1)

                    // 추가한 리스트 가져와서 리스트에 넣어야 함
                    var tagList = searchDesignRegionAdapter.getChoiceTagIndex()

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

                    searchDesignChoiceTagAdapter.setChoiceTagItem(choiceTagItems)
                    getSearchDesignByNetwork(keyword, keyword, choiceTagItems)
                }
                else if(clickedPosition == 2) {
                    btn_filter_size_search_design.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_btn_effect))
                    btn_filter_size_compact_search_design.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_compact))

                    listSelected[2] = true
                    sizeFilterOff()

                    // 기존 장소 값(초이스) 지우기
                    deleteChoiceTag(2)
                    // 추가한 리스트 가져와서 리스트에 넣어야 함
                    var tagList = searchDesignSizeAdapter.getChoiceTagIndex()

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

                    searchDesignChoiceTagAdapter.setChoiceTagItem(choiceTagItems)

                    getSearchDesignByNetwork(keyword, keyword, choiceTagItems)
                }
                else if(clickedPosition == 3) {
                    btn_filter_color_search_design.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_btn_effect))
                    btn_filter_color_compact_search_design.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_compact))

                    listSelected[3] = true
                    colorFilterOff()

                    // 기존 장소 값(초이스) 지우기
                    deleteChoiceTag(3)
                    // 추가한 리스트 가져와서 리스트에 넣어야 함
                    var tagList = searchDesignColorAdapter.getChoiceTagIndex()

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

                    searchDesignChoiceTagAdapter.setChoiceTagItem(choiceTagItems)
                    getSearchDesignByNetwork(keyword, keyword, choiceTagItems)
                }
                else if(clickedPosition == 4) {
                    btn_filter_category_search_design.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_btn_effect))
                    btn_filter_category_compact_search_design.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_compact))

                    listSelected[4] = true
                    categoryFilterOff()

                    // 기존 장소 값(초이스) 지우기
                    deleteChoiceTag(4)
                    // 추가한 리스트 가져와서 리스트에 넣어야 함
                    var tagList = searchDesignCategoryAdapter.getChoiceTagIndex()

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

                    searchDesignChoiceTagAdapter.setChoiceTagItem(choiceTagItems)
                    getSearchDesignByNetwork(keyword, keyword, choiceTagItems)
                }
            }
            R.id.btn_filter_default_search_design -> {
                if(!btn_filter_default_search_design.isSelected) {
                    setFilterItem(0)
                    regionFilterOff()
                    sizeFilterOff()
                    colorFilterOff()
                    categoryFilterOff()
                    defaultFilterOn()
                    view_background_search_design.visibility = View.VISIBLE
                    rv_design_list_search_design.visibility = View.GONE
                }
                else {
                    defaultFilterOff()
                    view_background_search_design.visibility = View.INVISIBLE
                    rv_design_list_search_design.visibility = View.VISIBLE
                }
            }
            R.id.btn_filter_pickup_region_search_design -> {
                if(!btn_filter_pickup_region_search_design.isSelected) {
                    setFilterItem(1)
                    defaultFilterOff()
                    sizeFilterOff()
                    colorFilterOff()
                    categoryFilterOff()
                    regionFilterOn()
                    view_background_search_design.visibility = View.VISIBLE
                    rv_design_list_search_design.visibility = View.GONE
                }
                else {
                    regionFilterOff()
                    view_background_search_design.visibility = View.INVISIBLE
                    rv_design_list_search_design.visibility = View.VISIBLE
                }
            }
            R.id.btn_filter_size_search_design -> {
                if(!btn_filter_size_search_design.isSelected) {
                    setFilterItem(2)
                    defaultFilterOff()
                    regionFilterOff()
                    colorFilterOff()
                    categoryFilterOff()
                    sizeFilterOn()
                    view_background_search_design.visibility = View.VISIBLE
                    rv_design_list_search_design.visibility = View.GONE
                }
                else {
                    sizeFilterOff()
                    view_background_search_design.visibility = View.INVISIBLE
                    rv_design_list_search_design.visibility = View.VISIBLE
                }
            }
            R.id.btn_filter_color_search_design -> {
                if(!btn_filter_color_search_design.isSelected) {
                    setFilterItem(3)
                    defaultFilterOff()
                    regionFilterOff()
                    sizeFilterOff()
                    categoryFilterOff()
                    colorFilterOn()
                    view_background_search_design.visibility = View.VISIBLE
                    rv_design_list_search_design.visibility = View.GONE
                }
                else {
                    colorFilterOff()
                    view_background_search_design.visibility = View.INVISIBLE
                    rv_design_list_search_design.visibility = View.VISIBLE
                }
            }
            R.id.btn_filter_category_search_design -> {
                if(!btn_filter_category_search_design.isSelected) {
                    setFilterItem(4)
                    defaultFilterOff()
                    regionFilterOff()
                    sizeFilterOff()
                    colorFilterOff()
                    categoryFilterOn()
                    view_background_search_design.visibility = View.VISIBLE
                    rv_design_list_search_design.visibility = View.GONE
                }
                else {
                    categoryFilterOff()
                    view_background_search_design.visibility = View.INVISIBLE
                    rv_design_list_search_design.visibility = View.VISIBLE
                }
            }
            else -> {
                var position: Int = rv_design_list_search_design.getChildAdapterPosition(view!!)
                val intent = Intent(context, DesignDetailActivity::class.java)
                Log.d("songjem", "position = " + position + ", cakeDesignID = " + searchCakeDesignIds[position])
                intent.putExtra("designId", searchCakeDesignIds[position].toInt())
                startActivity(intent)
            }
        }
    }
    // 기본순 초기화
    fun clearDefault() {
        btn_filter_default_search_design.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_btn))
        btn_filter_default_compact_search_design.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_compact_before))
        btn_filter_default_search_design.isSelected = false
        btn_filter_default_compact_search_design.isSelected = false
        tv_filter_default_title_search_design.setTextColor(Color.parseColor("#000000"))
        tv_filter_default_title_search_design.text = "기본순"
        searchDesignDefaultAdapter.checkedPosition.clear()
    }
    // 장소 선택 초기화
    fun clearRegion() {
        btn_filter_pickup_region_search_design.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_btn))
        btn_filter_pickup_region_compact_search_design.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_compact_before))
        btn_filter_pickup_region_search_design.isSelected = false
        btn_filter_pickup_region_compact_search_design.isSelected = false
        tv_filter_pickup_region_title_search_design.setTextColor(Color.parseColor("#000000"))
        tv_filter_pickup_region_title_search_design.text = "픽업 지역"
        searchDesignRegionAdapter.checkedPosition.clear()
    }
    // 크기 선택 초기화
    fun clearSize() {
        btn_filter_size_search_design.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_btn))
        btn_filter_size_compact_search_design.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_compact_before))
        btn_filter_size_search_design.isSelected = false
        btn_filter_size_compact_search_design.isSelected = false
        tv_filter_size_title_search_design.setTextColor(Color.parseColor("#000000"))
        tv_filter_size_title_search_design.text = "크기"
        searchDesignSizeAdapter.checkedPosition.clear()
    }
    // 색깔 선택 초기화
    fun clearColor() {
        btn_filter_color_search_design.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_btn))
        btn_filter_color_compact_search_design.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_compact_before))
        btn_filter_color_search_design.isSelected = false
        btn_filter_color_compact_search_design.isSelected = false
        tv_filter_color_title_search_design.setTextColor(Color.parseColor("#000000"))
        tv_filter_color_title_search_design.text = "색깔"
        searchDesignColorAdapter.checkedPosition.clear()
    }
    // 카테고리 초기화
    fun clearCategory() {
        btn_filter_category_search_design.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_btn))
        btn_filter_category_compact_search_design.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_compact_before))
        btn_filter_category_search_design.isSelected = false
        btn_filter_category_compact_search_design.isSelected = false
        tv_filter_category_title_search_design.setTextColor(Color.parseColor("#000000"))
        tv_filter_category_title_search_design.text = "카테고리"
        searchDesignCategoryAdapter.checkedPosition.clear()
    }

    // 기본순 필터링 ON
    fun defaultFilterOn() {
        setFilterItem(0)
        tv_filter_default_title_search_design.setTextColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
        cl_filter_content_search_design.visibility = View.VISIBLE
        rv_filter_default_list_search_design.visibility = View.VISIBLE
        btn_filter_default_search_design.isSelected = true
        btn_filter_default_compact_search_design.isSelected = true

        clickedPosition = 0
    }

    // 기본순 필터링 OFF
    fun defaultFilterOff() {
        cl_filter_content_search_design.visibility = View.GONE
        rv_filter_default_list_search_design.visibility = View.GONE
        btn_filter_default_search_design.isSelected = false
        btn_filter_default_compact_search_design.isSelected = false

        if(listSelected[0] == false){
            tv_filter_default_title_search_design.setTextColor(Color.parseColor("#000000"))
        }
        else {
            tv_filter_default_title_search_design.setTextColor(Color.parseColor("#ffffff"))
        }
    }

    // 지역별 필터링 ON
    fun regionFilterOn() {
        setFilterItem(1)
        tv_filter_pickup_region_title_search_design.setTextColor(ContextCompat.getColor(context!!, R.color.colorPrimary))

        cl_filter_content_search_design.visibility = View.VISIBLE
        rv_filter_region_list_search_design.visibility = View.VISIBLE
        btn_filter_pickup_region_search_design.isSelected = true
        btn_filter_pickup_region_compact_search_design.isSelected = true

        clickedPosition = 1
    }

    // 지역별 필터링 OFF
    fun regionFilterOff() {
        Log.d("songjem", "regionFilterOff")
        cl_filter_content_search_design.visibility = View.GONE
        rv_filter_region_list_search_design.visibility = View.GONE
        btn_filter_pickup_region_search_design.isSelected = false
        btn_filter_pickup_region_compact_search_design.isSelected = false

        if(listSelected[1] == false){
            tv_filter_pickup_region_title_search_design.setTextColor(Color.parseColor("#000000"))
        }
        else {
            tv_filter_pickup_region_title_search_design.setTextColor(Color.parseColor("#ffffff"))
        }
    }

    // 크기별 필터링 ON
    fun sizeFilterOn() {
        setFilterItem(2)
        tv_filter_size_title_search_design.setTextColor(ContextCompat.getColor(context!!, R.color.colorPrimary))

        cl_filter_content_search_design.visibility = View.VISIBLE
        rv_filter_size_list_search_design.visibility = View.VISIBLE
        btn_filter_size_search_design.isSelected = true
        btn_filter_size_compact_search_design.isSelected = true

        clickedPosition = 2
    }

    // 크기별 필터링 OFF
    fun sizeFilterOff() {
        cl_filter_content_search_design.visibility = View.GONE
        rv_filter_size_list_search_design.visibility = View.GONE
        btn_filter_size_search_design.isSelected = false
        btn_filter_size_compact_search_design.isSelected = false

        if(listSelected[2] == false){
            tv_filter_size_title_search_design.setTextColor(Color.parseColor("#000000"))
        }
        else {
            tv_filter_size_title_search_design.setTextColor(Color.parseColor("#ffffff"))
        }
    }

    // 색깔별 필터링 ON
    fun colorFilterOn() {
        setFilterItem(3)
        tv_filter_color_title_search_design.setTextColor(ContextCompat.getColor(context!!, R.color.colorPrimary))

        cl_filter_content_search_design.visibility = View.VISIBLE
        rv_filter_color_list_search_design.visibility = View.VISIBLE
        btn_filter_color_search_design.isSelected = true
        btn_filter_color_compact_search_design.isSelected = true

        clickedPosition = 3
    }

    // 색깔별 필터링 OFF
    fun colorFilterOff() {
        cl_filter_content_search_design.visibility = View.GONE
        rv_filter_color_list_search_design.visibility = View.GONE
        btn_filter_color_search_design.isSelected = false
        btn_filter_color_compact_search_design.isSelected = false

        if(listSelected[3] == false){
            tv_filter_color_title_search_design.setTextColor(Color.parseColor("#000000"))
        }
        else {
            tv_filter_color_title_search_design.setTextColor(Color.parseColor("#ffffff"))
        }
    }

    // 카테고리별 필터링 ON
    fun categoryFilterOn() {
        setFilterItem(4)
        tv_filter_category_title_search_design.setTextColor(ContextCompat.getColor(context!!, R.color.colorPrimary))

        cl_filter_content_search_design.visibility = View.VISIBLE
        rv_filter_category_list_search_design.visibility = View.VISIBLE
        btn_filter_category_search_design.isSelected = true
        btn_filter_category_compact_search_design.isSelected = true

        clickedPosition = 4
    }

    // 카테고리별 필터링 OFF
    fun categoryFilterOff() {
        cl_filter_content_search_design.visibility = View.GONE
        rv_filter_category_list_search_design.visibility = View.GONE
        btn_filter_category_search_design.isSelected = false
        btn_filter_category_compact_search_design.isSelected = false

        if(listSelected[4] == false){
            tv_filter_category_title_search_design.setTextColor(Color.parseColor("#000000"))
        }
        else {
            tv_filter_category_title_search_design.setTextColor(Color.parseColor("#ffffff"))
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
                searchDesignDefaultAdapter.setDefaultListItems(filterItems)
            }
            // 픽업 지역 필터
            1 -> {
                regionItems = ArrayList<String>()
                for (i in 0..regionList.size - 1) {
                    regionItems.add(regionList[i])
                }
                searchDesignRegionAdapter.setRegionListItems(regionItems)
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
                searchDesignSizeAdapter.setDesignSizeItems(designSizeItems)
            }
            // 색깔 필터
            3 -> {
                colorItems = ArrayList<String>()
                for (i in 0..colorList.size - 1) {
                    colorItems.add(colorList[i])
                }
                searchDesignColorAdapter.setDesignColorItems(colorItems)
            }
            // 카테고리 필터
            4 -> {
                categoryItems = ArrayList<String>()
                for (i in 0..categoryList.size - 1) {
                    categoryItems.add(categoryList[i])
                }
                searchDesignCategoryAdapter.setDesignCategoryItems(categoryItems)
            }
        }
    }

    companion object {
        lateinit var searchDesignFragment : SearchDesignFragment
    }
}