package com.cakeit.cakitandroid.presentation.search.searchlist

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.cakeit.cakitandroid.presentation.search.searchlist.design.SearchDesignFragment
import com.cakeit.cakitandroid.presentation.search.searchlist.shop.SearchShopFragment

class SearchListPagerAdapter(fm: FragmentManager, num : Int) :
    FragmentStatePagerAdapter(fm) {
    var num = num

    override fun getItem(position: Int): Fragment {
        if(position == 0) return SearchDesignFragment()
        else if(position == 1) return SearchShopFragment()
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