package com.cakeit.cakitandroid.presentation.zzim.design

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseFragment
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

    var designId = ArrayList<Int>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = getViewDataBinding()
        binding.vm = getViewModel()

        initRecycler(view)

        getZzimDesigns()

        zzimViewModel.desigDatas.observe(viewLifecycleOwner, Observer { datas ->
            if(datas != null)
            {
                var designs = ArrayList<String>()

                for(data in datas)
                {
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
                startActivity(intent)
            }
        })

        v.rv_zzim_design_item.adapter = designGridAdapter
        v.rv_zzim_design_item.addItemDecoration(CakeListDeco(context!!, "zzim"))
        v.rv_zzim_design_item.layoutManager = GridLayoutManager(context, 2)
    }

    fun getZzimDesigns()
    {
        zzimViewModel.getZzimDesign()
    }

}