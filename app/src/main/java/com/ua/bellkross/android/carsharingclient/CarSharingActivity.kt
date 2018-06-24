package com.ua.bellkross.android.carsharingclient

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.ua.bellkross.android.carsharingclient.PreferenceHelper.customPrefs
import com.ua.bellkross.android.carsharingclient.PreferenceHelper.licenceNumberKey
import com.ua.bellkross.android.carsharingclient.PreferenceHelper.passwordKey
import com.ua.bellkross.android.carsharingclient.PreferenceHelper.preferencesFileName
import com.ua.bellkross.android.carsharingclient.PreferenceHelper.set
import com.ua.bellkross.android.carsharingclient.adapter.CarsAdapter
import com.ua.bellkross.android.carsharingclient.rest.CarSharingApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_car_sharing.*

class CarSharingActivity : AppCompatActivity() {

    private lateinit var service: Disposable
    private lateinit var adapter: CarsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_sharing)
        rvCars.layoutManager = LinearLayoutManager(applicationContext)
        adapter = CarsAdapter()
        rvCars.adapter = adapter
        adapter.itemClick
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                        onNext = {
                            Toast.makeText(applicationContext, "It's car: ${adapter.cars[it]}!", Toast.LENGTH_SHORT).show()
                        },
                        onComplete = {

                        },
                        onError = {

                        }
                )
        fetchCars()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.car_sharing_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.miSignOut -> {
                val preferences = customPrefs(applicationContext, preferencesFileName)
                preferences[licenceNumberKey] = ""
                preferences[passwordKey] = ""
                val intent = Intent(this, AuthorizationActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.miRefresh -> {
                fetchCars()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun fetchCars() {
        val prefs = PreferenceHelper.customPrefs(applicationContext, preferencesFileName)

        val licenceNumber = prefs.getString(licenceNumberKey, "")
        val password = prefs.getString(passwordKey, "")

        val authorization = "licenceNumber=$licenceNumber, password=$password"
        service = CarSharingApi.create().getCarsForClient(authorization)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                        onNext = {
                            adapter.cars.clear()
                            adapter.cars.addAll(it)
                            runOnUiThread {
                                rvCars.adapter.notifyDataSetChanged()
                            }
                            service.dispose()
                        },
                        onError = {

                        },
                        onComplete = {

                        }
                )
    }


}