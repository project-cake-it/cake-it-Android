package com.cakeit.cakitandroid.presentation.main

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseActivity
import com.cakeit.cakitandroid.databinding.ActivityMainBinding
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import com.cakeit.cakitandroid.presentation.login.LoginActivity

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding()
        binding = getViewDataBinding()
        binding.viewModel = getViewModel()
        getHashKey()
        //MainActivity 진입시 로그인 되어있는지 확인후 분기
        binding.lifecycleOwner?.let {
            binding.viewModel?.isRegistered?.observe(it, Observer{ isLoggedIn ->
                if(!isLoggedIn){
                    val appContext = this
                    val loginActivityIntent = Intent(appContext, LoginActivity::class.java)
                    appContext.startActivity(loginActivityIntent)
                    finish()
                }
            })
        }
        binding.viewModel?.checkIsRegistered()
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun getViewModel(): MainViewModel {
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
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
}