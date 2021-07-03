package com.cakeit.cakitandroid.presentation.main

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.cakeit.cakitandroid.base.BaseViewModel

class MainViewModel(application: Application) : BaseViewModel<Any?>(application) {

    val isRegistered = MutableLiveData<Boolean>() // 회원가입이 되어있는가?

    fun checkIsRegistered(){
        //for testing social login
        //추후에는 pref등에서 token 있는지 체크
        isRegistered.value = false
    }
}