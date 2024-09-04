package com.example.sitekittest

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okio.IOException


class OauthActivity : AppCompatActivity() {

    private val TAG: String = OauthActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_oauth)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Sign In Google OAuth
        googleSignInChromeCustomTabs()

    }

    /**
        Handle Google OAuth 2.0 URL with Chrome Custom Tabs
     */
    private fun googleSignInChromeCustomTabs() {
        val uri = Uri.parse(
            "https://accounts.google.com/o/oauth2/auth?" +
                    "client_id=${Constant.GOOGLE_ANDROID_CLIENT_ID}&" +
                    "redirect_uri=com.example.sitekittest%3A/oauth2redirect&" +
                    "response_type=code&" +
                    "scope=email%20profile" +
                    "&access_type=offline" +
                    "&prompt=consent"
        )

        val customTabsIntent = CustomTabsIntent.Builder().build()
        customTabsIntent.launchUrl(this, uri)
    }

    /**
     *   Handle Google OAuth 2.0 Redirect URI with app links
     */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val uri = intent.data

        Log.d(TAG, "Received URI: $uri")

        uri?.let {
            val code = it.getQueryParameter("code")
            if (code != null) {

                lifecycleScope.launch {

                    //Obtain refresh and access token
                    val tokenResp = exchangeAuthCodeForToken(code)

                    //Handle intent to return results
                    val resultIntent = Intent()
                    resultIntent.putExtra("auth_code", code)
                    resultIntent.putExtra("token_resp", tokenResp)
                    setResult(RESULT_OK, resultIntent)

                    finish()
                }

            } else if (it.getQueryParameter("error") != null) {
                Log.e(TAG, "Error in OAuth flow")
                setResult(RESULT_CANCELED)
                finish()
            } else {
                Log.e(TAG, "URI is invalid")
                setResult(RESULT_CANCELED)
                finish()
            }
        } ?: run {
            Log.e(TAG, "URI is null")
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    /**
     *   Handle Google OAuth 2.0 Access Token & Refresh Token
     */
    private suspend fun exchangeAuthCodeForToken(authCode: String): GoogleAccessTokenResp? {
        val client = OkHttpClient()

        //Handle Google Access Token & Refresh Token Retrieval
        val body: RequestBody = FormBody.Builder()
            .add("code", authCode)
            .add("client_id", Constant.GOOGLE_ANDROID_CLIENT_ID)
            .add("redirect_uri", "com.example.sitekittest:/oauth2redirect")
            .add("grant_type", "authorization_code")
            .build()

        val request: Request = Request.Builder()
            .url("https://oauth2.googleapis.com/token")
            .post(body)
            .build()

        val response = withContext(Dispatchers.IO) {
            client.newCall(request).execute()
        }

        //Handle Token Response
        var googleAccessTokenResp: GoogleAccessTokenResp? = null

        if (response.isSuccessful) {
            val responseData = response.body?.string()
            Log.d(TAG, responseData ?: "No response data")

            responseData?.let {
                val gson = Gson()
                googleAccessTokenResp = gson.fromJson(it, GoogleAccessTokenResp::class.java)
                Log.d(TAG, "Access Token: ${googleAccessTokenResp?.accessToken}")
            }
        } else {
            Log.e(TAG, "Request failed with code: ${response.code}")
            throw IOException("Unexpected code $response")
        }

        return googleAccessTokenResp
    }
}
