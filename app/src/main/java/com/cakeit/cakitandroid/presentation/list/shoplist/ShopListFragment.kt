package com.cakeit.cakitandroid.presentation.list.shoplist

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
import com.cakeit.cakitandroid.databinding.FragmentShopListBinding
import com.cakeit.cakitandroid.presentation.list.MinDecorator
import com.cakeit.cakitandroid.presentation.list.shoplist.filter.ShopChoiceTagAdapter
import com.cakeit.cakitandroid.presentation.list.shoplist.filter.ShopDefaultFilterAdapter
import com.cakeit.cakitandroid.presentation.list.shoplist.filter.ShopRegionFilterAdapter
import com.cakeit.cakitandroid.presentation.shop.ShopDetailActivity
import com.cakeit.cakitandroid.presentation.shop.calendar.TodayDecorator
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import kotlinx.android.synthetic.main.fragment_shop_list.*
import kotlin.collections.ArrayList

class ShopListFragment : BaseFragment<FragmentShopListBinding, ShopListViewModel>(), View.OnClickListener {

    lateinit var shopListViewModel: ShopListViewModel
    lateinit var shopListBinding: FragmentShopListBinding

    lateinit var shopListAdapter: ShopListAdapter
    lateinit var shopChoiceTagAdapter : ShopChoiceTagAdapter
    lateinit var shopDefaultFilterAdapter: ShopDefaultFilterAdapter
    lateinit var shopRegionFilterAdapter: ShopRegionFilterAdapter

    private lateinit var regionItems: ArrayList<String>
    lateinit var choiceTagItems: ArrayList<ChoiceTag>
    private lateinit var filterItems: ArrayList<String>

    private var clickedPosition = -1;

    private val filterList = listOf<String>("기본순", "찜순", "가격 낮은 순")
    private val regionList = listOf<String>("전체", "강남구", "관악구", "광진구", "마포구", "서대문구"
        , "송파구")
    private var selectedDate : String = ""
    var listSelected = mutableListOf<Boolean>(false, false, false)

    var choicePickupDate : String? = null
    lateinit var selecedLocList : ArrayList<String>
    var selectedOrder : String? = null
    lateinit var cakeShopIds : ArrayList<Int>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shopListBinding = getViewDataBinding()
        shopListBinding.viewModel = getViewModel()

        choiceTagItems = ArrayList()
        initRecyclerview()

        view_background_shop_list.setOnClickListener(this)
        btn_filter_refresh_shop_list.setOnClickListener(this)
        btn_filter_default_shop_list.setOnClickListener(this)
        btn_filter_pickup_region_shop_list.setOnClickListener(this)
        btn_filter_pickup_date_shop_list.setOnClickListener(this)

        shopListAdapter.setOnItemClickListener(object : ShopListAdapter.OnShopItemClickListener{

            override fun onShopItemClick(position: Int) {
                val intent = Intent(context!!, ShopDetailActivity::class.java)
                Log.d("songjem", "ShopListFragment clickId = " + position)
                intent.putExtra("cakeShopId", cakeShopIds[position])
                startActivity(intent)
            }
        })

        shopListViewModel.cakeShopItems.observe(viewLifecycleOwner, Observer { datas ->
            cakeShopIds = ArrayList<Int>()
            if(datas.size > 0) {
                for(data in datas) {
                    cakeShopIds.add(data!!.shopId)
                }
            }
            else {
                Log.d("songjem", "get shopList size == 0")
            }
            shopListAdapter.setShopListItems(datas)
        })

        getshopList()
        shopListFragment = this

        cv_pickup_calendar_shop_list.addDecorators(
            MinDecorator(
                context!!
            ),
            TodayDecorator(
                context!!
            )
        )

        // 달력 날짜 선택
        cv_pickup_calendar_shop_list.setOnDateChangedListener(OnDateSelectedListener { widget, date, selected ->
            selectedDate = date.month.toString() + "월 " + date.day.toString() + "일"
            var pickupMonth = ""
            var pickupDay = ""
            if(date.month < 10) pickupMonth = "0" + date.month
            else pickupMonth = date.month.toString()

            if(date.day < 10) pickupDay = "0" + date.day
            else pickupDay = date.day.toString()

            choicePickupDate = date.year.toString() + pickupMonth + pickupDay

            btn_filter_pickup_date_shop_list.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_btn_effect))
            btn_filter_pickup_date_compact_shop_list.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_compact))
            btn_filter_pickup_date_shop_list.isSelected = false

            listSelected[2] = true
            dateFilterOff()

            // 기존 선택한 날짜 값(초이스) 지우기
            deleteChoiceTag(2)

            // 선택한 날짜 태그 리스트에 추가
            choiceTagItems.add(ChoiceTag(2, 0, selectedDate!!))

            // 리스트 화면 re visible
            view_background_shop_list.visibility = View.INVISIBLE
            rv_shop_list_shop_list.visibility = View.VISIBLE

            shopChoiceTagAdapter.setChoiceTagItem(choiceTagItems)
            getShopListByNetwork(choiceTagItems)
        })

    }

    fun getshopList() {
        selecedLocList = ArrayList<String>()
        shopListViewModel.sendParamsForShopList(selectedOrder, selecedLocList, choicePickupDate)
    }

    fun initRecyclerview() {

        shopChoiceTagAdapter = ShopChoiceTagAdapter().apply {
        }
        shopListAdapter = ShopListAdapter(context!!)

        shopDefaultFilterAdapter = ShopDefaultFilterAdapter()
                .apply {
                    listener = object : ShopDefaultFilterAdapter.OnShopFilterItemClickListener {
                        override fun onShopFilterItemClick(position: Int) {
                            Toast.makeText(context!!, "filter item" + position + " is clicked", Toast.LENGTH_LONG).show()
                        }
                    }
                }

        shopRegionFilterAdapter = ShopRegionFilterAdapter()
                .apply {
                    listener = object : ShopRegionFilterAdapter.OnShopFilterItemClickListener {
                        override fun onShopFilterItemClick(position: Int) {
                            Toast.makeText(context!!, "region item" + position + " is clicked", Toast.LENGTH_LONG).show()
                        }
                    }
                }

        rv_choice_tag_shop_list.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!!, LinearLayoutManager.HORIZONTAL, false)
            adapter = shopChoiceTagAdapter
        }

        rv_shop_list_shop_list.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!!)
            adapter = shopListAdapter
        }

        rv_filter_default_list_shop_list.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!!)
            adapter = shopDefaultFilterAdapter
        }

        rv_filter_region_list_shop_list.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!!)
            adapter = shopRegionFilterAdapter
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_shop_list
    }

    override fun getViewModel(): ShopListViewModel {
        shopListViewModel = ViewModelProvider(this).get(ShopListViewModel::class.java)
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
        // TAG 리스트 초기화
        selecedLocList = ArrayList<String>()

        // TAG 리스트 가져오기
        for(i in 0.. choiceTagItems.size - 1) {
            // 기본 정렬
            if(choiceTagItems[i].filterCode == 0) {
                sv_choice_tag_shop_list.visibility = View.VISIBLE
            }
            // 장소
            else if(choiceTagItems[i].filterCode == 1) {
                // 전체
                if(choiceTagItems[i].choiceCode == 0) {
                    for(i in 1.. choiceTagItems.size - 1) {
                        selecedLocList.add(choiceTagItems[i].choiceName)
                    }
                }
                else selecedLocList.add(choiceTagItems[i].choiceName)
            }
            // 날짜
            else if(choiceTagItems[i].filterCode == 2) {
                sv_choice_tag_shop_list.visibility = View.VISIBLE
            }
        }
        if(selectedOrder != null || selecedLocList.size > 0 || choicePickupDate != null) {
            sv_choice_tag_shop_list.visibility = View.VISIBLE
        } else {
            sv_choice_tag_shop_list.visibility = View.GONE
        }
        if(selectedOrder.equals(filterList[0])) selectedOrder = null
        else if(selectedOrder.equals(filterList[1])) selectedOrder = "zzim"
        else if(selectedOrder.equals(filterList[2])) selectedOrder = "cheap"

        shopListViewModel.sendParamsForShopList(selectedOrder, selecedLocList, choicePickupDate)

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.view_background_shop_list -> {
                Log.d("songjem", "background is touched")
                view_background_shop_list.visibility = View.INVISIBLE
                rv_shop_list_shop_list.visibility = View.VISIBLE
                if(clickedPosition == 0) {
                    btn_filter_default_shop_list.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_btn_effect))
                    btn_filter_default_compact_shop_list.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_compact))

                    listSelected[0] = true
                    defaultFilterOff()
                    selectedOrder = shopDefaultFilterAdapter.getClickedItem()
                    tv_filter_default_title_shop_list.text = selectedOrder

                    // 기존 정렬 값(초이스) 지우기
                    deleteChoiceTag(0)
                    choiceTagItems.add(ChoiceTag(0, 0, selectedOrder!!))
                    shopChoiceTagAdapter.setChoiceTagItem(choiceTagItems)

                    getShopListByNetwork(choiceTagItems)
                }
                else if(clickedPosition == 1) {
                    btn_filter_pickup_region_shop_list.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_btn_effect))
                    btn_filter_pickup_region_compact_shop_list.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_compact))

                    listSelected[1] = true
                    regionFilterOff()

                    // 기존 장소 값(초이스) 지우기
                    deleteChoiceTag(1)

                    // 추가한 리스트 가져와서 리스트에 넣어야 함
                    var tagList = shopRegionFilterAdapter.getChoiceTagIndex()

                    // 전체 선택
                    if(tagList[0] == 0) {
                        clearRegion()
                        listSelected[1] = false
                        for(i in 1.. regionList.size-1) {
//                            choiceTagItems.add(ChoiceTag(1, i, regionList[i]))
                            choiceTagItems.remove(ChoiceTag(1, i, regionList[i]))
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
                    dateFilterOff()
                }
            }
            R.id.btn_filter_refresh_shop_list -> {
                view_background_shop_list.visibility = View.INVISIBLE
                rv_shop_list_shop_list.visibility = View.VISIBLE

                choicePickupDate = null

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
        btn_filter_default_shop_list.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_btn_effect_before))
        btn_filter_default_compact_shop_list.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_compact_before))
        btn_filter_default_shop_list.isSelected = false
        btn_filter_default_compact_shop_list.isSelected = false
        tv_filter_default_title_shop_list.setTextColor(Color.parseColor("#000000"))
        tv_filter_default_title_shop_list.text = "기본순"
        shopDefaultFilterAdapter.checkedPosition.clear()
        shopDefaultFilterAdapter.checkedPosition.add(0)

        selectedOrder = null
    }
    // 장소 선택 초기화
    fun clearRegion() {
        btn_filter_pickup_region_shop_list.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_btn_effect_before))
        btn_filter_pickup_region_compact_shop_list.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_compact_before))
        btn_filter_pickup_region_shop_list.isSelected = false
        btn_filter_pickup_region_compact_shop_list.isSelected = false
        tv_filter_pickup_region_title_shop_list.setTextColor(Color.parseColor("#000000"))
        tv_filter_pickup_region_title_shop_list.text = "픽업 지역"
        shopRegionFilterAdapter.checkedPosition.clear()
        shopRegionFilterAdapter.checkedPosition.add(0)
    }
    // 날짜 선택 초기화
    fun clearDate() {
        btn_filter_pickup_date_shop_list.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_btn_effect_before))
        btn_filter_pickup_date_compact_shop_list.setBackground(ContextCompat.getDrawable(context!!, R.drawable.background_filter_compact_before))
        btn_filter_pickup_date_shop_list.isSelected = false
        btn_filter_pickup_date_compact_shop_list.isSelected = false
        tv_filter_pickup_date_title_shop_list.setTextColor(Color.parseColor("#000000"))
        tv_filter_pickup_date_title_shop_list.text = "주문 가능 날짜"
        selectedDate = ""
        choicePickupDate = null
    }

    // 기본순 필터링 ON
    fun defaultFilterOn() {
        setFilterItem(0)
        tv_filter_default_title_shop_list.setTextColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
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
        tv_filter_pickup_region_title_shop_list.setTextColor(ContextCompat.getColor(context!!, R.color.colorPrimary))

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
        tv_filter_pickup_date_title_shop_list.setTextColor(ContextCompat.getColor(context!!, R.color.colorPrimary))

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
        lateinit var shopListFragment : ShopListFragment
    }
}