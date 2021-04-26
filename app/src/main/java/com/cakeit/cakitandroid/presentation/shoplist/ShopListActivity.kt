package com.cakeit.cakitandroid.presentation.shoplist

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseActivity
import com.cakeit.cakitandroid.data.source.local.entity.CakeShopData
import com.cakeit.cakitandroid.databinding.ActivityShopListBinding
import com.cakeit.cakitandroid.domain.model.CakeSizeAndrPrice
import kotlinx.android.synthetic.main.activity_shop_list.*

class ShopListActivity : BaseActivity<ActivityShopListBinding, ShopListViewModel>() {

    lateinit var shopListViewModel: ShopListViewModel
    lateinit var shopListBinding: ActivityShopListBinding

    private lateinit var shopListAdapter: ShopListAdapter
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
        initRecyclerview()
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
                    Toast.makeText(applicationContext, "item" + position + " is clicked", Toast.LENGTH_LONG).show()
                }
            }
        }

        rv_shop_list_.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@ShopListActivity)
            adapter = shopListAdapter
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
}