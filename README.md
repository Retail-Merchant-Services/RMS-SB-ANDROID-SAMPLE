# RMS-SB-Android-SAMPLE-APP

Initialize the sdk configuration in your application global

```
RmsClient.configure(
        this, RmsConfiguration(
        clientID = "yourClientId",
        clientSecret = "yourClientSecret",
        callbackUrl = "yourCallBackUrl",
        signOutUrl = "yourSignOutUrl",
        baseUrl = "yourBaseUrl",
        oAuthUrl = "youroAuthUrl"
        )
)
```
