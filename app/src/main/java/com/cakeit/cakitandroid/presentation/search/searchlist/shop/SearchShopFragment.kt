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

    private val filterList = listOf<String>("기본순", "찜순", "가격 높은 순", "가격 낮은 순")
    private val regionList = listOf<String>("전체", "강남구", "관악구", "광진구", "마포구", "서대문구"
        , "송파구", "노원구", "성북구", "중구", "중랑구")
    private var selectedDate : String = ""
    var listSelected = mutableListOf<Boolean>(false, false, false)

    lateinit var selecedLocList : ArrayList<String>
    lateinit var seleceSizeList : ArrayList<String>
    lateinit var selecedColorList : ArrayList<String>
    lateinit var selecedCategoryList : ArrayList<String>
    var selectedTheme : String? = null
    var selectedOrder : String = ""
    lateinit var keyword : String
    lateinit var searchCakeShopIds : ArrayList<Int>

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
        selectedOrder = "DEFAULT"

        view_background_search_shop.setOnClickListener(this)
        btn_filter_default_search_shop.setOnClickListener(this)
        btn_filter_pickup_region_search_shop.setOnClickListener(this)
        btn_filter_pickup_date_search_shop.setOnClickListener(this)

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
            if(datas.size > 0) {
                for(data in datas) {
                    searchCakeShopIds.add(data.shopId!!)
                }
                rl_search_shop_not_empty.visibility = View.VISIBLE
                rl_search_shop_empty.visibility = View.GONE
            }
            else {
                rl_search_shop_not_empty.visibility = View.GONE
                rl_search_shop_empty.visibility = View.VISIBLE
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
        })
    }

    fun getshopList() {

//        selecedLocList = ArrayList<String>()
//        selectedOrder = "기본"
//        selectedDate = ""
//        selecedLocList.add("전체")
        selectedTheme = "NONE"
        selecedLocList = ArrayList<String>()
        seleceSizeList = ArrayList<String>()
        selecedColorList = ArrayList<String>()
        selecedCategoryList = ArrayList<String>()
        selectedOrder = "DEFAULT"

        searchShopViewModel.sendParamsForSearchShop(keyword, keyword, selectedTheme, selecedLocList, seleceSizeList, selecedColorList, selecedCategoryList, selectedOrder)
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
        // 데이터 초기화
        selecedLocList = ArrayList<String>()

        // 데이터 가져오기
        for(i in 0.. choiceTagItems.size - 1) {
            // 지역
            if(choiceTagItems[i].filterCode == 1) {
                // 전체
                if(choiceTagItems[i].choiceCode == 0) {
                    for(i in 1.. choiceTagItems.size - 1) {
                        selecedLocList.add(choiceTagItems[i].choiceName)
                    }
                }
                else selecedLocList.add(choiceTagItems[i].choiceName)
            }
        }
        Log.d("songjem", "locList = " + selecedLocList.toString())
        Log.d("songjem", "order = " + selectedOrder)
        searchShopViewModel.sendParamsForSearchShop(keyword, keyword, selectedTheme, selecedLocList, seleceSizeList, selecedColorList, selecedCategoryList, selectedOrder)

    }
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.view_background_search_shop -> {
                Log.d("songjem", "background is touched")
                view_background_search_shop.visibility = View.INVISIBLE
                rv_shop_list_search_shop.visibility = View.VISIBLE
                if(clickedPosition == 0) {
                    btn_filter_default_search_shop.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_btn_effect))
                    btn_filter_default_compact_search_shop.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_compact))

                    listSelected[0] = true
                    defaultFilterOff()
                    selectedOrder = searchShopDefaultAdapter.getClickedItem()
                    tv_filter_default_title_search_shop.text = selectedOrder

                    getShopListByNetwork(choiceTagItems)
                }
                else if(clickedPosition == 1) {
                    btn_filter_pickup_region_search_shop.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_btn_effect))
                    btn_filter_pickup_region_compact_search_shop.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_compact))

                    listSelected[1] = true
                    regionFilterOff()

                    // 기존 장소 값(초이스) 지우기
                    deleteChoiceTag(1)

                    // 추가한 리스트 가져와서 리스트에 넣어야 함
                    var tagList = searchShopRegionAdapter.getChoiceTagIndex()

                    if(tagList.size > 0) sv_choice_tag_search_shop.visibility = View.VISIBLE
                    else sv_choice_tag_search_shop.visibility = View.GONE

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

                    searchShopChoiceTagAdapter.setChoiceTagItem(choiceTagItems)
                    getShopListByNetwork(choiceTagItems)
                }
                else if(clickedPosition == 2) {
                    btn_filter_pickup_date_search_shop.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_btn_effect))
                    btn_filter_pickup_date_compact_search_shop.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_compact))
                    listSelected[2] = true

                    if(selectedDate.equals("")) tv_filter_pickup_date_title_search_shop.text = "픽업 날짜"
                    else {
                        tv_filter_pickup_date_title_search_shop.text = selectedDate
                    }
                    dateFilterOff()
                    btn_filter_pickup_date_search_shop.isSelected = false

                    getShopListByNetwork(choiceTagItems)
                }
            }
            R.id.btn_filter_default_search_shop -> {
                if(!btn_filter_default_search_shop.isSelected) {
                    setFilterItem(0)
                    regionFilterOff()
                    dateFilterOff()
                    defaultFilterOn()
                    view_background_search_shop.visibility = View.VISIBLE
                    rv_shop_list_search_shop.visibility = View.GONE
                }
                else {
                    defaultFilterOff()
                    view_background_search_shop.visibility = View.INVISIBLE
                    rv_shop_list_search_shop.visibility = View.VISIBLE
                }
            }
            R.id.btn_filter_pickup_region_search_shop -> {
                if(!btn_filter_pickup_region_search_shop.isSelected) {
                    setFilterItem(1)
                    defaultFilterOff()
                    dateFilterOff()
                    regionFilterOn()
                    view_background_search_shop.visibility = View.VISIBLE
                    rv_shop_list_search_shop.visibility = View.GONE
                }
                else {
                    regionFilterOff()
                    view_background_search_shop.visibility = View.INVISIBLE
                    rv_shop_list_search_shop.visibility = View.VISIBLE
                }
            }
            R.id.btn_filter_pickup_date_search_shop -> {
                if(!btn_filter_pickup_date_search_shop.isSelected) {
                    defaultFilterOff()
                    regionFilterOff()
                    dateFilterOn()
                    view_background_search_shop.visibility = View.VISIBLE
                    rv_shop_list_search_shop.visibility = View.GONE
                }
                else {
                    dateFilterOff()
                    view_background_search_shop.visibility = View.INVISIBLE
                    rv_shop_list_search_shop.visibility = View.VISIBLE
                }
            }
        }
    }
    // 기본순 초기화
    fun clearDefault() {
        btn_filter_default_search_shop.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_btn))
        btn_filter_default_compact_search_shop.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_compact_before))
        btn_filter_default_search_shop.isSelected = false
        btn_filter_default_compact_search_shop.isSelected = false
        tv_filter_default_title_search_shop.setTextColor(Color.parseColor("#000000"))
        tv_filter_default_title_search_shop.text = "기본순"
        searchShopDefaultAdapter.checkedPosition.clear()
    }
    // 장소 선택 초기화
    fun clearRegion() {
        btn_filter_pickup_region_search_shop.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_btn))
        btn_filter_pickup_region_compact_search_shop.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_compact_before))
        btn_filter_pickup_region_search_shop.isSelected = false
        btn_filter_pickup_region_compact_search_shop.isSelected = false
        tv_filter_pickup_region_title_search_shop.setTextColor(Color.parseColor("#000000"))
        tv_filter_pickup_region_title_search_shop.text = "픽업 지역"
        searchShopRegionAdapter.checkedPosition.clear()
    }
    // 날짜 선택 초기화
    fun clearDate() {
        btn_filter_pickup_date_search_shop.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_btn))
        btn_filter_pickup_date_compact_search_shop.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_compact_before))
        btn_filter_pickup_date_search_shop.isSelected = false
        btn_filter_pickup_date_compact_search_shop.isSelected = false
        tv_filter_pickup_date_title_search_shop.setTextColor(Color.parseColor("#000000"))
        tv_filter_pickup_date_title_search_shop.text = "픽업 날짜"
    }

    // 기본순 필터링 ON
    fun defaultFilterOn() {
        setFilterItem(0)
        tv_filter_default_title_search_shop.setTextColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
        cl_filter_content_search_shop.visibility = View.VISIBLE
        rv_filter_default_list_search_shop.visibility = View.VISIBLE
        btn_filter_default_search_shop.isSelected = true
        btn_filter_default_compact_search_shop.isSelected = true

        clickedPosition = 0
    }

    // 기본순 필터링 OFF
    fun defaultFilterOff() {
        cl_filter_content_search_shop.visibility = View.GONE
        rv_filter_default_list_search_shop.visibility = View.GONE
        btn_filter_default_search_shop.isSelected = false
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

        cl_filter_content_search_shop.visibility = View.VISIBLE
        rv_filter_region_list_search_shop.visibility = View.VISIBLE
        btn_filter_pickup_region_search_shop.isSelected = true
        btn_filter_pickup_region_compact_search_shop.isSelected = true

        clickedPosition = 1
    }

    // 지역별 필터링 OFF
    fun regionFilterOff() {
        cl_filter_content_search_shop.visibility = View.GONE
        rv_filter_region_list_search_shop.visibility = View.GONE
        btn_filter_pickup_region_search_shop.isSelected = false
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

        cl_filter_content_search_shop.visibility = View.VISIBLE
        cv_pickup_calendar_search_shop.visibility = View.VISIBLE
        btn_filter_pickup_date_search_shop.isSelected = true
        btn_filter_pickup_date_compact_search_shop.isSelected = true

        clickedPosition = 2
    }

    // 지역별 필터링 OFF
    fun dateFilterOff() {
        Log.d("songjem", "dateFilterOff")
        cl_filter_content_search_shop.visibility = View.GONE
        cv_pickup_calendar_search_shop.visibility = View.GONE
        btn_filter_pickup_date_search_shop.isSelected = false
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