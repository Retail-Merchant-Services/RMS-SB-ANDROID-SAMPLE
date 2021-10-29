# RMS-SB-Android-SAMPLE-APP

To get a Git project into your build:

Step 1. Add the JitPack repository to your build file

Add it in your settings.gradle at the end of repositories:

allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency in your app build.gradle file

dependencies {
        implementation('com.github.Retail-Merchant-Services:RMS-SB-ANDROID-SDK:1.0.4@aar') {
            transitive = true
          }
     }
