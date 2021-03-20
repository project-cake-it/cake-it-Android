package com.cakeit.cakitandroid.presentation.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseActivity
import com.cakeit.cakitandroid.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlin.coroutines.CoroutineContext


class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    private val REQUEST_CODE_GOOGLE_LOGIN = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding()
        binding = getViewDataBinding()
        binding.viewModel = getViewModel()


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

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "requestCode$requestCode")
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == REQUEST_CODE_GOOGLE_LOGIN) {
            // The Task returned from this call is always completed, no need to attach a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Signed in successfully, show authenticated UI.
                val account: GoogleSignInAccount? = task.result
                val authCode = account?.idToken
                GlobalScope.async {
                    Log.d(TAG, "authCode $authCode")
                    binding.viewModel?.sendAuthCodeToServer(authCode!!, "GOOGLE")
                }
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