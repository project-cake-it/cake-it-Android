package com.cakeit.cakitandroid.presentation.shop.inform

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseFragment
import com.cakeit.cakitandroid.databinding.FragmentShopInformBinding
import com.cakeit.cakitandroid.presentation.shop.ShopDetailViewModel
import kotlinx.android.synthetic.main.fragment_shop_inform.*
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class ShopInformFragment : BaseFragment<FragmentShopInformBinding, ShopDetailViewModel>(), View.OnClickListener {

    lateinit var binding : FragmentShopInformBinding
    lateinit var shopDetailViewModel : ShopDetailViewModel
    var shopAddress: String = ""
    var shopName: String = ""
    var x: Double = 0.0
    var y: Double = 0.0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = getViewDataBinding()
        binding.vm = getViewModel()

        shopDetailViewModel.shopDetailData.observe(viewLifecycleOwner, Observer { datas ->
            if(datas != null)
            {
                shopAddress = datas.fullAddress
                shopName = datas.name

                val geocoderList = Geocoder(activity).getFromLocationName(shopAddress, 1)
                val geocoder = geocoderList.get(0)
                x = geocoder.latitude
                y = geocoder.longitude

                Log.d("mapTest", x.toString())
                Log.d("mapTest", y.toString())

                loadKakaoMap()
            }
            else {
                Log.d("nulkong", "get shopDetail size == 0")
            }
        })

        setListener()
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

        val mapPoint = MapPoint.mapPointWithGeoCoord(x, y)
        mapView.setMapCenterPoint(mapPoint, true)
        mapViewContainer.addView(mapView)

        val marker = MapPOIItem()
        marker.itemName = shopName
        marker.tag = 0
        marker.mapPoint = mapPoint
        marker.markerType = MapPOIItem.MarkerType.BluePin
        mapView.addPOIItem(marker)
    }

    fun setListener()
    {
        btn_shop_inform_copy_address.setOnClickListener(this)
        btn_shop_inform_show_map.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id)
        {
            R.id.btn_shop_inform_copy_address -> {
                val clipboard: ClipboardManager = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip: ClipData = ClipData.newPlainText("address",shopAddress)
                clipboard.setPrimaryClip(clip)

                Toast.makeText(activity, "주소를 복사하였습니다",Toast.LENGTH_SHORT).show()
            }
            R.id.btn_shop_inform_show_map -> {
                var intent = Intent(Intent.ACTION_VIEW)
                intent.addCategory(Intent.CATEGORY_DEFAULT)
                intent.addCategory(Intent.CATEGORY_BROWSABLE)
                intent.setData(Uri.parse("kakaomap://look?p=${x},${y}"))
                startActivity(intent)
            }
        }
    }
}
