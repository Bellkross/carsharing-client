package com.ua.bellkross.android.carsharingclient

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.ua.bellkross.android.carsharingclient.PreferenceHelper.customPrefs
import com.ua.bellkross.android.carsharingclient.PreferenceHelper.licenceNumberKey
import com.ua.bellkross.android.carsharingclient.PreferenceHelper.passwordKey
import com.ua.bellkross.android.carsharingclient.PreferenceHelper.preferencesFileName
import com.ua.bellkross.android.carsharingclient.PreferenceHelper.set

class CarSharingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_sharing)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.car_sharing_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.miSignOut) {
            val preferences = customPrefs(applicationContext, preferencesFileName)
            preferences[licenceNumberKey] = ""
            preferences[passwordKey] = ""
            val intent = Intent(this, AuthorizationActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}