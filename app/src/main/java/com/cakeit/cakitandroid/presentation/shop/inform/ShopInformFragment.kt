package com.cakeit.cakitandroid.presentation.shop.inform

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseFragment
import com.cakeit.cakitandroid.databinding.FragmentShopInformBinding
import com.cakeit.cakitandroid.presentation.shop.ShopDetailViewModel
import kotlinx.android.synthetic.main.fragment_shop_inform.*
import net.daum.mf.map.api.MapView

class ShopInformFragment : BaseFragment<FragmentShopInformBinding, ShopDetailViewModel>() {

    lateinit var binding : FragmentShopInformBinding
    lateinit var shopDetailViewModel : ShopDetailViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = getViewDataBinding()
        binding.vm = getViewModel()

        loadKakaoMap()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_shop_inform
    }

    override fun getViewModel(): ShopDetailViewModel {
        shopDetailViewModel = ViewModelProvider(requireActivity()).get(ShopDetailViewModel::class.java)
        return shopDetailViewModel
    }

    fun loadKakaoMap()
    {
        val mapView = MapView(activity)
        val mapViewContainer = iv_shop_inform_map as ViewGroup

        mapViewContainer.addView(mapView)
    }
}