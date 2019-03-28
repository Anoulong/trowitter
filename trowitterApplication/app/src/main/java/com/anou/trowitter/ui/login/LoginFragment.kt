package com.anou.trowitter.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.ActivityNavigator
import com.anou.trowitter.R
import com.anou.trowitter.utils.Constants
import com.anou.prototype.core.viewmodel.MainViewModel
import com.anou.trowitter.base.BaseFragment
import com.anou.trowitter.navigation.MainRouter
import com.anou.trowitter.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_about.*
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : BaseFragment() {

    val mainViewModel by viewModel<MainViewModel>()
//    val mainRouter: MainRouter by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonLogin.setOnClickListener(View.OnClickListener {
            activity?.let { loginActivity ->
                ActivityNavigator(loginActivity).navigate(
                    ActivityNavigator(loginActivity).createDestination()
                        .setIntent(Intent(loginActivity, MainActivity::class.java)), null, null, null
                )
                loginActivity.finish()
            }

        })
//        mainRouter.onFragmentViewed(activity as MainActivity, "Login")
    }

}
