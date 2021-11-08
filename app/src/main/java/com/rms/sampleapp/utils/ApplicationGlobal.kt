package com.rms.sampleapp.utils

import android.app.Application
import com.kachyng.rmssdk.RmsClient
import com.kachyng.rmssdk.util.RmsConfiguration

class ApplicationGlobal : Application() {

    companion object {
        var accessToken: String? = null
        var refreshToken: String? = null
    }

    override fun onCreate() {
        super.onCreate()

        RmsClient.configure(
            this, RmsConfiguration(
                clientID = "yourClientId",
                clientSecret = "yourClientSecret",
                callbackUrl = "yourCallbackUrl",
                signOutUrl = "yourSignOutUrl",
                baseUrl = "yourBaseUrl",
                oAuthUrl = "yourOauthUrl"
            )
        )

        RmsClient.enableLogging()
    }
}