package com.example.sitekittest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.shobhitpuri.custombuttons.GoogleSignInButton


class MainActivity : AppCompatActivity() {

    private val TAG: String = MainActivity::class.java.simpleName
    private lateinit var oauthLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Register the ActivityResultLauncher
        oauthLauncher = this.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->

            Log.d(TAG, result.data.toString())
            if (result.resultCode == RESULT_OK) {
                val authCode: String? = result.data?.getStringExtra("auth_code")
                val tokenResp: GoogleAccessTokenResp? =
                    result.data?.getParcelableExtra("token_resp")

                /**
                 * Handle the token response here
                 * */

                Log.d(TAG, "Auth Code: ${authCode}")
                Log.d(TAG, "Access Token: ${tokenResp?.accessToken}")

            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        //Handle google sign in button to Oauth Activity
        findViewById<GoogleSignInButton>(R.id.googleSignInButton).setOnClickListener {
            val intent = Intent(this, OauthActivity::class.java)
            oauthLauncher.launch(intent) // Use the launcher instead of startActivityForResult
        }
    }

}