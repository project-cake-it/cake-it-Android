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
import com.cakeit.cakitandroid.presentation.mypage.textboard.TextboardActivity
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

    fun startTextBoardActivityWithData(boardTitle : String, body : String){
        var intent = Intent(context, TextboardActivity::class.java)

        intent.putExtra("boardTitle", boardTitle)
        intent.putExtra("preload", true)
        intent.putExtra("body", body)
        startActivity(intent)
    }

    override fun onClick(v: View?) {
        Log.d(TAG, v!!.id.toString())
        when(v!!.id)
        {
            R.id.btn_mypage_announcement -> {
                var intent = Intent(context, AnnouncementListActivity::class.java)
                startActivity(intent)
            }

            R.id.btn_mypage_qna -> {
                startTextBoardActivityWithData(getString(R.string.mypage_qna), getString(R.string.mypage_qna))
            }

            R.id.btn_mypage_terms_of_service -> {
                startTextBoardActivityWithData(getString(R.string.mypage_terms_of_service), getString(R.string.mypage_terms_of_service))
            }

            R.id.btn_mypage_terms_of_private_info -> {
                startTextBoardActivityWithData(getString(R.string.mypage_terms_of_private_info), getString(R.string.mypage_terms_of_private_info))
            }

            R.id.btn_mypage_opensource_license -> {
                startTextBoardActivityWithData(getString(R.string.mypage_opensource_license), getString(R.string.mypage_opensource_license))
            }

            R.id.btn_mypage_version_info -> {
                startTextBoardActivityWithData(getString(R.string.mypage_version_info), getString(R.string.mypage_version_info))
            }
        }
    }
}