package com.cakeit.cakitandroid.presentation.zzim.design

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.PorterDuff
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
import kotlinx.android.synthetic.main.fragment_zzim_design.*
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
        Log.d("songjem", "ZzimDesignFragment onViewCreated")

        binding = getViewDataBinding()
        binding.vm = getViewModel()

        showLoadingBar()
        authorization = SharedPreferenceController.getAccessToken(context!!)
        initRecycler(view)

        zzimDesignFragment = this
        getZzimDesigns()

        zzimViewModel.desigDatas.observe(viewLifecycleOwner, Observer { datas ->
            hideLoadingBar()
            if(datas != null) {
                if(datas.size > 0) {
                    rv_zzim_design_item.visibility = View.VISIBLE
                    tv_zzim_design_empty.visibility = View.GONE

                    var designs = ArrayList<String>()
                    designId = ArrayList()

                    for(data in datas) {
                        designs.add(data.displayImage)
                        designId.add(data.id)
                    }
                    designGridAdapter.setRefresh(designs)
                } else {
                    rv_zzim_design_item.visibility = View.GONE
                    tv_zzim_design_empty.visibility = View.VISIBLE
                }
            }
            else {
                Log.d("nulkong", "get zzim design list, ERROR")
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
        v.rv_zzim_design_item.addItemDecoration(CakeListDeco(context!!, "zzim"))
        v.rv_zzim_design_item.layoutManager = GridLayoutManager(context, 2)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            showLoadingBar()
            getZzimDesigns()
        }
    }

    fun getZzimDesigns()
    {
        zzimViewModel.getZzimDesign(authorization)
    }

    fun showLoadingBar() {
        val c = resources.getColor(R.color.colorPrimary)
        pb_loading_zzim_design.setIndeterminate(true)
        pb_loading_zzim_design.getIndeterminateDrawable().setColorFilter(c, PorterDuff.Mode.MULTIPLY)
        pb_loading_zzim_design.visibility = View.VISIBLE
    }

    fun hideLoadingBar() {
        pb_loading_zzim_design.visibility = View.GONE
    }

    companion object {
        var zzimDesignFragment : ZzimDesignFragment? = null
    }
}