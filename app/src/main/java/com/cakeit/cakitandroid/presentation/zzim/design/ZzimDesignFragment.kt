package com.cakeit.cakitandroid.presentation.zzim.design

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseFragment
import com.cakeit.cakitandroid.databinding.FragmentZzimDesignBinding
import com.cakeit.cakitandroid.presentation.shop.design.DesignGridAdapter
import com.cakeit.cakitandroid.presentation.zzim.ZzimViewModel
import kotlinx.android.synthetic.main.fragment_zzim_design.*
import kotlinx.android.synthetic.main.fragment_zzim_design.view.*

class ZzimDesignFragment : BaseFragment<FragmentZzimDesignBinding, ZzimViewModel>(), View.OnClickListener {

    lateinit var binding : FragmentZzimDesignBinding
    lateinit var zzimViewModel : ZzimViewModel

    lateinit var designGridAdapter: DesignGridAdapter

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

    override fun onClick(view: View?) {
        var index = rv_zzim_design_item.getChildAdapterPosition(view!!)
        Log.d(TAG, "pressed $index design")

    }

    fun initRecycler(v: View) {
        designGridAdapter = DesignGridAdapter(context!!)
        designGridAdapter.setOnItemClickListener(this)

        v.rv_zzim_design_item.adapter = designGridAdapter
        v.rv_zzim_design_item.layoutManager = GridLayoutManager(context, 2)
    }

    fun getZzimDesigns()
    {
        zzimViewModel.getZzimDesign()
    }

}