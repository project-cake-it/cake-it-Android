package com.cakeit.cakitandroid.presentation.shop

import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import android.util.Log
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.cakeit.cakitandroid.presentation.shop.design.ShopDesignFragment
import com.cakeit.cakitandroid.presentation.shop.inform.ShopInformFragment

class ContentsPagerAdapter(fm: FragmentManager, num : Int) :
    FragmentStatePagerAdapter(fm) {
    var num = num

    override fun getItem(position: Int): Fragment {
        Log.d("song", "getItem = " + position)
        if(position == 0) return ShopDesignFragment()
        else if(position == 1) return ShopInformFragment()
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