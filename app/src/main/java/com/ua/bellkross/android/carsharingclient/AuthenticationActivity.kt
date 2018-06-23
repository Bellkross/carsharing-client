package com.ua.bellkross.android.carsharingclient

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

class AuthenticationActivity : AppCompatActivity() {

    private lateinit var menu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, SignInFragment())
                .commit()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.authentication_activity_menu, menu)
        this.menu = menu
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_sign_up -> {
                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, SignUpFragment())
                        .commit()
                menu.findItem(R.id.mi_sign_up).isVisible = false
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                true
            }
            android.R.id.home -> {
                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, SignInFragment())
                        .commit()
                menu.findItem(R.id.mi_sign_up).isVisible = true
                supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
