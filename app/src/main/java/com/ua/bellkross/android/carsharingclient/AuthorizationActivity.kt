package com.ua.bellkross.android.carsharingclient

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import com.ua.bellkross.android.carsharingclient.rest.CarsharingApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_authorization.*
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import java.util.concurrent.TimeUnit

class AuthorizationActivity : AppCompatActivity() {

    private val logTag = "debug"

    private val licenceNumberRegex = ".[A-Za-z]+[A-Za-z0-9_]*".toRegex()
    private val passwordRegex = "([A-Za-z0-9]|\\p{Punct}){6,20}".toRegex()

    private lateinit var isLoginValid: Observable<Boolean>
    private lateinit var isPasswordValid: Observable<Boolean>

    private lateinit var isSignInEnabled: Observable<Boolean>

    private val isOnline: Boolean
        get() {
            val cm: ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo: NetworkInfo? = cm.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)

        tilLicenceNumber.isErrorEnabled = false
        tilPassword.isErrorEnabled = false

        //Add subscription on sign in action
        RxView.clicks(btnSignIn).subscribe {
            if (isOnline) {

                tilLicenceNumber.visibility = View.GONE
                tilPassword.visibility = View.GONE
                pbAuthorization.visibility = View.VISIBLE


                val service = CarsharingApi.create().authorization(
                        authorization = "licenceNumber=${etLicenceNumber.text}, " +
                                "password=${String(Hex.encodeHex(DigestUtils.md5(etPassword.text.toString())))}"
                ).observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe { isAuthorized ->

                            tilLicenceNumber.visibility = View.VISIBLE
                            tilPassword.visibility = View.VISIBLE
                            pbAuthorization.visibility = View.GONE

                            if (isAuthorized) {
                                Toast.makeText(
                                        this.applicationContext,
                                        "Authorized!",
                                        Toast.LENGTH_SHORT
                                ).show()
                                //TODO: GOTO next activity.
                                //service.dispose()
                            } else {
                                Toast.makeText(
                                        this.applicationContext,
                                        R.string.incorrect_data,
                                        Toast.LENGTH_SHORT
                                ).show()
                            }

                        }

            } else {
                Toast.makeText(
                        this.applicationContext,
                        R.string.no_internet_connection,
                        Toast.LENGTH_SHORT
                ).show()
            }
        }

        //Create observables
        isLoginValid = RxTextView.textChanges(etLicenceNumber)
                .delay(1, TimeUnit.SECONDS)
                .map { login ->
                    login.isEmpty() || login.toString().matches(licenceNumberRegex)
                }
                .distinctUntilChanged()

        isPasswordValid = RxTextView.textChanges(etPassword)
                .delay(1, TimeUnit.SECONDS)
                .map { password ->
                    password.isEmpty() || password.toString().matches(passwordRegex)
                }
                .distinctUntilChanged()

        isSignInEnabled = Observable.combineLatest(
                isLoginValid,
                isPasswordValid,
                BiFunction { loginIsValid, passwordIsValid ->
                    loginIsValid && passwordIsValid
                }
        )

        //Subscribe on observable
        isLoginValid.subscribe { isValid ->
            runOnUiThread {
                if (!isValid) {
                    tilLicenceNumber.error = getString(R.string.licence_number_input_error)
                } else {
                    tilLicenceNumber.isErrorEnabled = false
                }
            }
        }

        isPasswordValid.subscribe { isValid ->
            runOnUiThread {
                if (!isValid) {
                    tilPassword.error = getString(R.string.password_input_error)
                } else {
                    tilPassword.isErrorEnabled = false
                }
            }
        }

        isSignInEnabled.subscribe { signInEnabled ->
            runOnUiThread {
                btnSignIn.isEnabled = signInEnabled &&
                        etLicenceNumber.text.isNotEmpty() &&
                        etPassword.text.isNotEmpty()
            }
        }
    }

}