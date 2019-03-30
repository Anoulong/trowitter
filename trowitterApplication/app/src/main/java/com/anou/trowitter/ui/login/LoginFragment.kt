package com.anou.trowitter.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.ActivityNavigator
import com.anou.prototype.core.usecase.LoginUseCase
import com.anou.prototype.core.viewmodel.LoginViewModel
import com.anou.trowitter.R
import com.anou.trowitter.base.BaseFragment
import com.anou.trowitter.extension.setupClearButtonWithAction
import com.anou.trowitter.ui.MainActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : BaseFragment() {

    val loginViewModel by viewModel<LoginViewModel>()
    lateinit var snackbar: Snackbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCloseKeyboardOnTouchOutside(view)

        email.setupClearButtonWithAction()

        buttonLogin.setOnClickListener(View.OnClickListener {
            loginViewModel.loginUser(email.text.toString(), password.text.toString())
        })

        loginViewModel.loginUseCaseLiveData.observe(this, Observer { usecases ->
            usecases?.let {
                when (usecases) {
                    is LoginUseCase.navigateToMainScreen -> {
                        activity?.let { loginActivity ->
                            ActivityNavigator(loginActivity).navigate(
                                ActivityNavigator(loginActivity).createDestination()
                                    .setIntent(Intent(loginActivity, MainActivity::class.java)), null, null, null
                            )
                            loginActivity.finish()
                        }
                    }
                    is LoginUseCase.ShowError -> {
                        activity?.let { loginActivity ->
                            snackbar = Snackbar.make(
                                loginActivity?.findViewById(R.id.loginCoordinatorLayout),
                                usecases.errorMessage,
                                Snackbar.LENGTH_LONG
                            )
                            if(!snackbar.isShownOrQueued) {
                                snackbar.show()
                            }
                        }
                    }
                    LoginUseCase.ShowLoading -> {
//                        showTransparentProgressDialog()
                    }
                    LoginUseCase.HideLoading -> {
//                        dismissProgressDialog()
                    }
                }
            }
        })
    }

}
