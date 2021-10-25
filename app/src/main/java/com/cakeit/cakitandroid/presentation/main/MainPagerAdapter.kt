package com.cakeit.cakitandroid.presentation.main

import android.util.Log
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.cakeit.cakitandroid.presentation.home.HomeFragment
import com.cakeit.cakitandroid.presentation.list.shoplist.ShopListFragment
import com.cakeit.cakitandroid.presentation.mypage.MyPageFragment
import com.cakeit.cakitandroid.presentation.search.SearchFragment
import com.cakeit.cakitandroid.presentation.zzim.ZzimFragment

class MainPagerAdapter(fm: FragmentManager, num : Int) :
    FragmentStatePagerAdapter(fm) {
    var num = num

    override fun getItem(position: Int): Fragment {
        if(position == 0) return HomeFragment()
        else if(position == 1) return SearchFragment()
        else if(position == 2) return ShopListFragment()
        else if(position == 3) {
            Log.d("songjem", "ZzimFragment Tab is clicked")
            return ZzimFragment()
        }
        else if(position == 4) return MyPageFragment()
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