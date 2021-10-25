package com.cakeit.cakitandroid.presentation.search.searchlist.shop

import android.content.Intent
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
import com.cakeit.cakitandroid.base.BaseFragment
import com.cakeit.cakitandroid.data.source.local.entity.ChoiceTag
import com.cakeit.cakitandroid.databinding.FragmentSearchShopBinding
import com.cakeit.cakitandroid.presentation.list.MinDecorator
import com.cakeit.cakitandroid.presentation.list.TodayDecorator
import com.cakeit.cakitandroid.presentation.list.shoplist.ShopListAdapter
import com.cakeit.cakitandroid.presentation.search.searchlist.shop.filter.SearchShopChoiceTagAdapter
import com.cakeit.cakitandroid.presentation.search.searchlist.shop.filter.SearchShopDefaultAdapter
import com.cakeit.cakitandroid.presentation.search.searchlist.shop.filter.SearchShopRegionAdapter
import com.cakeit.cakitandroid.presentation.shop.ShopDetailActivity
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import kotlinx.android.synthetic.main.fragment_search_shop.*
import kotlinx.android.synthetic.main.fragment_shop_list.*

class SearchShopFragment : BaseFragment<FragmentSearchShopBinding, SearchShopViewModel>(), View.OnClickListener {

    lateinit var binding : FragmentSearchShopBinding
    lateinit var searchShopViewModel : SearchShopViewModel

    lateinit var shopListAdapter: ShopListAdapter
    lateinit var searchShopChoiceTagAdapter : SearchShopChoiceTagAdapter
    lateinit var searchShopDefaultAdapter: SearchShopDefaultAdapter
    lateinit var searchShopRegionAdapter: SearchShopRegionAdapter

    private lateinit var regionItems: ArrayList<String>
    lateinit var choiceTagItems: ArrayList<ChoiceTag>
    private lateinit var filterItems: ArrayList<String>

    private var clickedPosition = -1;

    private val filterList = listOf<String>("기본순", "찜순", "가격 낮은 순")
    private val regionList = listOf<String>("전체", "강남구", "관악구", "광진구", "마포구", "서대문구"
        , "송파구")
    private var selectedDate : String = ""
    var listSelected = mutableListOf<Boolean>(false, false, false)

    lateinit var selecedLocList : ArrayList<String>
    lateinit var seleceSizeList : ArrayList<String>
    lateinit var selecedColorList : ArrayList<String>
    lateinit var selecedCategoryList : ArrayList<String>
    var selectedTheme : String? = null
    var selectedOrder : String? = null
    var name : String? = null
    lateinit var keyword : String
    lateinit var searchCakeShopIds : ArrayList<Int>
    var choicePickupDate : String? = null
    var onceFlag = true
    var searchListSize = 0
    var isClickedOrder = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = getViewDataBinding()
        binding.viewModel = getViewModel()

        choiceTagItems = ArrayList()
        var extra = this.arguments
        extra = getArguments();
        keyword = extra!!.getString("keyword")!!

        selectedTheme = "NONE"
        selecedLocList = ArrayList<String>()
        seleceSizeList = ArrayList<String>()
        selecedColorList = ArrayList<String>()
        selecedCategoryList = ArrayList<String>()

        view_background_search_shop.setOnClickListener(this)
        rl_filter_refresh_search_shop.setOnClickListener(this)
        rl_filter_default_search_shop.setOnClickListener(this)
        rl_filter_pickup_region_search_shop.setOnClickListener(this)
        rl_filter_pickup_date_search_shop.setOnClickListener(this)

        initRecyclerview()

        shopListAdapter.setOnItemClickListener(object : ShopListAdapter.OnShopItemClickListener{

            override fun onShopItemClick(position: Int) {
                val intent = Intent(context, ShopDetailActivity::class.java)
                Log.d("songjem", "position = " + position + ", cakeShopID = " + searchCakeShopIds[position])
                intent.putExtra("cakeShopId", searchCakeShopIds[position])
                startActivity(intent)
            }
        })

        searchShopViewModel.cakeShopItems.observe(viewLifecycleOwner, Observer { datas ->
            searchCakeShopIds = ArrayList<Int>()
            searchListSize = datas.size
            if(searchListSize > 0) {
                for(data in datas) {
                    searchCakeShopIds.add(data.shopId!!)
                }
                rv_shop_list_search_shop.visibility = View.VISIBLE
                tv_empty_search_shop.visibility = View.GONE
            }
            else {
                rv_shop_list_search_shop.visibility = View.GONE
                sv_filter_btn_search_shop.visibility = View.GONE
                tv_empty_search_shop.visibility = View.VISIBLE

                // 필터 부분 visible
                if(onceFlag) {
                    sv_filter_btn_search_shop.visibility = View.GONE
                }
                else sv_filter_btn_search_shop.visibility = View.VISIBLE
                Log.d("songjem", "get search shops size == 0")
            }
            shopListAdapter.setShopListItems(datas)
        })
        getshopList()
        searchShopFragment = this

        cv_pickup_calendar_search_shop.addDecorators(
            MinDecorator(
                context!!
            ),
            TodayDecorator(
                context!!
            )
        )

        // 달력 날짜 선택
        cv_pickup_calendar_search_shop.setOnDateChangedListener(OnDateSelectedListener { widget, date, selected ->
            selectedDate = date.month.toString() + "월 " + date.day.toString() + "일"
            var pickupMonth = ""
            var pickupDay = ""
            if(date.month < 10) pickupMonth = "0" + date.month
            else pickupMonth = date.month.toString()

            if(date.day < 10) pickupDay = "0" + date.day
            else pickupDay = date.day.toString()

            choicePickupDate = date.year.toString() + pickupMonth + pickupDay

            rl_filter_pickup_date_search_shop.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_btn_effect))
            btn_filter_pickup_date_compact_search_shop.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_compact))
            rl_filter_pickup_date_search_shop.isSelected = false

            listSelected[2] = true
            dateFilterOff()

            // 기존 선택한 날짜 값(초이스) 지우기
            deleteChoiceTag(2)

            // 선택한 날짜 태그 리스트에 추가
            choiceTagItems.add(ChoiceTag(2, 0, selectedDate!!))

            // 리스트 화면 re visible
            view_background_search_shop.visibility = View.INVISIBLE

            searchShopChoiceTagAdapter.setChoiceTagItem(choiceTagItems)
            getShopListByNetwork(choiceTagItems)
        })
    }

    fun getshopList() {

        selectedTheme = "NONE"
        selecedLocList = ArrayList<String>()
        seleceSizeList = ArrayList<String>()
        selecedColorList = ArrayList<String>()
        selecedCategoryList = ArrayList<String>()

        searchShopViewModel.sendParamsForSearchShop(keyword, name, selectedTheme, selecedLocList, seleceSizeList, selecedColorList, selecedCategoryList, selectedOrder, choicePickupDate)
    }


    fun initRecyclerview() {

        searchShopChoiceTagAdapter = SearchShopChoiceTagAdapter().apply {
        }

        shopListAdapter = ShopListAdapter(context!!)
        searchShopDefaultAdapter = SearchShopDefaultAdapter()
            .apply {
                listener = object : SearchShopDefaultAdapter.OnShopFilterItemClickListener {
                    override fun onShopFilterItemClick(position: Int) {
                        Toast.makeText(context!!, "filter item" + position + " is clicked", Toast.LENGTH_LONG).show()
                    }
                }
            }

        searchShopRegionAdapter = SearchShopRegionAdapter()
            .apply {
                listener = object : SearchShopRegionAdapter.OnShopFilterItemClickListener {
                    override fun onShopFilterItemClick(position: Int) {
                        Toast.makeText(context!!, "region item" + position + " is clicked", Toast.LENGTH_LONG).show()
                    }
                }
            }

        rv_choice_tag_search_shop.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.HORIZONTAL, false)
            adapter = searchShopChoiceTagAdapter
        }

        rv_shop_list_search_shop.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!!)
            adapter = shopListAdapter
        }

        rv_filter_default_list_search_shop.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!!)
            adapter = searchShopDefaultAdapter
        }

        rv_filter_region_list_search_shop.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!!)
            adapter = searchShopRegionAdapter
        }
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

    fun getShopListByNetwork(choiceTagItems : ArrayList<ChoiceTag>) {
        onceFlag = false
        // TAG 리스트 초기화
        selecedLocList = ArrayList<String>()

        // TAG 리스트 가져오기
        for(i in 0.. choiceTagItems.size - 1) {
            // 기본 정렬
            if(choiceTagItems[i].filterCode == 0) {
                rv_choice_tag_search_shop.visibility = View.VISIBLE
            }
            // 지역
            if(choiceTagItems[i].filterCode == 1) {
                // 전체
                if(choiceTagItems[i].choiceCode == 0) {
                    for(i in 1.. choiceTagItems.size - 1) {
                        selecedLocList.remove(choiceTagItems[i].choiceName)
                    }
                }
                else selecedLocList.add(choiceTagItems[i].choiceName)
            }
            // 날짜
            else if(choiceTagItems[i].filterCode == 2) {
                rv_choice_tag_search_shop.visibility = View.VISIBLE
            }
        }

        if(isClickedOrder == true || selecedLocList.size > 0 || choicePickupDate != null) {
            rv_choice_tag_search_shop.visibility = View.VISIBLE
        } else {
            rv_choice_tag_search_shop.visibility = View.GONE
        }

        searchShopViewModel.sendParamsForSearchShop(keyword, name, selectedTheme, selecedLocList, seleceSizeList, selecedColorList, selecedCategoryList, selectedOrder, choicePickupDate)
    }
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.view_background_search_shop -> {
                Log.d("songjem", "background is touched")
                view_background_search_shop.visibility = View.INVISIBLE
                if(clickedPosition == 0) {
                    isClickedOrder = true

                    rl_filter_default_search_shop.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_btn_effect))
                    btn_filter_default_compact_search_shop.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_compact))

                    listSelected[0] = true
                    defaultFilterOff()

                    selectedOrder = searchShopDefaultAdapter.getClickedItem()
                    tv_filter_default_title_search_shop.text = selectedOrder

                    // 기존 정렬 값(초이스) 지우기
                    deleteChoiceTag(0)
                    choiceTagItems.add(ChoiceTag(0, 0, selectedOrder!!))
                    searchShopChoiceTagAdapter.setChoiceTagItem(choiceTagItems)

                    if(selectedOrder.equals("기본순")) selectedOrder = null
                    else if(selectedOrder.equals("찜순")) selectedOrder = "zzim"
                    else if(selectedOrder.equals("가격 낮은 순")) selectedOrder = "cheap"

                    getShopListByNetwork(choiceTagItems)
                }
                else if(clickedPosition == 1) {
                    rl_filter_pickup_region_search_shop.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_btn_effect))
                    btn_filter_pickup_region_compact_search_shop.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_compact))

                    listSelected[1] = true
                    regionFilterOff()

                    // 이전에 선택했던 장소 필터 값 지우기
                    deleteChoiceTag(1)

                    // 추가한 리스트 가져와서 리스트에 넣어야 함
                    var tagList = searchShopRegionAdapter.getChoiceTagIndex()

                    if(tagList.size > 0) rv_choice_tag_search_shop.visibility = View.VISIBLE
                    else rv_choice_tag_search_shop.visibility = View.GONE

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

                    searchShopChoiceTagAdapter.setChoiceTagItem(choiceTagItems)
                    getShopListByNetwork(choiceTagItems)
                }
                else if(clickedPosition == 2) {
                    dateFilterOff()
                }
            }
            R.id.rl_filter_refresh_search_shop -> {
                view_background_search_shop.visibility = View.INVISIBLE

                choicePickupDate = null

                for(i in 0 .. 2) {
                    listSelected[i] = false
                }
                rl_filter_content_search_shop.visibility = View.GONE
                rv_filter_default_list_search_shop.visibility = View.GONE

                clearDefault()
                clearRegion()
                clearDate()

                choiceTagItems = ArrayList()
                searchShopChoiceTagAdapter.setChoiceTagItem(choiceTagItems)
                getShopListByNetwork(choiceTagItems)
            }
            R.id.rl_filter_default_search_shop -> {
                if(!rl_filter_default_search_shop.isSelected) {
                    if(searchListSize == 0) {
                        // empty 화면 VISIBLITY OFF
                        rv_shop_list_search_shop.visibility = View.VISIBLE
                        tv_empty_search_shop.visibility = View.GONE
                    }

                    setFilterItem(0)
                    regionFilterOff()
                    dateFilterOff()
                    defaultFilterOn()
                    view_background_search_shop.visibility = View.VISIBLE
                }
                else {
                    if(searchListSize == 0) {
                        // empty 화면 VISIBILITY ON
                        rv_shop_list_search_shop.visibility = View.GONE
                        tv_empty_search_shop.visibility = View.VISIBLE
                    }

                    defaultFilterOff()
                    view_background_search_shop.visibility = View.INVISIBLE
                }
            }
            R.id.rl_filter_pickup_region_search_shop -> {
                if(searchListSize == 0) {
                    // empty 화면 VISIBLITY OFF
                    rv_shop_list_search_shop.visibility = View.VISIBLE
                    tv_empty_search_shop.visibility = View.GONE
                }

                if(!rl_filter_pickup_region_search_shop.isSelected) {
                    setFilterItem(1)
                    defaultFilterOff()
                    dateFilterOff()
                    regionFilterOn()
                    view_background_search_shop.visibility = View.VISIBLE
                }
                else {
                    if(searchListSize == 0) {
                        // empty 화면 VISIBILITY ON
                        rv_shop_list_search_shop.visibility = View.GONE
                        tv_empty_search_shop.visibility = View.VISIBLE
                    }

                    regionFilterOff()
                    view_background_search_shop.visibility = View.INVISIBLE
                }
            }
            R.id.rl_filter_pickup_date_search_shop -> {
                if(!rl_filter_pickup_date_search_shop.isSelected) {
                    if(searchListSize == 0) {
                        // empty 화면 VISIBLITY OFF
                        rv_shop_list_search_shop.visibility = View.VISIBLE
                        tv_empty_search_shop.visibility = View.GONE
                    }

                    defaultFilterOff()
                    regionFilterOff()
                    dateFilterOn()
                    view_background_search_shop.visibility = View.VISIBLE
                }
                else {
                    if(searchListSize == 0) {
                        // empty 화면 VISIBILITY ON
                        rv_shop_list_search_shop.visibility = View.GONE
                        tv_empty_search_shop.visibility = View.VISIBLE
                    }

                    dateFilterOff()
                    view_background_search_shop.visibility = View.INVISIBLE
                }
            }
        }
    }
    // 기본순 초기화
    fun clearDefault() {
        isClickedOrder = false

        rl_filter_default_search_shop.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_btn_effect_before))
        btn_filter_default_compact_search_shop.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_compact_before))
        rl_filter_default_search_shop.isSelected = false
        btn_filter_default_compact_search_shop.isSelected = false
        tv_filter_default_title_search_shop.setTextColor(Color.parseColor("#000000"))
        tv_filter_default_title_search_shop.text = "기본순"
        searchShopDefaultAdapter.checkedPosition.clear()
        searchShopDefaultAdapter.checkedPosition.add(0)

        selectedOrder = null
    }
    // 장소 선택 초기화
    fun clearRegion() {
        rl_filter_pickup_region_search_shop.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_btn_effect_before))
        btn_filter_pickup_region_compact_search_shop.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_compact_before))
        rl_filter_pickup_region_search_shop.isSelected = false
        btn_filter_pickup_region_compact_search_shop.isSelected = false
        tv_filter_pickup_region_title_search_shop.setTextColor(Color.parseColor("#000000"))
        tv_filter_pickup_region_title_search_shop.text = "픽업 지역"
        searchShopRegionAdapter.checkedPosition.clear()
    }
    // 날짜 선택 초기화
    fun clearDate() {
        rl_filter_pickup_date_search_shop.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_btn_effect_before))
        btn_filter_pickup_date_compact_search_shop.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_compact_before))
        rl_filter_pickup_date_search_shop.isSelected = false
        btn_filter_pickup_date_compact_search_shop.isSelected = false
        tv_filter_pickup_date_title_search_shop.setTextColor(Color.parseColor("#000000"))
        tv_filter_pickup_date_title_search_shop.text = "픽업 날짜"

        selectedDate = ""
        choicePickupDate = null
    }

    // 기본순 필터링 ON
    fun defaultFilterOn() {
        setFilterItem(0)
        tv_filter_default_title_search_shop.setTextColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
        rl_filter_content_search_shop.visibility = View.VISIBLE
        rv_filter_default_list_search_shop.visibility = View.VISIBLE
        rl_filter_default_search_shop.isSelected = true
        btn_filter_default_compact_search_shop.isSelected = true

        clickedPosition = 0
    }

    // 기본순 필터링 OFF
    fun defaultFilterOff() {
        rl_filter_content_search_shop.visibility = View.GONE
        rv_filter_default_list_search_shop.visibility = View.GONE
        rl_filter_default_search_shop.isSelected = false
        btn_filter_default_compact_search_shop.isSelected = false

        if(listSelected[0] == false){
            tv_filter_default_title_search_shop.setTextColor(Color.parseColor("#000000"))
        }
        else {
            tv_filter_default_title_search_shop.setTextColor(Color.parseColor("#ffffff"))
        }
    }

    // 지역별 필터링 ON
    fun regionFilterOn() {
        setFilterItem(1)
        tv_filter_pickup_region_title_search_shop.setTextColor(ContextCompat.getColor(context!!, R.color.colorPrimary))

        rl_filter_content_search_shop.visibility = View.VISIBLE
        rv_filter_region_list_search_shop.visibility = View.VISIBLE
        rl_filter_pickup_region_search_shop.isSelected = true
        btn_filter_pickup_region_compact_search_shop.isSelected = true

        clickedPosition = 1
    }

    // 지역별 필터링 OFF
    fun regionFilterOff() {
        rl_filter_content_search_shop.visibility = View.GONE
        rv_filter_region_list_search_shop.visibility = View.GONE
        rl_filter_pickup_region_search_shop.isSelected = false
        btn_filter_pickup_region_compact_search_shop.isSelected = false

        if(listSelected[1] == false){
            tv_filter_pickup_region_title_search_shop.setTextColor(Color.parseColor("#000000"))
        }
        else {
            tv_filter_pickup_region_title_search_shop.setTextColor(Color.parseColor("#ffffff"))
        }
    }

    // 날짜 필터링 ON
    fun dateFilterOn() {
        tv_filter_pickup_date_title_search_shop.setTextColor(ContextCompat.getColor(context!!, R.color.colorPrimary))

        rl_filter_content_search_shop.visibility = View.VISIBLE
        cv_pickup_calendar_search_shop.visibility = View.VISIBLE
        rl_filter_pickup_date_search_shop.isSelected = true
        btn_filter_pickup_date_compact_search_shop.isSelected = true

        clickedPosition = 2
    }

    // 지역별 필터링 OFF
    fun dateFilterOff() {
        rl_filter_content_search_shop.visibility = View.GONE
        cv_pickup_calendar_search_shop.visibility = View.GONE
        rl_filter_pickup_date_search_shop.isSelected = false
        btn_filter_pickup_date_compact_search_shop.isSelected = false

        if(listSelected[2] == false){
            tv_filter_pickup_date_title_search_shop.setTextColor(Color.parseColor("#000000"))
        }
        else {
            tv_filter_pickup_date_title_search_shop.setTextColor(Color.parseColor("#ffffff"))
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
                searchShopDefaultAdapter.setDefaultListItems(filterItems)
            }
            // 픽업 지역 필터
            1 -> {
                regionItems = ArrayList<String>()
                for (i in 0..regionList.size - 1) {
                    regionItems.add(regionList[i])
                }
                searchShopRegionAdapter.setRegionListItems(regionItems)
            }
        }
    }

    companion object {
        lateinit var searchShopFragment : SearchShopFragment
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_search_shop
    }

    override fun getViewModel(): SearchShopViewModel {
        searchShopViewModel = ViewModelProvider(this).get(SearchShopViewModel::class.java)
        return searchShopViewModel
    }
}