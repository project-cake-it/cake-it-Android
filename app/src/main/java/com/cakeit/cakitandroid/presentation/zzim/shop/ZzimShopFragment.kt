package com.cakeit.cakitandroid.presentation.zzim.shop

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseFragment
import com.cakeit.cakitandroid.data.source.local.prefs.SharedPreferenceController
import com.cakeit.cakitandroid.databinding.FragmentZzimShopBinding
import com.cakeit.cakitandroid.presentation.list.shoplist.ShopListAdapter
import com.cakeit.cakitandroid.presentation.list.shoplist.ShopListFragment
import com.cakeit.cakitandroid.presentation.shop.ShopDetailActivity
import com.cakeit.cakitandroid.presentation.zzim.ZzimViewModel
import kotlinx.android.synthetic.main.fragment_zzim_shop.*

class ZzimShopFragment : BaseFragment<FragmentZzimShopBinding, ZzimViewModel>() {

    lateinit var binding : FragmentZzimShopBinding
    lateinit var zzimViewModel : ZzimViewModel
    lateinit var shopListAdapter : ShopListAdapter
    lateinit var zzimCakeShopIds : ArrayList<Int>
    val REQUEST_CODE = 1

    private lateinit var authorization : String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        zzimShopFragment = this

        binding = getViewDataBinding()
        binding.viewModel = getViewModel()

        authorization = SharedPreferenceController.getToken(context!!)
        initRecyclerview()

        shopListAdapter.setOnItemClickListener(object : ShopListAdapter.OnShopItemClickListener{

            override fun onShopItemClick(position: Int) {
                val intent = Intent(context, ShopDetailActivity::class.java)
                intent.putExtra("cakeShopId", zzimCakeShopIds[position])
                intent.putExtra("fromToZzim", true)
                startActivityForResult(intent, REQUEST_CODE)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            getZzimShoplist()
        }
    }

    fun getZzimShoplist() {
        zzimViewModel.sendParamsForZzimShopList(authorization)
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

    companion object {
        var zzimShopFragment : ZzimShopFragment? = null
    }
}