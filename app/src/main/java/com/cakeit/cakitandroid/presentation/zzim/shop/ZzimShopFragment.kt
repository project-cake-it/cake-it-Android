package com.cakeit.cakitandroid.presentation.zzim.shop

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseFragment
import com.cakeit.cakitandroid.databinding.FragmentZzimShopBinding
import com.cakeit.cakitandroid.presentation.list.shoplist.ShopListAdapter
import com.cakeit.cakitandroid.presentation.shop.ShopDetailActivity
import com.cakeit.cakitandroid.presentation.zzim.ZzimViewModel
import kotlinx.android.synthetic.main.fragment_zzim_shop.*

class ZzimShopFragment : BaseFragment<FragmentZzimShopBinding, ZzimViewModel>() {

    lateinit var binding : FragmentZzimShopBinding
    lateinit var zzimViewModel : ZzimViewModel
    lateinit var shopListAdapter : ShopListAdapter
    lateinit var zzimCakeShopIds : ArrayList<Int>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = getViewDataBinding()
        binding.viewModel = getViewModel()

        initRecyclerview()

        shopListAdapter.setOnItemClickListener(object : ShopListAdapter.OnShopItemClickListener{

            override fun onShopItemClick(position: Int) {
                Log.d("songjem", "position = " + position + "zzimCakeShopIds = " + zzimCakeShopIds[position])
                val intent = Intent(context, ShopDetailActivity::class.java)
                intent.putExtra("cakeShopId", zzimCakeShopIds[position])
                startActivity(intent)
            }
        })

        zzimViewModel.cakeShopItems.observe(viewLifecycleOwner, Observer { datas ->
            zzimCakeShopIds = ArrayList<Int>()
            if(datas.size > 0) {
                for(data in datas) {
                   zzimCakeShopIds.add(data.shopId!!)
                }
            }
            else {
                Log.d("ssongjem", "get zzim shopList size == 0")
            }
            shopListAdapter.setShopListItems(datas)
        })

        getZzimShoplist()
    }

    fun getZzimShoplist() {
        zzimViewModel.sendParamsForZzimShopList()
    }

    fun initRecyclerview() {
        shopListAdapter = ShopListAdapter(context!!)
        rv_zzim_shop_list.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context!!)
            adapter = shopListAdapter
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_zzim_shop
    }

    override fun getViewModel(): ZzimViewModel {
        zzimViewModel = ViewModelProvider(parentFragment!!).get(ZzimViewModel::class.java)
        return zzimViewModel
    }
}