package com.cakeit.cakitandroid.presentation.main

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Process
import android.util.Base64
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseActivity
import com.cakeit.cakitandroid.data.source.local.prefs.SharedPreferenceController
import com.cakeit.cakitandroid.databinding.ActivityMainBinding
import com.cakeit.cakitandroid.presentation.login.LoginActivity
import com.google.android.material.tabs.TabLayout
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    private val adapter by lazy { MainPagerAdapter(supportFragmentManager, 5) }
    private lateinit var binding : ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding()
        binding = getViewDataBinding()
        binding.viewModel = getViewModel()
        mainActivity = MainActivity()

        setTabLayout()
//        getHashKey()
        //MainActivity 진입시 로그인 되어있는지 확인후 분기
//        binding.lifecycleOwner?.let {
//            binding.viewModel?.isRegistered?.observe(it, Observer{ isLoggedIn ->
//                if(!isLoggedIn){
//                    val appContext = this
//                    val loginActivityIntent = Intent(appContext, LoginActivity::class.java)
//                    appContext.startActivity(loginActivityIntent)
//                    finish()
//                }
//            })
//        }
//        binding.viewModel?.checkIsRegistered()

        var authorization = SharedPreferenceController.getAccessToken(applicationContext)
        if (authorization.equals("")) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("fromToScreen", "DesignDetailActivity")
            startActivity(intent)
        }
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun getViewModel(): MainViewModel {
        // 뷰페이저 어댑터 연결
        binding.vpMainViewpager.adapter = adapter
        binding.vpMainViewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tlMainTablayout))

        // 탭 레이아웃에 뷰페이저 연결
        binding.tlMainTablayout.setupWithViewPager(binding.vpMainViewpager)

        mainViewModel = ViewModelProvider(this
            , MainViewModel.Factory(application,supportFragmentManager
            , TabLayout.ViewPagerOnTabSelectedListener(binding.vpMainViewpager)
            , TabLayout.TabLayoutOnPageChangeListener(binding.tlMainTablayout)
            )).get(MainViewModel::class.java)
        return mainViewModel
    }

    fun getHashKey() {
        var packageInfo: PackageInfo? = null
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        if (packageInfo == null) Log.e("KeyHash", "KeyHash:null")
        for (signature in packageInfo!!.signatures) {
            try {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            } catch (e: NoSuchAlgorithmException) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=$signature", e)
            }
        }
    }

    @SuppressLint("InflateParams")
    fun setTabLayout()
    {
        //Tab 아이콘 설정
        binding.tlMainTablayout.getTabAt(0)?.setIcon(R.drawable.isclick_tab_home)
        binding.tlMainTablayout.getTabAt(1)?.setIcon(R.drawable.isclick_tab_search)
        binding.tlMainTablayout.getTabAt(2)?.setIcon(R.drawable.isclick_tab_store)
        binding.tlMainTablayout.getTabAt(3)?.setIcon(R.drawable.isclick_tab_mark)
        binding.tlMainTablayout.getTabAt(4)?.setIcon(R.drawable.isclick_tab_mypage)

        for (i in 0 until binding.tlMainTablayout.getTabCount()) {
            val tab: TabLayout.Tab = binding.tlMainTablayout.getTabAt(i)!!

            val customTabView = layoutInflater.inflate(R.layout.view_tab_icon_home, null)
            val txtTabName = customTabView.findViewById(R.id.tv_name_tab_icon) as TextView

            when(i)
            {
                0 -> txtTabName.setText("홈")
                1 -> txtTabName.setText("검색")
                2 -> txtTabName.setText("가게")
                3 -> txtTabName.setText("찜")
                4 -> txtTabName.setText("마이페이지")
            }

            tab.customView = customTabView
        }
    }

    fun showExitDialog() {
        val dialog = AlertDialog.Builder(this)
        dialog.setMessage("케이크잇을 종료할까요?")
        dialog.setPositiveButton(
            "확인"
        ) { dialogInterface, i ->
            moveTaskToBack(true)
            finish()
            Process.killProcess(Process.myPid())
        }
        dialog.setNegativeButton("취소", null)
        dialog.show()
    }

    override fun onBackPressed() {
        showExitDialog()
    }

    companion object {
        lateinit var mainActivity: MainActivity
    }
}