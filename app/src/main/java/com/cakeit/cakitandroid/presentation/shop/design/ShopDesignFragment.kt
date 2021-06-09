package com.cakeit.cakitandroid.presentation.shop.design

import android.content.Intent
import android.os.Bundle
import android.os.Debug
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseFragment
import com.cakeit.cakitandroid.databinding.FragmentShopDesignBinding
import kotlinx.android.synthetic.main.activity_shop_detail.*
import kotlinx.android.synthetic.main.activity_shop_detail.view.*
import kotlinx.android.synthetic.main.fragment_shop_design.*
import kotlinx.android.synthetic.main.fragment_shop_design.view.*

class ShopDesignFragment : BaseFragment<FragmentShopDesignBinding, ShopDesignFragmentViewModel>(), View.OnClickListener {

    lateinit var binding : FragmentShopDesignBinding
    lateinit var shopDesignFragmentViewModel : ShopDesignFragmentViewModel

    lateinit var designGridAdapter: DesignGridAdapter

    companion object {
        const val TAG: String = "ShopDesignFragment.TAG"
        var viewPagerSize = 406
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = getViewDataBinding()
        binding.viewModel = getViewModel()

        recyclerInit(view)

        //TODO : 서버에서 데이터 세팅 필요
        var data = ArrayList<String>()
        data.add("test")
        data.add("test")
        data.add("test")
        data.add("test")
        data.add("test")
        data.add("test")
        data.add("test")
        data.add("test")
        data.add("test")
        data.add("test")
        data.add("test")
        data.add("test")
        designGridAdapter.setRefresh(data)

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_shop_design
    }

    override fun getViewModel(): ShopDesignFragmentViewModel {
        shopDesignFragmentViewModel = ViewModelProvider(this).get(ShopDesignFragmentViewModel::class.java)
        return shopDesignFragmentViewModel
    }

    override fun onClick(view: View) {
        var index = rv_shop_detail_item.getChildAdapterPosition(view)
//        val intent = Intent(context, CakeDetailActivity::class.java)
//        intent.putExtra("cakeIndex", cakeShopDatas[index].cakeShopIndex)
//        startActivity(intent)
        Log.d(TAG, "pressed $index design")
    }

    // recyclerview 초기화
    fun recyclerInit(v: View) {
        designGridAdapter = DesignGridAdapter(context!!)
        designGridAdapter.setOnItemClickListener(this)

        v.rv_shop_detail_item.adapter = designGridAdapter
        v.rv_shop_detail_item.layoutManager = GridLayoutManager(context, 2)
    }
}