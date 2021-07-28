package com.cakeit.cakitandroid.presentation.mypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseFragment
import com.cakeit.cakitandroid.databinding.FragmentMypageBinding
import com.cakeit.cakitandroid.presentation.mypage.announcement.AnnouncementListActivity
import kotlinx.android.synthetic.main.fragment_mypage.view.*

class MyPageFragment : BaseFragment<FragmentMypageBinding, MyPageViewModel>(), View.OnClickListener {

    private lateinit var binding : FragmentMypageBinding
    private lateinit var myPageViewModel: MyPageViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = getViewDataBinding()
        binding.viewModel = getViewModel()

        // Set onClickListener to buttons
        view.btn_mypage_announcement.setOnClickListener(this)
        view.btn_mypage_qna.setOnClickListener(this)
        view.btn_mypage_terms_of_service.setOnClickListener(this)
        view.btn_mypage_terms_of_private_info.setOnClickListener(this)
        view.btn_mypage_opensource_license.setOnClickListener(this)
        view.btn_mypage_version_info.setOnClickListener(this)


    }
    override fun getLayoutId(): Int {
        return R.layout.fragment_mypage
    }

    override fun getViewModel(): MyPageViewModel {
        myPageViewModel = ViewModelProvider(this).get(MyPageViewModel::class.java)
        return myPageViewModel
    }

    override fun onClick(v: View?) {
        Log.d(TAG, v!!.id.toString())
        when(v!!.id)
        {
            R.id.btn_mypage_announcement -> {
                Log.d(TAG, "announcement")
                var intent = Intent(context, AnnouncementListActivity::class.java)
                startActivity(intent)
            }

            R.id.btn_mypage_qna -> {
                Log.d(TAG, "announcement")
                var intent = Intent(context, AnnouncementListActivity::class.java)
                startActivity(intent)
            }

            R.id.btn_mypage_terms_of_service -> {
                Log.d(TAG, "announcement")
                var intent = Intent(context, AnnouncementListActivity::class.java)
                startActivity(intent)
            }

            R.id.btn_mypage_terms_of_private_info -> {
                Log.d(TAG, "announcement")
                var intent = Intent(context, AnnouncementListActivity::class.java)
                startActivity(intent)
            }

            R.id.btn_mypage_opensource_license -> {
                Log.d(TAG, "announcement")
                var intent = Intent(context, AnnouncementListActivity::class.java)
                startActivity(intent)
            }

            R.id.btn_mypage_version_info -> {
                Log.d(TAG, "announcement")
                var intent = Intent(context, AnnouncementListActivity::class.java)
                startActivity(intent)
            }

            else -> {
//                var position: Int = rv_home_cake_list.getChildAdapterPosition(v)
//                val intent = Intent(context, DesignDetailActivity::class.java)
//                intent.putExtra("designId", popularDesignId[position])
//                startActivity(intent)
            }
        }
    }
}