package com.cakeit.cakitandroid.presentation.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseActivity
import com.cakeit.cakitandroid.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient


class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    private val REQUEST_CODE_GOOGLE_LOGIN = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding()
        binding = getViewDataBinding()
        binding.viewModel = getViewModel()

        binding.lifecycleOwner?.let {
            binding.viewModel?.registerState?.observe(it, Observer{ it ->
                //filtering reset :: is this necessary?
                val message = binding.viewModel?.registerMessage!!
                if(it || message != "Reset livedata"){
                    showToast(message, true)
                }

//                TODO("미/완료시 logic 짜기")
            })
        }

        //카카오 로그인 버튼
        findViewById<Button>(R.id.btn_login_kakaoLogin).setOnClickListener {
            val kakaoClientInstance = UserApiClient.instance
            if (kakaoClientInstance.isKakaoTalkLoginAvailable(this)) {
                kakaoClientInstance.loginWithKakaoTalk(this, callback = kakaoLoginCallback)
            } else {
                kakaoClientInstance.loginWithKakaoAccount(this, callback = kakaoLoginCallback)
            }
        }

        //네이버 로그인 버튼
        findViewById<Button>(R.id.btn_login_naverLogin).setOnClickListener {

        }

        //구글 로그인 버튼
        findViewById<Button>(R.id.btn_login_googleLogin).setOnClickListener {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.AUTHCODE_GOOGLE_CLIENTID))
                .requestEmail()
                .build()

            val googleSignInClient = GoogleSignIn.getClient(this, gso)

            val signInIntent: Intent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, REQUEST_CODE_GOOGLE_LOGIN)
        }

        //애플 로그인 버튼

        findViewById<Button>(R.id.btn_login_appleLogin).setOnClickListener {

        }

    }

    val kakaoLoginCallback: (OAuthToken?, Throwable?) -> Unit = { token, e ->
        if (e != null) {
            Log.d(TAG, "Kakao signin failed : ${e.message}")
            showToast("Kakao signin failed : ${e.message}")
        }
        else if (token != null) {
            //Login Success
            binding.viewModel?.sendKakaoCodeToServer(token)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == REQUEST_CODE_GOOGLE_LOGIN) {
            // The Task returned from this call is always completed, no need to attach a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Signed in successfully, show authenticated UI.
                binding.viewModel?.sendGoogleCodeToServer(task.result)
            } catch (e: Exception) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                Log.d(TAG, "Google signin failed : ${e.message}")
                showToast("Google signin failed : ${e.message}")
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun getViewModel(): LoginViewModel {
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        return loginViewModel
    }
}