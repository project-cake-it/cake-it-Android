package com.cakeit.cakitandroid.presentation.zzim

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseFragment
import com.cakeit.cakitandroid.data.source.local.prefs.SharedPreferenceController
import com.cakeit.cakitandroid.databinding.FragmentZzimBinding
import com.cakeit.cakitandroid.presentation.login.LoginActivity
import com.cakeit.cakitandroid.presentation.zzim.design.ZzimDesignFragment
import com.cakeit.cakitandroid.presentation.zzim.shop.ZzimShopFragment
import com.google.android.material.tabs.TabLayout

class ZzimFragment : BaseFragment<FragmentZzimBinding, ZzimViewModel>() {

    private lateinit var adapter : ZzimContentsPagerAdapter
    private lateinit var binding : FragmentZzimBinding
    private lateinit var zzimViewModel: ZzimViewModel
    var myIsVisibleToUser : Boolean = false

    companion object {
        const val TAG: String = "ZzimActivityTAG"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUserVisibleHint(myIsVisibleToUser)

        adapter = ZzimContentsPagerAdapter(childFragmentManager, 2)
        binding = getViewDataBinding()
        binding.vm = getViewModel()
        setTabLayout()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_zzim
    }

    override fun getViewModel(): ZzimViewModel {
        // 뷰페이저 어댑터 연결
        binding.vpZzimViewpager.adapter = adapter

        // 탭 레이아웃에 뷰페이저 연결
        binding.tlZzimTabLayout.setupWithViewPager(binding.vpZzimViewpager)

        zzimViewModel = ViewModelProvider(this, ZzimViewModel.Factory(
            activity!!.application, childFragmentManager, TabLayout.ViewPagerOnTabSelectedListener(binding.vpZzimViewpager)
            , TabLayout.TabLayoutOnPageChangeListener(binding.tlZzimTabLayout)
        )).get(ZzimViewModel::class.java)

        return zzimViewModel
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        myIsVisibleToUser = isVisibleToUser

        if (isVisibleToUser && getActivity()!=null) {
            if (ZzimShopFragment.zzimShopFragment != null && ZzimDesignFragment.zzimDesignFragment != null) {
                var accessToken : String? = SharedPreferenceController.getAccessToken(context!!)!!
                if (accessToken.equals("")) {
                    goToLoginAct()
                } else {
                    ZzimShopFragment.zzimShopFragment!!.getZzimShoplist()
                    ZzimDesignFragment.zzimDesignFragment!!.getZzimDesigns()
                }
            } else {
                goToLoginAct()
            }
        }
    }

    fun goToLoginAct() {
        Log.d("songjem", "here5")
        val intent = Intent(activity, LoginActivity::class.java)
        intent.putExtra("fromToScreen", "ZzimFragment")
        startActivity(intent)
    }

    fun setTabLayout()
    {
        binding.tlZzimTabLayout.getTabAt(0)?.setText(R.string.zzim_design_fragment_name)
        binding.tlZzimTabLayout.getTabAt(1)?.setText(R.string.zzim_shop_fragment_name)
    }
}