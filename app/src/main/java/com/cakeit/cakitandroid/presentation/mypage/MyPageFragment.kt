package com.cakeit.cakitandroid.presentation.mypage

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseFragment
import com.cakeit.cakitandroid.data.source.local.prefs.SharedPreferenceController
import com.cakeit.cakitandroid.databinding.FragmentMypageBinding
import com.cakeit.cakitandroid.presentation.login.LoginActivity
import com.cakeit.cakitandroid.presentation.login.LoginViewModel
import com.cakeit.cakitandroid.presentation.mypage.announcement.AnnouncementListActivity
import com.cakeit.cakitandroid.presentation.mypage.textboard.TextboardActivity
import com.cakeit.cakitandroid.presentation.mypage.webview.WebViewActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.OnCompleteListener
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.talk.TalkApiClient
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.fragment_mypage.view.*


class MyPageFragment : BaseFragment<FragmentMypageBinding, MyPageViewModel>(), View.OnClickListener {

    private lateinit var binding : FragmentMypageBinding
    private lateinit var myPageViewModel: MyPageViewModel

    private lateinit var btn_mypage_loginout : Button

    private var accessToken : String = ""
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

        accessToken = SharedPreferenceController?.getAccessToken(context!!)

        view.btn_mypage_loginout.text = if (accessToken.equals("")) {
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
                if (accessToken == ""){
                    // 로그인
                    var intent = Intent(context, LoginActivity::class.java)
                    intent.putExtra("fromToScreen", "MyPageFragment")
                    startActivity(intent)
                } else {
                    val msgBuilder: AlertDialog.Builder = AlertDialog.Builder(context!!)
                        .setTitle("로그아웃")
                        .setMessage("정말로 로그아웃 하시겠습니까?")
                        .setPositiveButton("확인",
                            DialogInterface.OnClickListener { dialog, id ->
                                var socialType = SharedPreferenceController.getSocialType(context!!)

                                Log.e("SUNGMIN", "$socialType")
                                when (socialType) {
                                    "GOOGLE" -> {
                                        LoginActivity.googleSignInClient.signOut()
                                            .addOnCompleteListener {
                                                logoutCallback()
                                            }
                                    }
                                    "KAKAO" -> {
                                        Log.e("SUNGMIN", "KAKAO LOGOUT")
                                        UserApiClient.instance.logout { error ->
                                            if (error != null) {
                                                Log.e(TAG, "Kakao Logout Failed.", error)
                                            } else {
                                                logoutCallback()
                                                Log.e("SUNGMIN", "KAKAO LOGOUT")
                                            }
                                        }
                                    }
                                    "NAVER" -> {
                                        LoginActivity.mOAuthLoginModule.logout(context)
                                        logoutCallback()
                                    }
                                    else -> {
                                        Log.e(TAG, "wrong socialLogin Type $socialType")
                                    }
                                }
                            })
                        .setNegativeButton("취소",null)
                    val msgDlg : AlertDialog = msgBuilder.create()
                    msgDlg.show()

                }
            }
        }
    }

    fun logoutCallback(){
        Toast.makeText(context, "로그아웃에 성공하였습니다", Toast.LENGTH_LONG).show()
        SharedPreferenceController.setAccessToken(context!!, "")
        SharedPreferenceController.setSocialType(context!!, "")

        accessToken = SharedPreferenceController.getAccessToken(context!!)
        binding.btnMypageLoginout.text = "로그인"

    }
}