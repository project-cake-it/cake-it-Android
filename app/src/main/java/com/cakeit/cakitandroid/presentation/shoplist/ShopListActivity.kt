package com.cakeit.cakitandroid.presentation.shoplist

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseActivity
import com.cakeit.cakitandroid.data.source.local.entity.CakeShopData
import com.cakeit.cakitandroid.databinding.ActivityShopListBinding
import com.cakeit.cakitandroid.domain.model.CakeSizeAndrPrice
import kotlinx.android.synthetic.main.activity_shop_list.*


class ShopListActivity : BaseActivity<ActivityShopListBinding, ShopListViewModel>(),
    View.OnClickListener {

    lateinit var shopListViewModel: ShopListViewModel
    lateinit var shopListBinding: ActivityShopListBinding

    private lateinit var shopListAdapter: ShopListAdapter
    private lateinit var shopListFilterAdapter: ShopListFilterAdapter
    private val shopListItems : ArrayList<CakeShopData> = ArrayList()
    private val shopTagItems : ArrayList<String> = ArrayList()
    private val shopSizeAndPriceItems : ArrayList<CakeSizeAndrPrice> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding()
        shopListBinding = getViewDataBinding()
        shopListBinding.viewModel = getViewModel()

        shopTagItems.add("태그1")
        shopTagItems.add("태그2")
        shopSizeAndPriceItems.add(CakeSizeAndrPrice("미니", 18000))
        shopSizeAndPriceItems.add(CakeSizeAndrPrice("1호", 34000))

        btn_filter_default_shop_list.setOnClickListener(this)
        btn_filter_pickup_region_shop_list.setOnClickListener(this)
        btn_filter_pickup_date_shop_list.setOnClickListener(this)

        initRecyclerview()

/*        shopListItems.run {
            add(CakeShopData(0,"케이크 매장명1", "서울시 노원구 마들로 31"))
            add(CakeShopData(1, "케이크 매장명2", "서울시 서초구 마들로 31"))
            add(CakeShopData(2, "케이크 매장명3", "서울시 중구 마들로 31"))
            add(CakeShopData(3, "케이크 매장명4", "서울시 강북구 마들로 31"))
            add(CakeShopData(4, "케이크 매장명5", "서울시 도봉구 마들로 31"))
            add(CakeShopData(5, "케이크 매장명6", "서울시 강남구 마들로 31"))
            add(CakeShopData(6, "케이크 매장명7", "서울시 성북구 마들로 31"))
            add(CakeShopData(7, "케이크 매장명8", "서울시 마포구 마들로 31"))
        }*/

//        insertTempData()

//        shopListViewModel.cakeShopItems.observe(this, Observer {
//            Log.d("songjem", "observe, shopData size = " + it.size)
//            for(i in 0 .. it.size -1 ) {
//                Log.d("songjem", "data [ " + i + "] = " + it[i].shopName)
//            }
//            shopListAdapter.setShopListItems(it)
//        })

/*        var tempShopListItems = ArrayList<CakeShopData>()
        tempShopListItems.add(CakeShopData(3, "케이크 매장명4", "서울시 강북구 마들로 31"))
        shopListAdapter.setShopListItems(tempShopListItems)*/

        var testFilterItem = ArrayList<String>()
        testFilterItem.add("기본순")
        testFilterItem.add("찜순")
        testFilterItem.add("가격 낮은 순")
        shopListFilterAdapter.setShopListItems(testFilterItem)
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

        shopListFilterAdapter = ShopListFilterAdapter().apply {
            listener = object : ShopListFilterAdapter.OnShopFilterItemClickListener {
                override fun onShopFilterItemClick(position: Int) {
                    Toast.makeText(applicationContext, "filter item" + position + " is clicked", Toast.LENGTH_LONG).show()
                }
            }
        }

        rv_shop_list_shop_list.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@ShopListActivity)
            adapter = shopListAdapter
        }

        rv_filter_list_shop_list.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@ShopListActivity)
            adapter = shopListFilterAdapter
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
            R.id.btn_filter_default_shop_list->{
                if(btn_filter_default_shop_list.isSelected) {
                    tv_filter_default_title_shop_list.setTextColor(Color.parseColor("#242424"));
                    rv_filter_list_shop_list.visibility = View.GONE
                    btn_filter_default_compact_shop_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
                }
                else {
                    tv_filter_default_title_shop_list.setTextColor(Color.parseColor("#577399"));
                    rv_filter_list_shop_list.visibility = View.VISIBLE
                    btn_filter_default_compact_shop_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_up))
                }
                btn_filter_default_shop_list.isSelected = !btn_filter_default_shop_list.isSelected
            }
            R.id.btn_filter_pickup_region_shop_list->{
                if(btn_filter_pickup_region_shop_list.isSelected) {
                    tv_filter_pickup_region_title_shop_list.setTextColor(Color.parseColor("#242424"));
                    rv_filter_list_shop_list.visibility = View.GONE
                    btn_filter_pickup_region_compact_shop_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
                }
                else {
                    tv_filter_pickup_region_title_shop_list.setTextColor(Color.parseColor("#577399"));
                    rv_filter_list_shop_list.visibility = View.VISIBLE
                    btn_filter_pickup_region_compact_shop_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_up))
                }
                btn_filter_pickup_region_shop_list.isSelected = !btn_filter_pickup_region_shop_list.isSelected
            }
            R.id.btn_filter_pickup_date_shop_list->{
                if(btn_filter_pickup_date_shop_list.isSelected) {
                    tv_filter_pickup_date_title_shop_list.setTextColor(Color.parseColor("#242424"));
                    rv_filter_list_shop_list.visibility = View.GONE
                    btn_filter_pickup_date_compact_shop_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_down))
                }
                else {
                    tv_filter_pickup_date_title_shop_list.setTextColor(Color.parseColor("#577399"));
                    rv_filter_list_shop_list.visibility = View.VISIBLE
                    btn_filter_pickup_date_compact_shop_list.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_chevron_compact_up))
                }
                btn_filter_pickup_date_shop_list.isSelected = !btn_filter_pickup_date_shop_list.isSelected
            }
        }
    }
}