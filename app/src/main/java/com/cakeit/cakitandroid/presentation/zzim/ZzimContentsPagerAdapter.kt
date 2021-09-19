package com.cakeit.cakitandroid.presentation.zzim

import android.util.Log
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.cakeit.cakitandroid.presentation.zzim.design.ZzimDesignFragment
import com.cakeit.cakitandroid.presentation.zzim.shop.ZzimShopFragment

class ZzimContentsPagerAdapter(fm: FragmentManager, num : Int) :
    FragmentStatePagerAdapter(fm) {
    var num = num

    override fun getItem(position: Int): Fragment {
        if(position == 0) {
            return ZzimDesignFragment()
        }
        else if(position == 1) {
            return ZzimShopFragment()
        }
        else return null!!
    }

    override fun getCount(): Int {
        return num
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return null
    }
}