package com.cakeit.cakitandroid.presentation.design

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseActivity
import com.cakeit.cakitandroid.databinding.ActivityDesignDetailBinding
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.talk.TalkApiClient
import kotlinx.android.synthetic.main.activity_design_detail.*
import kotlinx.android.synthetic.main.activity_design_detail.tv_cake_detail_size_price_contents
import kotlinx.android.synthetic.main.activity_shop_detail.*
import kotlin.properties.Delegates

class DesignDetailActivity : BaseActivity<ActivityDesignDetailBinding, DesignDetailViewModel>() {

    private lateinit var binding : ActivityDesignDetailBinding
    private lateinit var designDetailViewModel: DesignDetailViewModel

    var designId by Delegates.notNull<Int>()
    lateinit var designPagerAdapter : DesignPagerAdapter

    private var zzim : Boolean = false
    private lateinit var shopChannel : String

    companion object {
        const val TAG: String = "DesignDetailActivityTAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_design_detail)

        dataBinding()
        binding = getViewDataBinding()
        binding.vm = getViewModel()

        btn_cake_detail_zzim.setOnClickListener {
            designDetailViewModel.clickZzimBtn(designId, zzim)
        }

        btn_cake_detail_back.setOnClickListener {
            super.onBackPressed()
        }

        designDetailViewModel.zzim.observe(this, Observer { datas ->
            Log.d("songjem", "observe, zzim = " + datas)
            if(datas != null) {
                zzim = datas
                btn_cake_detail_zzim.isSelected = datas
            }
            else {
                Log.d("songjem", "design zzim error")
            }
        })

        designDetailViewModel.designDetailData.observe(this, Observer { datas ->
            if(datas != null)
            {
                zzim = datas.zzim
                btn_cake_detail_zzim.isSelected = zzim

                if(datas.shopChannel != null) {
                    shopChannel = datas.shopChannel
                    if(shopChannel.contains("http://pf.kakao.com/")) {
                        shopChannel = shopChannel.substring(shopChannel.indexOf("http://pf.kakao.com/") + 20)
                    } else if(shopChannel.contains("https://pf.kakao.com/")) {
                        shopChannel = shopChannel.substring(shopChannel.indexOf("https://pf.kakao.com/") + 21)
                    }
                }

                val data = ArrayList<String>()
                for(image in datas.designImages)
                {
                    data.add(image.designImageUrl)
                }

                pb_cake_detail_progress_bar.progress = 100/data.size
                designPagerAdapter = DesignPagerAdapter(applicationContext,  data)
                vp_cake_detail_img.adapter = designPagerAdapter
                vp_cake_detail_img.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
                    override fun onPageScrollStateChanged(state: Int) {
                    }

                    override fun onPageScrolled(
                        position: Int,
                        positionOffset: Float,
                        positionOffsetPixels: Int
                    ) {
                    }

                    override fun onPageSelected(position: Int) {
                        if(position == data.size-1) pb_cake_detail_progress_bar.progress = 100
                        else pb_cake_detail_progress_bar.progress = 100/data.size*(position+1)
                    }
                })

                var sizeDataAll : String = ""
                for (i in datas.sizes.indices)
                {
                    sizeDataAll += datas.sizes[i].name
                    if(datas.sizes[i].size.isNotEmpty()) sizeDataAll += "(${datas.sizes[i].size})"
                    sizeDataAll += " / ${datas.sizes[i].price}원"
                    if(i < datas.sizes.size-1) sizeDataAll += "\n"
                }
                tv_cake_detail_size_price_contents.text = sizeDataAll
            }
            else {
                Log.d("nulkong", "get designDetail size == 0")
            }
        })

        rl_cake_detail_connect.setOnClickListener {
            Log.d("songjem", "가게연결 클릭")
            if(shopChannel != null) {
                Log.d("songjem", "shopChannel = " + shopChannel)
                // 카카오톡 채널 채팅 URL
                val url = TalkApiClient.instance.channelChatUrl(shopChannel)
                // CustomTabs 로 열기
                KakaoCustomTabsClient.openWithDefault(this, url)
            }
        }

        sendDesignIdToServer()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_design_detail
    }

    override fun getViewModel(): DesignDetailViewModel {
        designDetailViewModel = ViewModelProvider(this).get(DesignDetailViewModel::class.java)
        return designDetailViewModel
    }

    fun sendDesignIdToServer()
    {
        designId = intent.extras!!.getInt("designId")
        //designId = 1

        designDetailViewModel.sendDesignIdForDesignDetail(designId)
    }

}