package com.cakeit.cakitandroid.presentation.mypage

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager.widget.ViewPager
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseFragment
import com.cakeit.cakitandroid.databinding.FragmentMypageBinding
import com.cakeit.cakitandroid.presentation.design.DesignDetailActivity
import com.cakeit.cakitandroid.presentation.list.designlist.DesignListActivity
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_home_promotion.*
import kotlin.properties.Delegates

class MyPageFragment : BaseFragment<FragmentMypageBinding, MyPageViewModel>(), View.OnClickListener {

    private lateinit var binding : FragmentMypageBinding
    private lateinit var myPageViewModel: MyPageViewModel

    var designId by Delegates.notNull<Int>()
    var promotionDesignId = ArrayList<Int>()
    var popularDesignId = ArrayList<Int>()

    companion object {
        const val TAG: String = "FragmentTabTAG"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = getViewDataBinding()
        binding.viewModel = getViewModel()
    }
    override fun getLayoutId(): Int {
        return R.layout.fragment_mypage
    }

    override fun getViewModel(): MyPageViewModel {
        myPageViewModel = ViewModelProvider(this).get(MyPageViewModel::class.java)
        return myPageViewModel
    }

    override fun onClick(v: View?) {
        Log.d("TEST","onClick ${v!!.id}")
        when(v!!.id)
        {

//            R.id.tv_home_hide_theme -> {
//                tv_home_hide_theme.visibility = View.GONE
//                rl_home_company1_theme.visibility = View.GONE
//                rl_home_view_more.visibility = View.VISIBLE
//                ll_home_third_theme_line.visibility = View.GONE
//                ll_home_fourth_theme_line.visibility = View.GONE
//            }
//
//            R.id.rl_home_view_more -> {
//                tv_home_hide_theme.visibility = View.VISIBLE
//                rl_home_company1_theme.visibility = View.VISIBLE
//                rl_home_view_more.visibility = View.GONE
//                ll_home_third_theme_line.visibility = View.VISIBLE
//                ll_home_fourth_theme_line.visibility = View.VISIBLE
//            }
//
//            R.id.rl_home_theme_birthday -> {
//                var intent = Intent(context, DesignListActivity::class.java)
//                intent.putExtra("theme", "BIRTHDAY")
//                startActivity(intent)
//            }
//
//            R.id.rl_home_theme_anniv -> {
//                var intent = Intent(context, DesignListActivity::class.java)
//                intent.putExtra("theme", "ANNIVERSARY")
//                startActivity(intent)
//            }
//
//            R.id.rl_home_theme_wedding -> {
//                var intent = Intent(context, DesignListActivity::class.java)
//                intent.putExtra("theme", "WEDDING")
//                startActivity(intent)
//            }
//
//            R.id.rl_home_company1_theme -> {
//                var intent = Intent(context, DesignListActivity::class.java)
//                intent.putExtra("theme", "EMPLOYMENT")
//                startActivity(intent)
//            }
//
//            R.id.rl_home_theme_retire -> {
//                var intent = Intent(context, DesignListActivity::class.java)
//                intent.putExtra("theme", "LEAVE")
//                startActivity(intent)
//            }
//
//            R.id.rl_home_theme_discharge -> {
//                var intent = Intent(context, DesignListActivity::class.java)
//                intent.putExtra("theme", "DISCHARGE")
//                startActivity(intent)
//            }
//
//            R.id.rl_home_theme_club -> {
//                var intent = Intent(context, DesignListActivity::class.java)
//                intent.putExtra("theme", "GRADUATED")
//                startActivity(intent)
//            }
//
//            R.id.rl_home_theme_etc -> {
//                var intent = Intent(context, DesignListActivity::class.java)
//                intent.putExtra("theme", "NONE")
//                startActivity(intent)
//            }
//
//            else -> {
//                var position: Int = rv_home_cake_list.getChildAdapterPosition(v)
//                val intent = Intent(context, DesignDetailActivity::class.java)
//                intent.putExtra("designId", popularDesignId[position])
//                startActivity(intent)
//            }
        }
    }
}