package com.cakeit.cakitandroid.presentation.design

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Process
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseActivity
import com.cakeit.cakitandroid.data.source.local.prefs.SharedPreferenceController
import com.cakeit.cakitandroid.databinding.ActivityDesignDetailBinding
import com.cakeit.cakitandroid.presentation.login.LoginActivity
import com.cakeit.cakitandroid.presentation.shop.ShopDetailActivity
import com.cakeit.cakitandroid.presentation.shop.calendar.CalendarActivity
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.talk.TalkApiClient
import kotlinx.android.synthetic.main.activity_design_detail.*
import kotlinx.android.synthetic.main.activity_design_detail.tv_cake_detail_size_price_contents
import java.text.DecimalFormat
import kotlin.properties.Delegates

class DesignDetailActivity : BaseActivity<ActivityDesignDetailBinding, DesignDetailViewModel>() {

    private lateinit var binding : ActivityDesignDetailBinding
    private lateinit var designDetailViewModel: DesignDetailViewModel

    var designId by Delegates.notNull<Int>()
    lateinit var designPagerAdapter : DesignPagerAdapter

    private var orderDates = ArrayList<String>()
    private var shopId by Delegates.notNull<Int>()
    private var zzim : Boolean = false
    private var fromToZzim : Boolean = false
    private lateinit var shopChannel : String
    private lateinit var authorization : String

    companion object {
        const val TAG: String = "DesignDetailActivityTAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_design_detail)

        dataBinding()
        binding = getViewDataBinding()
        binding.vm = getViewModel()

        authorization = SharedPreferenceController.getAccessToken(applicationContext)
        fromToZzim = intent.getBooleanExtra("fromToZzim", false)

        btn_cake_detail_zzim.setOnClickListener {
            if (authorization.equals("")) {
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("fromToScreen", "DesignDetailActivity")
                startActivity(intent)
            } else {
                designDetailViewModel.clickZzimBtn(authorization, designId, zzim)
            }
        }

        btn_cake_detail_back.setOnClickListener {
            if(fromToZzim) {
                setResult(RESULT_OK, intent);
                finish()
            }
            else super.onBackPressed()
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
                shopId = datas.shopId
                orderDates = datas.orderAvailabilityDates

                if (authorization.equals("")) zzim = false
                else zzim = datas.zzim

                btn_cake_detail_zzim.isSelected = zzim

                shopChannel = datas.shopChannel
                if(shopChannel.contains("http://pf.kakao.com/")) {
                    shopChannel = shopChannel.substring(shopChannel.indexOf("http://pf.kakao.com/") + 20)
                } else if(shopChannel.contains("https://pf.kakao.com/")) {
                    shopChannel = shopChannel.substring(shopChannel.indexOf("https://pf.kakao.com/") + 21)
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
                var dec = DecimalFormat("#,###")
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
                Log.d("nulkong", "get designDetail size == 0")
            }
        })

        rl_cake_detail_connect.setOnClickListener {
            val msgBuilder:AlertDialog.Builder = AlertDialog.Builder(this)
                    .setTitle("케이크 가게와 주문 상담을 시작할까요?")
                    .setMessage("카카오톡 채널 앱으로 이동해요!")
                    .setPositiveButton("예",
                    DialogInterface.OnClickListener { dialog, id ->
                        // 카카오톡 채널 채팅 URL
                        val url = TalkApiClient.instance.channelChatUrl(shopChannel)
                        // CustomTabs 로 열기
                        try {
                            KakaoCustomTabsClient.openWithDefault(this, url)
                        } catch (e: UnsupportedOperationException){
                            Toast.makeText(this, "크롬 브라우저가 활성화 되어있지 않습니다.", Toast.LENGTH_SHORT).show()
                        }
                    })
                    .setNegativeButton("아니오",null)
            val msgDlg : AlertDialog = msgBuilder.create()
            msgDlg.show()
        }

        btn_cake_detail_order_date.setOnClickListener{
            var intent = Intent(applicationContext, CalendarActivity::class.java)
            intent.putExtra("dates", orderDates)
            startActivity(intent)
        }

        tv_shop_detail_name.setOnClickListener {
            val intent = Intent(applicationContext, ShopDetailActivity::class.java)
            intent.putExtra("cakeShopId", shopId)
            intent.putExtra("fromToZzim", false)
            startActivity(intent)
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

    override fun onBackPressed() {
        if(fromToZzim) {
            setResult(RESULT_OK, intent);
            finish()
        }
        else super.onBackPressed()
    }

    fun sendDesignIdToServer()
    {
        designId = intent.extras!!.getInt("designId")

        designDetailViewModel.sendDesignIdForDesignDetail(designId)
    }
}