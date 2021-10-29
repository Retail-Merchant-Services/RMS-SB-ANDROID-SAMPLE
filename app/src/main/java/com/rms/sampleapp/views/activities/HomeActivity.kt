package com.rms.sampleapp.views.activities

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.rms.sampleapp.R
import com.rms.sampleapp.viewmodels.EmptyViewModel
import com.kachyng.rmssdk.RmsClient
import com.kachyng.rmssdk.callbacks.RmsAuthCallback
import com.kachyng.rmssdk.exceptions.RmsAuthException


class HomeActivity : BaseViewModelActivity<EmptyViewModel>() {

    override fun provideBaseViewModel() = ViewModelProvider(this).get(EmptyViewModel::class.java)

    override fun getLayoutResource() = R.layout.activity_demo

    override fun init() {
    }

    override fun registerObservables() {
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.actionSignOut -> {
                RmsClient.signOut(object : RmsAuthCallback {
                    override fun success(accessToken: String, refreshToken: String) {
                    }

                    override fun error(exception: RmsAuthException) {
                    }

                    override fun signOut() {
                        startActivity(
                            Intent(
                                this@HomeActivity,
                                MainActivity::class.java
                            )
                        )
                        finishAffinity()
                    }
                })
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        RmsClient.verifyoAuthCallback(intent)
    }
}