package com.cakeit.cakitandroid.presentation.search.searchlist.design

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
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
    private lateinit var colorValItems: ArrayList<Int>
    private lateinit var colorItems: ArrayList<String>
    private lateinit var categoryItems: ArrayList<String>
    lateinit var choiceTagItems: ArrayList<ChoiceTag>
    private lateinit var filterItems: ArrayList<String>

    private var clickedPosition = -1
    var name : String? = null
    var searchListSize = 0

    private val filterList = listOf<String>("기본순", "찜순", "가격 낮은 순")
    private val filterTransList = listOf<String>("DEFAULT", "zzim", "cheap")
    private val regionList = listOf<String>("전체", "강남구", "관악구", "광진구", "마포구", "서대문구", "송파구")
    private var designSizeItems = ArrayList<CakeDesignSize>()
    private val colorValList = listOf<Int>(0, Color.parseColor("#F4F3EF"), Color.BLACK, Color.parseColor("#fb319c"), Color.YELLOW, Color.RED, Color.BLUE, Color.parseColor("#7033AD"), Color.parseColor("#909090"))
    private val colorList = listOf<String>("전체", "화이트", "블랙", "핑크", "옐로우", "레드", "블루", "퍼플", "기타")
    private val categoryList = listOf<String>("전체", "문구", "이미지", "캐릭터", "개성")
    var listSelected = mutableListOf<Boolean>(false, false, false, false, false)

    lateinit var selectedLocList : ArrayList<String>
    lateinit var selectedSizeList : ArrayList<String>
    lateinit var selectedColorList : ArrayList<String>
    lateinit var selectedCategoryList : ArrayList<String>

    var selectedTheme : String? = null
    var selectedOrder : String? = null
    lateinit var keyword : String
    var onceFlag = true
    var isClickedOrder = false

    lateinit var searchCakeDesignIds : ArrayList<Long>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = getViewDataBinding()
        binding.viewModel = getViewModel()

        showLoadingBar()
        choiceTagItems = ArrayList()
        var extra = this.arguments
        extra = getArguments();
        keyword = extra!!.getString("keyword")!!

        view_background_search_design.setOnClickListener(this)
        rl_filter_refresh_search_design.setOnClickListener(this)
        rl_filter_default_search_design.setOnClickListener(this)
        rl_filter_pickup_region_search_design.setOnClickListener(this)
        rl_filter_size_search_design.setOnClickListener(this)
        rl_filter_color_search_design.setOnClickListener(this)
        rl_filter_category_search_design.setOnClickListener(this)

        initRecyclerview()

        searchDesignViewModel.cakeDesignItems.observe(viewLifecycleOwner, Observer { datas ->
            hideLoadingBar()
            searchCakeDesignIds = ArrayList<Long>()
            searchListSize = datas.size
            if(searchListSize > 0) {
                for(data in datas) {
                    searchCakeDesignIds.add(data.designIndex!!)
                }
                rv_design_list_search_design.visibility = View.VISIBLE
                tv_empty_search_design.visibility = View.GONE
            }
            else {
                rv_design_list_search_design.visibility = View.GONE
                tv_empty_search_design.visibility = View.VISIBLE

//                 필터 부분 visible
                if(onceFlag) {
                    sv_filter_btn_search_design.visibility = View.GONE
                }
                else sv_filter_btn_search_design.visibility = View.VISIBLE

                Log.d("songjem", "get search designs size == 0")
            }
            designListAdapter.setDesignListItems(datas)
        })
        searchDesignFragment = this
        getSearchDesign()
    }


    fun getSearchDesign() {

        selectedTheme = null
        selectedLocList = ArrayList<String>()
        selectedSizeList = ArrayList<String>()
        selectedColorList = ArrayList<String>()
        selectedCategoryList = ArrayList<String>()
        searchDesignViewModel.sendParamsForSearchDesign(keyword, name, selectedTheme, selectedLocList, selectedSizeList, selectedColorList, selectedCategoryList, null, null)
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

        searchDesignColorAdapter = SearchDesignColorAdapter(context!!)
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

    fun getSearchDesignByNetwork(keyword : String, name : String?, choiceTagItems : ArrayList<ChoiceTag>) {
        showLoadingBar()
        onceFlag = false
        // 데이터 초기화
        selectedLocList = ArrayList<String>()
        selectedSizeList = ArrayList<String>()
        selectedColorList = ArrayList<String>()
        selectedCategoryList = ArrayList<String>()

        if(choiceTagItems.size > 0) rv_choice_tag_search_design.visibility = View.VISIBLE
        else rv_choice_tag_search_design.visibility = View.GONE

        // 데이터 가져오기
        for(i in 0.. choiceTagItems.size - 1) {
            // 기본 정렬
            if(choiceTagItems[i].filterCode == 0) {
            }
            // 지역
            else if(choiceTagItems[i].filterCode == 1) {
                // 전체
                if(choiceTagItems[i].choiceCode == 0) {
                    for(i in 1.. choiceTagItems.size - 1) {
                        selectedLocList.remove(choiceTagItems[i].choiceName)
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
        if((isClickedOrder == false) && ((selectedLocList.size + selectedSizeList.size + selectedColorList.size + selectedCategoryList.size) == 0)) rv_choice_tag_search_design.visibility = View.GONE

        searchDesignViewModel.sendParamsForSearchDesign(keyword, name, selectedTheme, selectedLocList, selectedSizeList, selectedColorList, selectedCategoryList, selectedOrder, null)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.view_background_search_design -> {
                Log.d("songjem", "background is touched")
                view_background_search_design.visibility = View.INVISIBLE
                if(clickedPosition == 0) {
                    isClickedOrder = true
                    rl_filter_default_search_design.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_btn_effect))
                    btn_filter_default_compact_search_design.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_compact))

                    listSelected[0] = true
                    defaultFilterOff()
                    selectedOrder = searchDesignDefaultAdapter.getClickedItem()
                    tv_filter_default_title_search_design.text = selectedOrder

                    // 기존 정렬 값(초이스) 지우기
                    deleteChoiceTag(0)
                    choiceTagItems.add(ChoiceTag(0, 0, selectedOrder!!))
                    searchDesignChoiceTagAdapter.setChoiceTagItem(choiceTagItems)

                    if(selectedOrder.equals("기본순")) selectedOrder = null
                    else if(selectedOrder.equals("찜순")) selectedOrder = "zzim"
                    else if(selectedOrder.equals("가격 낮은 순")) selectedOrder = "cheap"

                    getSearchDesignByNetwork(keyword, null, choiceTagItems)
                }
                else if(clickedPosition == 1) {
                    rl_filter_pickup_region_search_design.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_btn_effect))
                    btn_filter_pickup_region_compact_search_design.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_compact))

                    listSelected[1] = true
                    regionFilterOff()

                    // 이전에 선택했던 장소 필터 값 지우기
                    deleteChoiceTag(1)

                    // 추가한 리스트 가져와서 리스트에 넣어야 함
                    var tagList = searchDesignRegionAdapter.getChoiceTagIndex()

                    // 전체 선택
                    if(tagList[0] == 0) {
                        clearRegion()
                        listSelected[1] = false
                        for(i in 1.. regionList.size-1) {
                            choiceTagItems.remove(ChoiceTag(1, i, regionList[i]))
                        }
                    }
                    else {
                        for(i in 0.. tagList.size-1) {
                            choiceTagItems.add(ChoiceTag(1, tagList[i], regionList[tagList[i]]))
                        }
                    }

                    searchDesignChoiceTagAdapter.setChoiceTagItem(choiceTagItems)
                    getSearchDesignByNetwork(keyword, name, choiceTagItems)
                }
                else if(clickedPosition == 2) {
                    rl_filter_size_search_design.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_btn_effect))
                    btn_filter_size_compact_search_design.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_compact))

                    listSelected[2] = true
                    sizeFilterOff()

                    // 이전에 선택했던 크기 필터 값 지우기
                    deleteChoiceTag(2)
                    // 추가한 리스트 가져와서 리스트에 넣어야 함
                    var tagList = searchDesignSizeAdapter.getChoiceTagIndex()

                    // 전체 선택
                    if(tagList[0] == 0) {
                        clearSize()
                        listSelected[2] = false
                        for(i in 1.. designSizeItems.size-1) {
                            choiceTagItems.remove(ChoiceTag(1, i, designSizeItems[i].sizeName))
//                            choiceTagItems.add(ChoiceTag(1, i, designSizeItems[i].sizeName))
                        }
                    }
                    else {
                        for(i in 0.. tagList.size-1) {
                            choiceTagItems.add(ChoiceTag(2, tagList[i], designSizeItems[tagList[i]].sizeName))
                        }
                    }

                    searchDesignChoiceTagAdapter.setChoiceTagItem(choiceTagItems)

                    getSearchDesignByNetwork(keyword, name, choiceTagItems)
                }
                else if(clickedPosition == 3) {
                    rl_filter_color_search_design.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_btn_effect))
                    btn_filter_color_compact_search_design.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_compact))

                    listSelected[3] = true
                    colorFilterOff()

                    // 이전에 선택했던 색깔 필터 값 지우기
                    deleteChoiceTag(3)
                    // 추가한 리스트 가져와서 리스트에 넣어야 함
                    var tagList = searchDesignColorAdapter.getChoiceTagIndex()

                    // 전체 선택
                    if(tagList[0] == 0) {
                        clearColor()
                        listSelected[3] = false
                        for(i in 1.. colorList.size-1) {
                            choiceTagItems.remove(ChoiceTag(1, i, colorList[i]))
//                            choiceTagItems.add(ChoiceTag(1, i, colorList[i]))
                        }
                    }
                    else {
                        for(i in 0.. tagList.size-1) {
                            choiceTagItems.add(ChoiceTag(3, tagList[i], colorList[tagList[i]]))
                        }
                    }

                    searchDesignChoiceTagAdapter.setChoiceTagItem(choiceTagItems)
                    getSearchDesignByNetwork(keyword, name, choiceTagItems)
                }
                else if(clickedPosition == 4) {
                    rl_filter_category_search_design.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_btn_effect))
                    btn_filter_category_compact_search_design.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_compact))

                    listSelected[4] = true
                    categoryFilterOff()

                    // 이전에 선택했던 카테고리 필터 값 지우기
                    deleteChoiceTag(4)
                    // 추가한 리스트 가져와서 리스트에 넣어야 함
                    var tagList = searchDesignCategoryAdapter.getChoiceTagIndex()

                    // 전체 선택
                    if(tagList[0] == 0) {
                        clearCategory()
                        listSelected[4] = false
                        for(i in 1.. categoryList.size-1) {
                            choiceTagItems.remove(ChoiceTag(1, i, categoryList[i]))
//                            choiceTagItems.add(ChoiceTag(1, i, categoryList[i]))
                        }
                    }
                    else {
                        for(i in 0.. tagList.size-1) {
                            choiceTagItems.add(ChoiceTag(4, tagList[i], categoryList[tagList[i]]))
                        }
                    }

                    searchDesignChoiceTagAdapter.setChoiceTagItem(choiceTagItems)
                    getSearchDesignByNetwork(keyword, name, choiceTagItems)
                }
            }
            R.id.rl_filter_refresh_search_design -> {
                view_background_search_design.visibility = View.INVISIBLE

                for(i in 0 .. 4) {
                    listSelected[i] = false
                }
                rl_filter_content_search_design.visibility = View.GONE

                clearDefault()
                clearRegion()
                clearSize()
                clearColor()
                clearCategory()

                choiceTagItems = ArrayList()
                searchDesignChoiceTagAdapter.setChoiceTagItem(choiceTagItems)
                rv_choice_tag_search_design.visibility = View.GONE
                getSearchDesign()
            }
            R.id.rl_filter_default_search_design -> {
                if(!rl_filter_default_search_design.isSelected) {
                    if(searchListSize == 0) {
                        // empty 화면 VISIBLITY OFF
                        rv_design_list_search_design.visibility = View.VISIBLE
                        tv_empty_search_design.visibility = View.GONE
                    }

                    setFilterItem(0)
                    regionFilterOff()
                    sizeFilterOff()
                    colorFilterOff()
                    categoryFilterOff()
                    defaultFilterOn()
                    view_background_search_design.visibility = View.VISIBLE
                }
                else {
                    if(searchListSize == 0) {
                        // empty 화면 VISIBILITY ON
                        rv_design_list_search_design.visibility = View.GONE
                        tv_empty_search_design.visibility = View.VISIBLE
                    }

                    defaultFilterOff()
                    view_background_search_design.visibility = View.INVISIBLE
                }
            }
            R.id.rl_filter_pickup_region_search_design -> {
                if(!rl_filter_pickup_region_search_design.isSelected) {
                    if(searchListSize == 0) {
                        // empty 화면 VISIBLITY OFF
                        rv_design_list_search_design.visibility = View.VISIBLE
                        tv_empty_search_design.visibility = View.GONE
                    }

                    setFilterItem(1)
                    defaultFilterOff()
                    sizeFilterOff()
                    colorFilterOff()
                    categoryFilterOff()
                    regionFilterOn()
                    view_background_search_design.visibility = View.VISIBLE
                }
                else {
                    if(searchListSize == 0) {
                        // empty 화면 VISIBILITY ON
                        rv_design_list_search_design.visibility = View.GONE
                        tv_empty_search_design.visibility = View.VISIBLE
                    }
                    regionFilterOff()
                    view_background_search_design.visibility = View.INVISIBLE
                }
            }
            R.id.rl_filter_size_search_design -> {
                if(!rl_filter_size_search_design.isSelected) {
                    if(searchListSize == 0) {
                        // empty 화면 VISIBLITY OFF
                        rv_design_list_search_design.visibility = View.VISIBLE
                        tv_empty_search_design.visibility = View.GONE
                    }

                    setFilterItem(2)
                    defaultFilterOff()
                    regionFilterOff()
                    colorFilterOff()
                    categoryFilterOff()
                    sizeFilterOn()
                    view_background_search_design.visibility = View.VISIBLE
                }
                else {
                    if(searchListSize == 0) {
                        // empty 화면 VISIBILITY ON
                        rv_design_list_search_design.visibility = View.GONE
                        tv_empty_search_design.visibility = View.VISIBLE
                    }

                    sizeFilterOff()
                    view_background_search_design.visibility = View.INVISIBLE
                }
            }
            R.id.rl_filter_color_search_design -> {
                if(!rl_filter_color_search_design.isSelected) {
                    if(searchListSize == 0) {
                        // empty 화면 VISIBLITY OFF
                        rv_design_list_search_design.visibility = View.VISIBLE
                        tv_empty_search_design.visibility = View.GONE
                    }

                    setFilterItem(3)
                    defaultFilterOff()
                    regionFilterOff()
                    sizeFilterOff()
                    categoryFilterOff()
                    colorFilterOn()
                    view_background_search_design.visibility = View.VISIBLE
                }
                else {
                    if(searchListSize == 0) {
                        // empty 화면 VISIBILITY ON
                        rv_design_list_search_design.visibility = View.GONE
                        tv_empty_search_design.visibility = View.VISIBLE
                    }

                    colorFilterOff()
                    view_background_search_design.visibility = View.INVISIBLE
                }
            }
            R.id.rl_filter_category_search_design -> {
                if(!rl_filter_category_search_design.isSelected) {
                    if(searchListSize == 0) {
                        // empty 화면 VISIBLITY OFF
                        rv_design_list_search_design.visibility = View.VISIBLE
                        tv_empty_search_design.visibility = View.GONE
                    }

                    setFilterItem(4)
                    defaultFilterOff()
                    regionFilterOff()
                    sizeFilterOff()
                    colorFilterOff()
                    categoryFilterOn()
                    view_background_search_design.visibility = View.VISIBLE
                }
                else {
                    if(searchListSize == 0) {
                        // empty 화면 VISIBILITY ON
                        rv_design_list_search_design.visibility = View.GONE
                        tv_empty_search_design.visibility = View.VISIBLE
                    }

                    categoryFilterOff()
                    view_background_search_design.visibility = View.INVISIBLE
                }
            }
            else -> {
                var position: Int = rv_design_list_search_design.getChildAdapterPosition(view!!)
                val intent = Intent(context, DesignDetailActivity::class.java)
                intent.putExtra("designId", searchCakeDesignIds[position].toInt())
                startActivity(intent)
            }
        }
    }
    // 기본순 초기화
    fun clearDefault() {
        isClickedOrder = false

        rl_filter_default_search_design.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_btn))
        btn_filter_default_compact_search_design.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_compact_before))
        rl_filter_default_search_design.isSelected = false
        btn_filter_default_compact_search_design.isSelected = false
        tv_filter_default_title_search_design.setTextColor(Color.parseColor("#000000"))
        tv_filter_default_title_search_design.text = "기본순"
        searchDesignDefaultAdapter.checkedPosition.clear()
        searchDesignDefaultAdapter.checkedPosition.add(0)

        selectedOrder = null
    }
    // 장소 선택 초기화
    fun clearRegion() {
        rl_filter_pickup_region_search_design.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_btn_effect_before))
        btn_filter_pickup_region_compact_search_design.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_compact_before))
        rl_filter_pickup_region_search_design.isSelected = false
        btn_filter_pickup_region_compact_search_design.isSelected = false
        tv_filter_pickup_region_title_search_design.setTextColor(Color.parseColor("#000000"))
        tv_filter_pickup_region_title_search_design.text = "지역"
        searchDesignRegionAdapter.checkedPosition.clear()
        searchDesignRegionAdapter.checkedPosition.add(0)
    }
    // 크기 선택 초기화
    fun clearSize() {
        rl_filter_size_search_design.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_btn_effect_before))
        btn_filter_size_compact_search_design.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_compact_before))
        rl_filter_size_search_design.isSelected = false
        btn_filter_size_compact_search_design.isSelected = false
        tv_filter_size_title_search_design.setTextColor(Color.parseColor("#000000"))
        tv_filter_size_title_search_design.text = "크기"
        searchDesignSizeAdapter.checkedPosition.clear()
        searchDesignSizeAdapter.checkedPosition.add(0)
    }
    // 색깔 선택 초기화
    fun clearColor() {
        rl_filter_color_search_design.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_btn_effect_before))
        btn_filter_color_compact_search_design.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_compact_before))
        rl_filter_color_search_design.isSelected = false
        btn_filter_color_compact_search_design.isSelected = false
        tv_filter_color_title_search_design.setTextColor(Color.parseColor("#000000"))
        tv_filter_color_title_search_design.text = "색깔"
        searchDesignColorAdapter.checkedPosition.clear()
        searchDesignColorAdapter.checkedPosition.add(0)
    }
    // 카테고리 초기화
    fun clearCategory() {
        rl_filter_category_search_design.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_btn_effect_before))
        btn_filter_category_compact_search_design.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_compact_before))
        rl_filter_category_search_design.isSelected = false
        btn_filter_category_compact_search_design.isSelected = false
        tv_filter_category_title_search_design.setTextColor(Color.parseColor("#000000"))
        tv_filter_category_title_search_design.text = "카테고리"
        searchDesignCategoryAdapter.checkedPosition.clear()
        searchDesignCategoryAdapter.checkedPosition.add(0)
    }

    fun showLoadingBar() {
        val c = resources.getColor(R.color.colorPrimary)
        pb_loading_search_design.setIndeterminate(true)
        pb_loading_search_design.getIndeterminateDrawable().setColorFilter(c, PorterDuff.Mode.MULTIPLY)
        pb_loading_search_design.visibility = View.VISIBLE
    }

    fun hideLoadingBar() {
        pb_loading_search_design.visibility = View.GONE
    }

    // 기본순 필터링 ON
    fun defaultFilterOn() {
        setFilterItem(0)
        tv_filter_default_title_search_design.setTextColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
        rl_filter_content_search_design.visibility = View.VISIBLE
        rv_filter_default_list_search_design.visibility = View.VISIBLE
        rl_filter_default_search_design.isSelected = true
        btn_filter_default_compact_search_design.isSelected = true

        clickedPosition = 0
    }

    // 기본순 필터링 OFF
    fun defaultFilterOff() {
        rl_filter_content_search_design.visibility = View.GONE
        rv_filter_default_list_search_design.visibility = View.GONE
        rl_filter_default_search_design.isSelected = false
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

        rl_filter_content_search_design.visibility = View.VISIBLE
        rv_filter_region_list_search_design.visibility = View.VISIBLE
        rl_filter_pickup_region_search_design.isSelected = true
        btn_filter_pickup_region_compact_search_design.isSelected = true

        clickedPosition = 1
    }

    // 지역별 필터링 OFF
    fun regionFilterOff() {
        rl_filter_content_search_design.visibility = View.GONE
        rv_filter_region_list_search_design.visibility = View.GONE
        rl_filter_pickup_region_search_design.isSelected = false
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

        rl_filter_content_search_design.visibility = View.VISIBLE
        rv_filter_size_list_search_design.visibility = View.VISIBLE
        rl_filter_size_search_design.isSelected = true
        btn_filter_size_compact_search_design.isSelected = true

        clickedPosition = 2
    }

    // 크기별 필터링 OFF
    fun sizeFilterOff() {
        rl_filter_content_search_design.visibility = View.GONE
        rv_filter_size_list_search_design.visibility = View.GONE
        rl_filter_size_search_design.isSelected = false
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

        rl_filter_content_search_design.visibility = View.VISIBLE
        rv_filter_color_list_search_design.visibility = View.VISIBLE
        rl_filter_color_search_design.isSelected = true
        btn_filter_color_compact_search_design.isSelected = true

        clickedPosition = 3
    }

    // 색깔별 필터링 OFF
    fun colorFilterOff() {
        rl_filter_content_search_design.visibility = View.GONE
        rv_filter_color_list_search_design.visibility = View.GONE
        rl_filter_color_search_design.isSelected = false
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

        rl_filter_content_search_design.visibility = View.VISIBLE
        rv_filter_category_list_search_design.visibility = View.VISIBLE
        rl_filter_category_search_design.isSelected = true
        btn_filter_category_compact_search_design.isSelected = true

        clickedPosition = 4
    }

    // 카테고리별 필터링 OFF
    fun categoryFilterOff() {
        rl_filter_content_search_design.visibility = View.GONE
        rv_filter_category_list_search_design.visibility = View.GONE
        rl_filter_category_search_design.isSelected = false
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
                designSizeItems.add(CakeDesignSize(1, "미니", "10-11cm, 1-2인용"))
                designSizeItems.add(CakeDesignSize(2, "1호", "15-16cm, 3-4인용"))
                designSizeItems.add(CakeDesignSize(3, "2호", "18cm, 5-6인용"))
                designSizeItems.add(CakeDesignSize(4, "3호", "21cm, 7-8인용"))
                designSizeItems.add(CakeDesignSize(5, "2단", "파티용 특별제작"))
                searchDesignSizeAdapter.setDesignSizeItems(designSizeItems)
            }
            // 색깔 필터
            3 -> {
                colorValItems = ArrayList<Int>()
                colorItems = ArrayList<String>()
                for (i in 0..colorList.size - 1) {
                    colorValItems.add(colorValList[i])
                    colorItems.add(colorList[i])
                }
                searchDesignColorAdapter.setDesignColorItems(colorValItems, colorItems)
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