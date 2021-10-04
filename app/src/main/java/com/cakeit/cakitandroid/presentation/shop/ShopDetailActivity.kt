package com.cakeit.cakitandroid.presentation.shop

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseActivity
import com.cakeit.cakitandroid.data.source.local.prefs.SharedPreferenceController
import com.cakeit.cakitandroid.databinding.ActivityShopDetailBinding
import com.cakeit.cakitandroid.presentation.shop.calendar.CalendarActivity
import com.google.android.material.tabs.TabLayout
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.talk.TalkApiClient
import kotlinx.android.synthetic.main.activity_shop_detail.*
import java.text.DecimalFormat
import kotlinx.android.synthetic.main.activity_shop_detail.tv_cake_detail_size_price_contents
import kotlin.properties.Delegates

class ShopDetailActivity : BaseActivity<ActivityShopDetailBinding, ShopDetailViewModel>() {

    private var orderDates = ArrayList<String>()
    private val adapter by lazy { ContentsPagerAdapter(supportFragmentManager, 2) }
    private lateinit var binding : ActivityShopDetailBinding
    private lateinit var shopDetailViewModel: ShopDetailViewModel
    private var zzim : Boolean = false
    private lateinit var shopChannel : String
    private var fromToZzim : Boolean = false

    private lateinit var authorization : String

    var shopId by Delegates.notNull<Int>()

    companion object {
        const val TAG: String = "ShopActivityTAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataBinding()
        binding = getViewDataBinding()
        binding.vm = getViewModel()

        authorization = SharedPreferenceController.getToken(applicationContext)
        fromToZzim = intent.getBooleanExtra("fromToZzim", false)

        shopDetailViewModel.shopDetailData.observe(this, Observer { datas ->
            if(datas != null) {
                orderDates = datas.orderAvailabilityDates
                zzim = datas.zzim
                btn_shop_detail_zzim.isSelected = zzim

                tv_shop_detail_zzim_cnt.text = datas.zzimCount.toString()

                shopChannel = datas.shopChannel
                if(shopChannel.contains("http://pf.kakao.com/")) {
                    shopChannel = shopChannel.substring(shopChannel.indexOf("http://pf.kakao.com/") + 20)
                } else if(shopChannel.contains("https://pf.kakao.com/")) {
                    shopChannel = shopChannel.substring(shopChannel.indexOf("https://pf.kakao.com/") + 21)
                }
                var sizeDataAll : String = ""
                val dec = DecimalFormat("#,###")
                for (i in datas.sizes.indices)
                {
                    sizeDataAll += datas.sizes[i].name
                    if(datas.sizes[i].size.isNotEmpty()) sizeDataAll += "(${datas.sizes[i].size}cm)"
                    sizeDataAll += " / ${dec.format(datas.sizes[i].price)}원"
                    if(i < datas.sizes.size-1) sizeDataAll += "\n"
                }
                tv_cake_detail_size_price_contents.text = sizeDataAll
            }
            else {
                Log.d("nulkong", "get shopDetail size == 0")
            }
        })

        shopDetailViewModel.zzim.observe(this, Observer { datas ->
            Log.d("songjem", "observe, zzim = " + datas)
            if(datas != null) {
                zzim = datas
                if(zzim) {
                    btn_shop_detail_zzim.isSelected = true
                    tv_shop_detail_zzim_cnt.text = (Integer.parseInt(tv_shop_detail_zzim_cnt.text.toString()) + 1).toString()
                }
                else {
                    btn_shop_detail_zzim.isSelected = false
                    tv_shop_detail_zzim_cnt.text = (Integer.parseInt(tv_shop_detail_zzim_cnt.text.toString()) - 1).toString()
                }
            }
            else {
                Log.d("songjem", "zzim shop error")
            }
        })

        btn_shop_detail_zzim.setOnClickListener {
            shopDetailViewModel.clickZzimBtn(authorization, shopId, zzim)
        }

        rl_shop_detail_connect.setOnClickListener {
            Log.d("songjem", "shopChannel = " + shopChannel)
            // 카카오톡 채널 채팅 URL
            val url = TalkApiClient.instance.channelChatUrl(shopChannel)
            // CustomTabs 로 열기
            KakaoCustomTabsClient.openWithDefault(this, url)
        }

        btn_shop_detail_back.setOnClickListener {
            if(fromToZzim) {
                setResult(RESULT_OK, intent);
                finish()
            }
            else super.onBackPressed()
        }

        btn_shop_detail_order_date.setOnClickListener{
            val intent = Intent(applicationContext, CalendarActivity::class.java)
            intent.putExtra("dates", orderDates)
            startActivity(intent)
        }

        setTabLayout()
        sendShopIdToServer()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_shop_detail
    }

    override fun getViewModel(): ShopDetailViewModel {
        // 뷰페이저 어댑터 연결
        binding.vpShopDetailViewpager.adapter = ShopActivity@adapter

        // 탭 레이아웃에 뷰페이저 연결
        binding.tlShopDetailTabLayout.setupWithViewPager(binding.vpShopDetailViewpager)

        shopDetailViewModel = ViewModelProvider(this, ShopDetailViewModel.Factory(application, supportFragmentManager, TabLayout.ViewPagerOnTabSelectedListener(binding.vpShopDetailViewpager)
        , TabLayout.TabLayoutOnPageChangeListener(binding.tlShopDetailTabLayout)
        )).get(ShopDetailViewModel::class.java)

        return shopDetailViewModel
    }

    fun setTabLayout()
    {
        binding.tlShopDetailTabLayout.getTabAt(0)?.setText(R.string.shop_design_fragment_name)
        binding.tlShopDetailTabLayout.getTabAt(1)?.setText(R.string.shop_inform_fragment_name)
    }

    override fun onBackPressed() {
        if(fromToZzim) {
            setResult(RESULT_OK, intent);
            finish()
        }
        else super.onBackPressed()
    }

    fun sendShopIdToServer()
    {
        shopId = intent.extras!!.getInt("cakeShopId")

        shopDetailViewModel.sendShopIdForShopDetail(shopId)
    }
}