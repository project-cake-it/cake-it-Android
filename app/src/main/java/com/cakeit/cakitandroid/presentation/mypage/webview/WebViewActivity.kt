package com.cakeit.cakitandroid.presentation.mypage.webview

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.cakeit.cakitandroid.R

class WebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        val webView = findViewById<WebView>(R.id.wv_webview)
        val url = intent.getStringExtra("fileUrl")!!
        webView.webViewClient = WebViewClient()
        webView.loadUrl(url)
    }

    fun getLayoutId(): Int {
        return R.layout.activity_webview
    }
}