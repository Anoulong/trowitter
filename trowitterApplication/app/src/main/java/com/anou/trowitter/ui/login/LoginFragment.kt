package com.anou.trowitter.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.ActivityNavigator
import com.anou.prototype.core.usecase.LoginUseCase
import com.anou.prototype.core.viewmodel.LoginViewModel
import com.anou.trowitter.R
import com.anou.trowitter.base.BaseFragment
import com.anou.trowitter.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : BaseFragment() {

    val loginViewModel by viewModel<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonLogin.setOnClickListener(View.OnClickListener {
            loginViewModel.getRemoteUser("batman@yopmail.com", "trov").observe(this, Observer { usecases ->
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
                        is LoginUseCase.navigateToLoginScreen -> {

                            activity?.let { loginActivity ->
                                ActivityNavigator(loginActivity).navigate(
                                    ActivityNavigator(loginActivity).createDestination()
                                        .setIntent(Intent(loginActivity, MainActivity::class.java)), null, null, null
                                )
                                loginActivity.finish()
                            }


                        }
                        else -> {
                            activity?.let { loginActivity ->
                                ActivityNavigator(loginActivity).navigate(
                                    ActivityNavigator(loginActivity).createDestination()
                                        .setIntent(Intent(loginActivity, MainActivity::class.java)), null, null, null
                                )
                                loginActivity.finish()
                            }

                        }
                    }
                }
            })
        })
    }

}
