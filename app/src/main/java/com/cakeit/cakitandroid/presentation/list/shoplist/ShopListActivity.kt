package com.cakeit.cakitandroid.presentation.list.shoplist
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
import com.cakeit.cakitandroid.data.source.local.entity.ChoiceTag
import com.cakeit.cakitandroid.databinding.ActivityShopListBinding
import com.cakeit.cakitandroid.presentation.list.MinDecorator
import com.cakeit.cakitandroid.presentation.list.TodayDecorator
import com.cakeit.cakitandroid.presentation.list.shoplist.filter.ShopChoiceTagAdapter
import com.cakeit.cakitandroid.presentation.list.shoplist.filter.ShopDefaultFilterAdapter
import com.cakeit.cakitandroid.presentation.list.shoplist.filter.ShopRegionFilterAdapter
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import kotlinx.android.synthetic.main.activity_shop_list.*
import kotlin.collections.ArrayList

class ShopListActivity : BaseActivity<ActivityShopListBinding, ShopListViewModel>(),
        View.OnClickListener {

    lateinit var shopListViewModel: ShopListViewModel
    lateinit var shopListBinding: ActivityShopListBinding

    lateinit var shopListAdapter: ShopListAdapter
    lateinit var shopChoiceTagAdapter : ShopChoiceTagAdapter
    lateinit var shopDefaultFilterAdapter: ShopDefaultFilterAdapter
    lateinit var shopRegionFilterAdapter: ShopRegionFilterAdapter

    private lateinit var regionItems: ArrayList<String>
    lateinit var choiceTagItems: ArrayList<ChoiceTag>
    private lateinit var filterItems: ArrayList<String>

    private var clickedPosition = -1;

    private val filterList = listOf<String>("기본순", "찜순", "가격 높은 순", "가격 낮은 순")
    private val filterTransList = listOf<String>("DEFAULT", "ZZIM", "HIGH_PRICE", "LOW_PRICE")
    private val regionList = listOf<String>("전체", "강남구", "관악구", "광진구", "마포구", "서대문구"
            , "송파구", "노원구", "성북구", "중구", "중랑구")
    private lateinit var selectedDate : String
    var listSelected = mutableListOf<Boolean>(false, false, false)

    lateinit var selecedLocList : ArrayList<String>
    var selectedTheme : String? = null
    var selectedOrder : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding()
        shopListBinding = getViewDataBinding()
        shopListBinding.viewModel = getViewModel()

        choiceTagItems = ArrayList()

        view_background_shop_list.setOnClickListener(this)
        btn_filter_refresh_shop_list.setOnClickListener(this)
        btn_filter_default_shop_list.setOnClickListener(this)
        btn_filter_pickup_region_shop_list.setOnClickListener(this)
        btn_filter_pickup_date_shop_list.setOnClickListener(this)

        initRecyclerview()

        shopListViewModel.cakeShopItems.observe(this, Observer { datas ->
            if(datas.size > 0) {
            }
            else {
                Log.d("songjem", "get shopList size == 0")
            }
            shopListAdapter.setShopListItems(datas)
        })
        getshopList()
        shopListActivity = this

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
            selectedDate = date.month.toString() + "월 " + date.day.toString() + "일"
        })
    }

    fun getshopList() {

        selectedTheme = "NONE"
        selecedLocList = ArrayList<String>()
        selectedOrder = "DEFAULT"
        selectedDate = ""

        Log.d("songjem", "theme = " + selectedTheme)
        Log.d("songjem", "locList = " + selecedLocList.toString())
        Log.d("songjem", "order = " + selectedOrder)
        shopListViewModel.sendParamsForShopList(selectedTheme, selecedLocList, selectedDate, selectedOrder)
    }

    fun initRecyclerview() {

        shopListAdapter = ShopListAdapter(applicationContext)

        shopChoiceTagAdapter = ShopChoiceTagAdapter().apply {

        }

        shopDefaultFilterAdapter = ShopDefaultFilterAdapter()
                .apply {
                    listener = object : ShopDefaultFilterAdapter.OnShopFilterItemClickListener {
                        override fun onShopFilterItemClick(position: Int) {
                            Toast.makeText(applicationContext, "filter item" + position + " is clicked", Toast.LENGTH_LONG).show()
                        }
                    }
                }

        shopRegionFilterAdapter = ShopRegionFilterAdapter()
                .apply {
                    listener = object : ShopRegionFilterAdapter.OnShopFilterItemClickListener {
                        override fun onShopFilterItemClick(position: Int) {
                            Toast.makeText(applicationContext, "region item" + position + " is clicked", Toast.LENGTH_LONG).show()
                        }
                    }
                }

        rv_choice_tag_shop_list.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@ShopListActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = shopChoiceTagAdapter
        }

        rv_shop_list_shop_list.run {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@ShopListActivity, 2)
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
        return shopListViewModel
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
        }
        Log.d("songjem", "theme = " + selectedTheme)
        Log.d("songjem", "locList = " + selecedLocList.toString())
        Log.d("songjem", "order = " + selectedOrder)
        shopListViewModel.sendParamsForShopList(selectedTheme, selecedLocList, selectedDate, selectedOrder)

    }
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.view_background_shop_list -> {
                Log.d("songjem", "background is touched")
                view_background_shop_list.visibility = View.INVISIBLE
                rv_shop_list_shop_list.visibility = View.VISIBLE
                if(clickedPosition == 0) {
                    btn_filter_default_shop_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_btn_effect))
                    btn_filter_default_compact_shop_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_compact))

                    listSelected[0] = true
                    defaultFilterOff()
                    selectedOrder = shopDefaultFilterAdapter.getClickedItem()
                    tv_filter_default_title_shop_list.text = selectedOrder

                    if(selectedOrder.equals("기본순")) selectedOrder = "DEFAULT"
                    else if(selectedOrder.equals("찜순")) selectedOrder = "ZZIM"
                    else if(selectedOrder.equals("가격 높은 순")) selectedOrder = "HIGH_PRICE"
                    else if(selectedOrder.equals("가격 낮은 순")) selectedOrder = "LOW_PRICE"

                    getShopListByNetwork(choiceTagItems)
                }
                else if(clickedPosition == 1) {
                    btn_filter_pickup_region_shop_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_btn_effect))
                    btn_filter_pickup_region_compact_shop_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_compact))

                    listSelected[1] = true
                    regionFilterOff()

                    // 기존 장소 값(초이스) 지우기
                    deleteChoiceTag(1)

                    // 추가한 리스트 가져와서 리스트에 넣어야 함
                    var tagList = shopRegionFilterAdapter.getChoiceTagIndex()

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

                    shopChoiceTagAdapter.setChoiceTagItem(choiceTagItems)
                    getShopListByNetwork(choiceTagItems)
                }
                else if(clickedPosition == 2) {
                    btn_filter_pickup_date_shop_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_btn_effect))
                    btn_filter_pickup_date_compact_shop_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_compact))
                    listSelected[2] = true

                    tv_filter_pickup_date_title_shop_list.text = selectedDate
                    Log.d("song", "selectedDate = " + selectedDate)
                    dateFilterOff()
                    btn_filter_pickup_date_shop_list.isSelected = false

                    getShopListByNetwork(choiceTagItems)
                }
            }
            R.id.btn_filter_refresh_shop_list -> {
                view_background_shop_list.visibility = View.INVISIBLE
                rv_shop_list_shop_list.visibility = View.VISIBLE

                for(i in 0 .. 2) {
                    listSelected[i] = false
                }
                cl_filter_content_shop_list.visibility = View.GONE
                rv_filter_default_list_shop_list.visibility = View.GONE

                clearDefault()
                clearRegion()
                clearDate()

                choiceTagItems = ArrayList()
                shopChoiceTagAdapter.setChoiceTagItem(choiceTagItems)
                getShopListByNetwork(choiceTagItems)
            }
            R.id.btn_filter_default_shop_list -> {
                if(!btn_filter_default_shop_list.isSelected) {
                    setFilterItem(0)
                    regionFilterOff()
                    dateFilterOff()
                    defaultFilterOn()
                    view_background_shop_list.visibility = View.VISIBLE
                    rv_shop_list_shop_list.visibility = View.GONE
                }
                else {
                    defaultFilterOff()
                    view_background_shop_list.visibility = View.INVISIBLE
                    rv_shop_list_shop_list.visibility = View.VISIBLE
                }
            }
            R.id.btn_filter_pickup_region_shop_list -> {
                if(!btn_filter_pickup_region_shop_list.isSelected) {
                    setFilterItem(1)
                    defaultFilterOff()
                    dateFilterOff()
                    regionFilterOn()
                    view_background_shop_list.visibility = View.VISIBLE
                    rv_shop_list_shop_list.visibility = View.GONE
                }
                else {
                    regionFilterOff()
                    view_background_shop_list.visibility = View.INVISIBLE
                    rv_shop_list_shop_list.visibility = View.VISIBLE
                }
            }
            R.id.btn_filter_pickup_date_shop_list -> {
                if(!btn_filter_pickup_date_shop_list.isSelected) {
                    defaultFilterOff()
                    regionFilterOff()
                    dateFilterOn()
                    view_background_shop_list.visibility = View.VISIBLE
                    rv_shop_list_shop_list.visibility = View.GONE
                }
                else {
                    dateFilterOff()
                    view_background_shop_list.visibility = View.INVISIBLE
                    rv_shop_list_shop_list.visibility = View.VISIBLE
                }
            }
        }
    }
    // 기본순 초기화
    fun clearDefault() {
        btn_filter_default_shop_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_btn))
        btn_filter_default_compact_shop_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_compact_before))
        btn_filter_default_shop_list.isSelected = false
        btn_filter_default_compact_shop_list.isSelected = false
        tv_filter_default_title_shop_list.setTextColor(Color.parseColor("#000000"))
        tv_filter_default_title_shop_list.text = "기본순"
        shopDefaultFilterAdapter.checkedPosition.clear()
    }
    // 장소 선택 초기화
    fun clearRegion() {
        btn_filter_pickup_region_shop_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_btn))
        btn_filter_pickup_region_compact_shop_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_compact_before))
        btn_filter_pickup_region_shop_list.isSelected = false
        btn_filter_pickup_region_compact_shop_list.isSelected = false
        tv_filter_pickup_region_title_shop_list.setTextColor(Color.parseColor("#000000"))
        tv_filter_pickup_region_title_shop_list.text = "픽업 지역"
        shopRegionFilterAdapter.checkedPosition.clear()
    }
    // 날짜 선택 초기화
    fun clearDate() {
        btn_filter_pickup_date_shop_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_btn))
        btn_filter_pickup_date_compact_shop_list.setBackground(ContextCompat.getDrawable(this, R.drawable.background_filter_compact_before))
        btn_filter_pickup_date_shop_list.isSelected = false
        btn_filter_pickup_date_compact_shop_list.isSelected = false
        tv_filter_pickup_date_title_shop_list.setTextColor(Color.parseColor("#000000"))
        tv_filter_pickup_date_title_shop_list.text = "픽업 날짜"
    }

    // 기본순 필터링 ON
    fun defaultFilterOn() {
        setFilterItem(0)
        tv_filter_default_title_shop_list.setTextColor(Color.parseColor("#577399"))
        cl_filter_content_shop_list.visibility = View.VISIBLE
        rv_filter_default_list_shop_list.visibility = View.VISIBLE
        btn_filter_default_shop_list.isSelected = true
        btn_filter_default_compact_shop_list.isSelected = true

        clickedPosition = 0
    }

    // 기본순 필터링 OFF
    fun defaultFilterOff() {
        cl_filter_content_shop_list.visibility = View.GONE
        rv_filter_default_list_shop_list.visibility = View.GONE
        btn_filter_default_shop_list.isSelected = false
        btn_filter_default_compact_shop_list.isSelected = false

        if(listSelected[0] == false){
            tv_filter_default_title_shop_list.setTextColor(Color.parseColor("#000000"))
        }
        else {
            tv_filter_default_title_shop_list.setTextColor(Color.parseColor("#ffffff"))
        }
    }

    // 지역별 필터링 ON
    fun regionFilterOn() {
        setFilterItem(1)
        tv_filter_pickup_region_title_shop_list.setTextColor(Color.parseColor("#577399"))

        cl_filter_content_shop_list.visibility = View.VISIBLE
        rv_filter_region_list_shop_list.visibility = View.VISIBLE
        btn_filter_pickup_region_shop_list.isSelected = true
        btn_filter_pickup_region_compact_shop_list.isSelected = true

        clickedPosition = 1
    }

    // 지역별 필터링 OFF
    fun regionFilterOff() {
        cl_filter_content_shop_list.visibility = View.GONE
        rv_filter_region_list_shop_list.visibility = View.GONE
        btn_filter_pickup_region_shop_list.isSelected = false
        btn_filter_pickup_region_compact_shop_list.isSelected = false

        if(listSelected[1] == false){
            tv_filter_pickup_region_title_shop_list.setTextColor(Color.parseColor("#000000"))
        }
        else {
            tv_filter_pickup_region_title_shop_list.setTextColor(Color.parseColor("#ffffff"))
        }
    }

    // 날짜 필터링 ON
    fun dateFilterOn() {
        tv_filter_pickup_date_title_shop_list.setTextColor(Color.parseColor("#577399"))

        cl_filter_content_shop_list.visibility = View.VISIBLE
        cv_pickup_calendar_shop_list.visibility = View.VISIBLE
        btn_filter_pickup_date_shop_list.isSelected = true
        btn_filter_pickup_date_compact_shop_list.isSelected = true

        clickedPosition = 2
    }

    // 지역별 필터링 OFF
    fun dateFilterOff() {
        Log.d("songjem", "dateFilterOff")
        cl_filter_content_shop_list.visibility = View.GONE
        cv_pickup_calendar_shop_list.visibility = View.GONE
        btn_filter_pickup_date_shop_list.isSelected = false
        btn_filter_pickup_date_compact_shop_list.isSelected = false

        if(listSelected[2] == false){
            tv_filter_pickup_date_title_shop_list.setTextColor(Color.parseColor("#000000"))
        }
        else {
            tv_filter_pickup_date_title_shop_list.setTextColor(Color.parseColor("#ffffff"))
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
                shopDefaultFilterAdapter.setDefaultListItems(filterItems)
            }
            // 픽업 지역 필터
            1 -> {
                regionItems = ArrayList<String>()
                for (i in 0..regionList.size - 1) {
                    regionItems.add(regionList[i])
                }
                shopRegionFilterAdapter.setRegionListItems(regionItems)
            }
        }
    }

    companion object {
        lateinit var shopListActivity : ShopListActivity
    }
}