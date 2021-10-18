package com.cakeit.cakitandroid.presentation.mypage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseFragment
import com.cakeit.cakitandroid.databinding.FragmentMypageBinding
import com.cakeit.cakitandroid.presentation.login.LoginActivity
import com.cakeit.cakitandroid.presentation.mypage.announcement.AnnouncementListActivity
import com.cakeit.cakitandroid.presentation.mypage.textboard.TextboardActivity
import com.cakeit.cakitandroid.presentation.mypage.webview.WebViewActivity
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.talk.TalkApiClient
import kotlinx.android.synthetic.main.fragment_mypage.view.*


class MyPageFragment : BaseFragment<FragmentMypageBinding, MyPageViewModel>(), View.OnClickListener {

    private lateinit var binding : FragmentMypageBinding
    private lateinit var myPageViewModel: MyPageViewModel

    private var accessToken : String? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = getViewDataBinding()
        binding.viewModel = getViewModel()

        // Set onClickListener to buttons
        view.btn_mypage_announcement.setOnClickListener(this)
        view.btn_mypage_qna.setOnClickListener(this)
        view.btn_mypage_terms_of_service.setOnClickListener(this)
        view.btn_mypage_terms_of_private_info.setOnClickListener(this)
        view.btn_mypage_loginout.setOnClickListener(this)
        view.tv_mypage_version_info.setOnClickListener(this)

        accessToken = context?.getSharedPreferences("userAccount", Context.MODE_PRIVATE)
            ?.getString("accessToken", null)

        view.btn_mypage_loginout.text = if (accessToken == null) {
            "로그인"
        } else {
            "로그아웃"
        }

        // Set version name from package info
        // Not using MVVM since it's very static and lightweight job.
        val pInfo = context!!.packageManager.getPackageInfo(context!!.packageName, 0)
        view.tv_mypage_version_name.text = pInfo.versionName

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

    fun startWebViewActivityFromAssets(fileName : String){
        var intent = Intent(context, WebViewActivity::class.java)

        intent.putExtra("fileUrl", "file:///android_asset/${fileName}")
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
                val url = TalkApiClient.instance.channelChatUrl(getString(R.string.mypage_cakeit_pfchannel_id))
                KakaoCustomTabsClient.openWithDefault(context!!, url)
            }

            R.id.btn_mypage_terms_of_service -> {
              startWebViewActivityFromAssets("terms.html")
            }

            R.id.btn_mypage_terms_of_private_info -> {
                startWebViewActivityFromAssets("personalinfomation.html")
            }

            R.id.btn_mypage_loginout -> {
                if (accessToken == null){
                    // 로그인
                    var intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    // 로그아웃

                }
            }
        }
    }
}