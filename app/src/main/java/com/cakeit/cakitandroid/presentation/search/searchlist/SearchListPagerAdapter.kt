package com.cakeit.cakitandroid.presentation.search.searchlist

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.cakeit.cakitandroid.presentation.search.searchlist.design.SearchDesignFragment
import com.cakeit.cakitandroid.presentation.search.searchlist.shop.SearchShopFragment

class SearchListPagerAdapter(fm: FragmentManager, num : Int, keyword : String) :
    FragmentStatePagerAdapter(fm) {
    var num = num
    var searchKeyword = keyword

    override fun getItem(position: Int): Fragment {
        if(position == 0) {
            var bundle = Bundle()
            var searchDesignFragment = SearchDesignFragment()
            bundle.putString("keyword", searchKeyword)
            searchDesignFragment.arguments = bundle
            return searchDesignFragment
        }
        else if(position == 1) {
            var bundle = Bundle()
            var searchShopFragment = SearchShopFragment()
            bundle.putString("keyword", searchKeyword)
            searchShopFragment.arguments = bundle
            return searchShopFragment
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