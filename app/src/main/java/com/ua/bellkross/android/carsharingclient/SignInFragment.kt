package com.ua.bellkross.android.carsharingclient


import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import java.util.concurrent.TimeUnit


class SignInFragment : Fragment() {

    private lateinit var textInputLayoutLogin: TextInputLayout
    private lateinit var editTextLogin: TextInputEditText
    private val loginRegex = ".[A-Za-z]+[A-Za-z0-9]*".toRegex()
    private lateinit var isLoginValid: Observable<Boolean>

    private lateinit var textInputLayoutPassword: TextInputLayout
    private lateinit var editTextPassword: EditText
    private val passwordRegex = "([A-Za-z0-9]|\\p{Punct}){8,20}".toRegex()
    private lateinit var isPasswordValid: Observable<Boolean>

    private lateinit var signInButton: Button
    private lateinit var isSignInEnabled: Observable<Boolean>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)

        //Init views
        textInputLayoutLogin = view.findViewById(R.id.til_login)
        editTextLogin = view.findViewById(R.id.et_login)
        textInputLayoutLogin.isErrorEnabled = false

        textInputLayoutPassword = view.findViewById(R.id.til_password)
        editTextPassword = view.findViewById(R.id.et_password)
        textInputLayoutPassword.isErrorEnabled = false

        signInButton = view.findViewById(R.id.btn_sign_in)

        RxView.clicks(signInButton).subscribe {
            //TODO: add here GET request
        }

        //Create observables
        isLoginValid = RxTextView.textChanges(editTextLogin)
                .delay(400, TimeUnit.MILLISECONDS)
                .map { login ->
                    login.isEmpty() || login.toString().matches(loginRegex)
                }
                .distinctUntilChanged()

        isPasswordValid = RxTextView.textChanges(editTextPassword)
                .delay(400, TimeUnit.MILLISECONDS)
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
            activity!!.runOnUiThread {
                if (!isValid) {
                    textInputLayoutLogin.error = getString(R.string.login_input_error)
                } else {
                    textInputLayoutLogin.isErrorEnabled = false
                }
            }
        }

        isPasswordValid.subscribe { isValid ->
            activity!!.runOnUiThread {
                if (!isValid) {
                    textInputLayoutPassword.error = getString(R.string.password_input_error)
                } else {
                    textInputLayoutPassword.isErrorEnabled = false
                }
            }
        }

        isSignInEnabled.subscribe { signInEnabled ->
            activity!!.runOnUiThread {
                signInButton.isEnabled = signInEnabled &&
                        editTextLogin.text.isNotEmpty() &&
                        editTextPassword.text.isNotEmpty()
            }
        }
        return view
    }

}
