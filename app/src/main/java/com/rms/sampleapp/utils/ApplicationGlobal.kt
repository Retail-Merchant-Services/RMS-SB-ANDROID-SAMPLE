package com.rms.sampleapp.utils

import android.app.Application
import com.kachyng.rmssdk.RmsClient
import com.kachyng.rmssdk.util.RmsConfiguration

class ApplicationGlobal : Application() {

    override fun onCreate() {
        super.onCreate()

//        RmsClient.configure(
//            this, RmsConfiguration(
//                clientID = "yourClientId",
//                clientSecret = "yourClientSecret",
//                callbackUrl = "yourCallbackUrl",
//                signOutUrl = "yourSignOutUrl",
//                baseUrl = "yourBaseUrl",
//                oAuthUrl = "yourOauthUrl"
//            )
//        )

        //Sandbox environment
//        RmsClient.configure(
//            this, RmsConfiguration(
//                clientID = "3c65r36dinkd5n8fo4ud4829cn",
//                clientSecret = "13dpgtvgfnqsn43drq20tdlvttps5m44luha7hauj6d6djua8j7f",
//                callbackUrl = "rmssdk://www.rmssdk.com/callback",
//                signOutUrl = "rmssdk://www.rmssdk.com/callback_signout",
//                baseUrl = "https://api-terminal-sandbox.retailmerchantservices.net/",
//                oAuthUrl = "api-auth-dev.retailmerchantservices.net"
//            )
//        )

        // QA environmnet
        RmsClient.configure(
            this, RmsConfiguration(
                clientID = "1srr1hksma58kcbfa444aua2fj",
                clientSecret = "n5vqqd5fokb1i8qkt8ienspbta51buu9s3ipj7k6nimuqf6t7tp",
                callbackUrl = "rmssdk://www.rmssdk.com/callback",
                signOutUrl = "rmssdk://www.rmssdk.com/callback_signout",
                baseUrl = "https://api-terminal-qa.retailmerchantservices.net/",
                oAuthUrl = "api-auth-qa.retailmerchantservices.net"
            )
        )


        RmsClient.enableLogging()
    }
}