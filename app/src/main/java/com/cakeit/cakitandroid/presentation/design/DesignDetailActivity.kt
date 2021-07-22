package com.cakeit.cakitandroid.presentation.design

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseActivity
import com.cakeit.cakitandroid.databinding.ActivityDesignDetailBinding
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

        designDetailViewModel.zzim.observe(this, Observer { datas ->
            Log.d("songjem", "observe, zzim = " + datas)
            if(datas != null) {
                zzim = datas
                if(datas) btn_cake_detail_zzim.isSelected = true
                else btn_cake_detail_zzim.isSelected = false
            }
            else {
                Log.d("songjem", "design zzim error")
            }
        })

        designDetailViewModel.designDetailData.observe(this, Observer { datas ->
            if(datas != null)
            {
                zzim = datas.zzim
                Log.d("songjem", "가게 디자인, zzim = " + zzim)
                if(zzim) btn_cake_detail_zzim.isSelected = true
                else btn_cake_detail_zzim.isSelected = false

                var data = ArrayList<String>()
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

        designDetailViewModel.sendDesignIdForDesignDetail(designId)
    }

}