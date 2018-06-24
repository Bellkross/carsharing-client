package com.ua.bellkross.android.carsharingclient

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ua.bellkross.android.carsharingclient.PreferenceHelper.licenceNumberKey
import com.ua.bellkross.android.carsharingclient.PreferenceHelper.passwordKey
import com.ua.bellkross.android.carsharingclient.PreferenceHelper.preferencesFileName

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        PreferenceHelper.customPrefs(applicationContext, preferencesFileName).apply {
            val isAuthorized =
                    getString(passwordKey, "").isNotBlank() &&
                            getString(licenceNumberKey, "").isNotBlank()

            startActivity(Intent(
                    this@SplashActivity,

                    if (isAuthorized)
                        CarSharingActivity::class.java
                    else
                        AuthorizationActivity::class.java
            ))
            finish()
        }
    }
}
