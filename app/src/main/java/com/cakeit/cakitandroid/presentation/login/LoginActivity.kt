package com.cakeit.cakitandroid.presentation.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.SpannedString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.TypefaceSpan
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.set
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseActivity
import com.cakeit.cakitandroid.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.nhn.android.naverlogin.OAuthLogin
import android.widget.Toast
import com.cakeit.cakitandroid.data.source.local.prefs.SharedPreferenceController
import com.cakeit.cakitandroid.presentation.main.MainActivity

import com.nhn.android.naverlogin.OAuthLoginHandler
import org.json.JSONException
import org.json.JSONObject


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
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    showToast(message, true)
                }

//                TODO("미/완료시 logic 짜기")
            })

            binding.viewModel?.accessToken?.observe(it, Observer{ token ->
                SharedPreferenceController.setToken(applicationContext, token)
            })
        }

        val welcomeText = findViewById<TextView>(R.id.tv_login_welcome).text as Spannable
        welcomeText.setSpan(ForegroundColorSpan(Color.parseColor("#df7373")), 11, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        welcomeText.setSpan(StyleSpan(ResourcesCompat.getFont(this, R.font.spoqa_han_sans_neo_bold)!!.style), 11, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

//        Log.e("ReleaseKeyHash",  Utility.getKeyHash(this))

        //카카오 로그인 버튼
        findViewById<Button>(R.id.btn_login_kakaoLogin).setOnClickListener {
            val kakaoClientInstance = UserApiClient.instance
            if (kakaoClientInstance.isKakaoTalkLoginAvailable(this)) {
                kakaoClientInstance.loginWithKakaoTalk(this, callback = kakaoLoginCallback)
            } else {
                kakaoClientInstance.loginWithKakaoAccount(this, callback = kakaoLoginCallback)
            }
        }

        val ctx = this
        //네이버 로그인 버튼
        findViewById<Button>(R.id.btn_login_naverLogin).setOnClickListener {
            val mOAuthLoginModule = OAuthLogin.getInstance();
            mOAuthLoginModule.init(
                ctx
                ,getString(R.string.AUTHCODE_NAVER_CLIENTID)
                ,getString(R.string.AUTHCODE_NAVER_SECRET)
                ,getString(R.string.app_name)

            );

            val naverLoginHandler: OAuthLoginHandler = object : OAuthLoginHandler() {
                override fun run(success: Boolean) {
                    if (success) {
                        val accessToken: String = mOAuthLoginModule.getAccessToken(ctx)
                        val refreshToken: String = mOAuthLoginModule.getRefreshToken(ctx)
                        val expiresAt: Long = mOAuthLoginModule.getExpiresAt(ctx)
                        val tokenType: String = mOAuthLoginModule.getTokenType(ctx)

                        binding.viewModel?.sendNaverCodeToServer(mOAuthLoginModule.getAccessToken(ctx))

                    } else {
                        val errorCode: String = mOAuthLoginModule.getLastErrorCode(ctx).code
                        val errorDesc: String = mOAuthLoginModule.getLastErrorDesc(ctx)
                        Toast.makeText(
                            ctx, "errorCode:" + errorCode
                                    + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            mOAuthLoginModule.startOauthLoginActivity(this, naverLoginHandler);


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

//class RequestApiTask(private val mContext: Context, private val mOAuthLoginModule: OAuthLogin) :
//    AsyncTask<Void?, Void?, String>() {
//    override fun onPreExecute() {}
//
//    override fun onPostExecute(content: String) {
//        try {
//            val loginResult = JSONObject(content)
//            if (loginResult.getString("resultcode") == "00") {
//                val response = loginResult.getJSONObject("response")
//                val id = response.getString("id")
//                val email = mOAuthLoginModule.getAccessToken()
//                Toast.makeText(mContext, "id : $id email : $email", Toast.LENGTH_SHORT).show()
//            }
//        } catch (e: JSONException) {
//            e.printStackTrace()
//        }
//    }
//
//    override fun doInBackground(vararg params: Void?): String {
//
//        return mOAuthLoginModule.requestApi(mContext, at, url)
//    }
//}