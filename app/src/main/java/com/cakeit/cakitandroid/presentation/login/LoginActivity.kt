package com.cakeit.cakitandroid.presentation.login

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cakeit.cakitandroid.R
import com.cakeit.cakitandroid.base.BaseActivity
import com.cakeit.cakitandroid.data.source.local.prefs.SharedPreferenceController
import com.cakeit.cakitandroid.databinding.ActivityLoginBinding
import com.cakeit.cakitandroid.presentation.main.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.api.client.googleapis.auth.oauth2.*
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException


class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    var fromToScreen : String = ""

    private val REQUEST_CODE_GOOGLE_LOGIN = 100

    companion object {
        lateinit var googleSignInClient : GoogleSignInClient
        val mOAuthLoginModule = OAuthLogin.getInstance()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding()
        binding = getViewDataBinding()
        binding.viewModel = getViewModel()

        if(intent != null) {
            if(intent.getStringExtra("fromToScreen") != null) fromToScreen = intent.getStringExtra("fromToScreen")!!
        }

        val imgArr = arrayOf(
            R.drawable.bg_login_image_1,
            R.drawable.bg_login_image_2,
            R.drawable.bg_login_image_3,
            R.drawable.bg_login_image_4,
            R.drawable.bg_login_image_5,
            R.drawable.bg_login_image_6,
            R.drawable.bg_login_image_7,
            R.drawable.bg_login_image_8,
            R.drawable.bg_login_image_9,
            R.drawable.bg_login_image_10,
            R.drawable.bg_login_image_11
        )

        val backgroundRndIdx = (1..11).random()
        iv_login_background.setImageResource(imgArr[backgroundRndIdx])

        tv_login_eixt.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        Log.d("songjem", "fromToScreen = " + fromToScreen)

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
                SharedPreferenceController.setAccessToken(applicationContext, token)
            })
        }

        tv_login_eixt.setOnClickListener{
            if(fromToScreen.equals("ZzimFragment") || fromToScreen.isNullOrEmpty()) {
                val intent = Intent(applicationContext, MainActivity::class.java)
                finish()
                startActivity(intent)
            } else if(fromToScreen.equals("MyPageFragment") || fromToScreen.equals("ShopDetailActivity")
                || fromToScreen.equals("DesignDetailActivity")) {
                super.onBackPressed()
            } else super.onBackPressed()
        }

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
            val client_id = getString(R.string.AUTHCODE_GOOGLE_CLIENTID)
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(client_id)
                .requestServerAuthCode(client_id)
//                .requestEmail()
                .build()

            googleSignInClient = GoogleSignIn.getClient(this, gso)

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
                val transport = NetHttpTransport()
                val jacksonFactory = JacksonFactory()
                val authcode = task.result?.serverAuthCode
                var tokenRequest = GoogleAuthorizationCodeTokenRequest(
                    transport,
                    jacksonFactory,
                    getString(R.string.AUTHCODE_GOOGLE_CLIENTID),
                    getString(R.string.AUTHCODE_GOOGLE_SECRET),
                    authcode,
                    GoogleOAuthConstants.OOB_REDIRECT_URI
                ) // Specify the same redirect URI that you use with your web
                // app. If you don't have a web version of your app, you can
                // specify an empty string.
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        val tokenResponse = tokenRequest.execute()
                        val accessToken = tokenResponse.accessToken
                        runOnUiThread { binding.viewModel?.sendGoogleCodeToServer(accessToken) }
                    } catch (ioe : IOException){
                        Log.d(TAG, "Google signin failed IOException: ${ioe.message}")
                        showToast("Google signin failed IOException: ${ioe.message}")
                    }
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

    override fun onBackPressed() {
        if(fromToScreen.equals("ZzimFragment")) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            finish()
            startActivity(intent)
        } else if(fromToScreen.equals("MyPageFragment") || fromToScreen.equals("ShopDetailActivity")
            || fromToScreen.equals("DesignDetailActivity")) {
            super.onBackPressed()
        } else super.onBackPressed()
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