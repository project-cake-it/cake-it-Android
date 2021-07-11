package com.cakeit.cakitandroid.presentation.design

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseActivity
import com.cakeit.cakitandroid.databinding.ActivityDesignDetailBinding
import kotlinx.android.synthetic.main.activity_design_detail.*
import kotlinx.android.synthetic.main.activity_design_detail.tv_cake_detail_size_price_contents
import kotlin.properties.Delegates

class DesignDetailActivity : BaseActivity<ActivityDesignDetailBinding, DesignDetailViewModel>() {

    private lateinit var binding : ActivityDesignDetailBinding
    private lateinit var designDetailViewModel: DesignDetailViewModel

    var designId by Delegates.notNull<Int>()
    lateinit var designPagerAdapter : DesignPagerAdapter

    companion object {
        const val TAG: String = "DesignDetailActivityTAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_design_detail)

        dataBinding()
        binding = getViewDataBinding()
        binding.vm = getViewModel()

        designDetailViewModel.designDetailData.observe(this, Observer { datas ->
            if(datas != null)
            {
                var data = ArrayList<String>()
                for(image in datas.designImages)
                {
                    data.add(image.designImageUrl)
                }

                designPagerAdapter = DesignPagerAdapter(applicationContext,  data)
                vp_cake_detail_img.adapter = designPagerAdapter

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
        designId = 1;

        designDetailViewModel.sendDesignIdForDesignDetail(designId)
    }

}