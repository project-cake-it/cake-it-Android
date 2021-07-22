package com.cakeit.cakitandroid.presentation.shop.design

import android.content.Intent
import android.os.Bundle
import android.os.Debug
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseFragment
import com.cakeit.cakitandroid.databinding.FragmentShopDesignBinding
import com.cakeit.cakitandroid.presentation.design.DesignDetailActivity
import com.cakeit.cakitandroid.presentation.shop.ShopDetailViewModel
import kotlinx.android.synthetic.main.activity_shop_detail.*
import kotlinx.android.synthetic.main.activity_shop_detail.tv_cake_detail_size_price_contents
import kotlinx.android.synthetic.main.activity_shop_detail.view.*
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.fragment_shop_design.*
import kotlinx.android.synthetic.main.fragment_shop_design.view.*

class ShopDesignFragment : BaseFragment<FragmentShopDesignBinding, ShopDetailViewModel>() {

    lateinit var binding : FragmentShopDesignBinding
    lateinit var shopDetailViewModel : ShopDetailViewModel

    lateinit var designGridAdapter: DesignGridAdapter

    var designId = ArrayList<Int>()

    companion object {
        const val TAG: String = "ShopDesignFragment.TAG"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = getViewDataBinding()
        binding.vm = getViewModel()

        setRecycler(view)

        shopDetailViewModel.shopDetailData.observe(requireActivity(), Observer { datas ->
            if(datas != null) {
                var designImages = ArrayList<String>()
                for(design in datas.designs) {
                    designImages.add(design.designImage.designImageUrl)
                    designId.add(design.id)
                }
                designGridAdapter.setRefresh(designImages)
            }
            else {
                Log.d("nulkong", "get shopDetail size == 0")
            }
        })
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_shop_design
    }

    override fun getViewModel(): ShopDetailViewModel {
        shopDetailViewModel = ViewModelProvider(requireActivity()).get(ShopDetailViewModel::class.java)
        return shopDetailViewModel
    }

    // recyclerview 초기화
    fun setRecycler(v: View) {
        designGridAdapter = DesignGridAdapter(context!!)
        designGridAdapter.setOnItemClickListener(object : DesignGridAdapter.OnItemClickListener{
            override fun OnClick(view: View, position: Int) {
                val intent = Intent(context, DesignDetailActivity::class.java)
                intent.putExtra("designId", designId[position])
                startActivity(intent)
            }
        })

        v.rv_shop_detail_item.adapter = designGridAdapter
        v.rv_shop_detail_item.layoutManager = GridLayoutManager(context, 2)
    }
}