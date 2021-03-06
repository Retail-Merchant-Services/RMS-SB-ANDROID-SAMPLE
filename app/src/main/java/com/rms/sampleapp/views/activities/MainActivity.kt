package com.rms.sampleapp.views.activities

import android.content.Intent
import android.os.Bundle
import com.kachyng.rmssdk.RmsClient
import com.kachyng.rmssdk.callbacks.RmsAuthCallback
import com.kachyng.rmssdk.exceptions.RmsAuthException
import com.rms.sampleapp.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btSignIn.setOnClickListener {
            RmsClient.signIn(this, object : RmsAuthCallback {
                override fun success(accessToken: String, refreshToken: String) {
                    RmsClient.setTokens(accessToken, refreshToken)
                    startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                    finishAffinity()
                }

                override fun error(exception: RmsAuthException) {
                }

                override fun signOut() {

                }
            })
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        RmsClient.verifyoAuthCallback(intent)
    }
}