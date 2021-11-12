package com.rms.sampleapp.utils

import android.app.Application
import com.kachyng.rmssdk.RmsClient
import com.kachyng.rmssdk.util.RmsConfiguration

class ApplicationGlobal : Application() {

    override fun onCreate() {
        super.onCreate()

        RmsClient.configure(
            this, RmsConfiguration(
                clientID = "3c65r36dinkd5n8fo4ud4829cn",
                clientSecret = "13dpgtvgfnqsn43drq20tdlvttps5m44luha7hauj6d6djua8j7f",
                callbackUrl = "rmssdk://www.rmssdk.com/callback",
                signOutUrl = "rmssdk://www.rmssdk.com/callback_signout",
                baseUrl = "https://api-terminal-sandbox.retailmerchantservices.net/",
                oAuthUrl = "api-auth-dev.retailmerchantservices.net"
            )
        )

        RmsClient.enableLogging()
    }
}