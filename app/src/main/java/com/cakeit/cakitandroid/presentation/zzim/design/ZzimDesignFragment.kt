package com.cakeit.cakitandroid.presentation.zzim.design

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseFragment
import com.cakeit.cakitandroid.data.source.local.prefs.SharedPreferenceController
import com.cakeit.cakitandroid.databinding.FragmentZzimDesignBinding
import com.cakeit.cakitandroid.presentation.design.DesignDetailActivity
import com.cakeit.cakitandroid.presentation.home.CakeListDeco
import com.cakeit.cakitandroid.presentation.shop.design.DesignGridAdapter
import com.cakeit.cakitandroid.presentation.zzim.ZzimViewModel
import com.cakeit.cakitandroid.presentation.zzim.shop.ZzimShopFragment
import kotlinx.android.synthetic.main.fragment_zzim_design.view.*

class ZzimDesignFragment : BaseFragment<FragmentZzimDesignBinding, ZzimViewModel>() {

    lateinit var binding : FragmentZzimDesignBinding
    lateinit var zzimViewModel : ZzimViewModel

    lateinit var designGridAdapter: DesignGridAdapter

    private lateinit var authorization : String
    val REQUEST_CODE = 1

    var designId = ArrayList<Int>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = getViewDataBinding()
        binding.vm = getViewModel()

        authorization = SharedPreferenceController.getToken(context!!)
        initRecycler(view)

        zzimDesignFragment = this
        getZzimDesigns()

        zzimViewModel.desigDatas.observe(viewLifecycleOwner, Observer { datas ->
            if(datas != null)
            {
                var designs = ArrayList<String>()
                designId = ArrayList()

                for(data in datas)
                {
                    // 프롬마틸다 디자인 임시 제거
                    if(data.id == 1) continue

                    designs.add(data.displayImage)
                    designId.add(data.id)
                }
                designGridAdapter.setRefresh(designs)
            }
            else {
                Log.d("nulkong", "get zzim designs size == 0")
            }
        })
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_zzim_design
    }

    override fun getViewModel(): ZzimViewModel {
        zzimViewModel = ViewModelProvider(parentFragment!!).get(ZzimViewModel::class.java)
        return zzimViewModel
    }

    fun initRecycler(v: View) {
        designGridAdapter = DesignGridAdapter(context!!)
        designGridAdapter.setOnItemClickListener(object : DesignGridAdapter.OnItemClickListener{
            override fun OnClick(view: View, position: Int) {
                val intent = Intent(context, DesignDetailActivity::class.java)
                intent.putExtra("designId", designId[position])
                intent.putExtra("fromToZzim", true)
                startActivityForResult(intent, REQUEST_CODE)
            }
        })

        v.rv_zzim_design_item.adapter = designGridAdapter
        v.rv_zzim_design_item.addItemDecoration(CakeListDeco(context!!, "designImage"))
        v.rv_zzim_design_item.layoutManager = GridLayoutManager(context, 2)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            getZzimDesigns()
        }
    }

    fun getZzimDesigns()
    {
        zzimViewModel.getZzimDesign(authorization)
    }

    companion object {
        var zzimDesignFragment : ZzimDesignFragment? = null
    }
}