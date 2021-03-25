package com.cakeit.cakitandroid.di.applicationModule;

import android.app.Application
import com.cakeit.cakitandroid.R
import com.kakao.sdk.common.KakaoSdk

/*
 * Application을 상속 받은 곳 : 시스템이 처음 켜질 때 접근하는 부분
 * */

class GlobalApplication : Application() {
        override fun onCreate() {
        super.onCreate()
        // 다른 초기화 코드들

        // Kakao SDK 초기화
        KakaoSdk.init(this, getString(R.string.AUTHCODE_KAKAO_NATIVEAPPKEY))
        }
}

