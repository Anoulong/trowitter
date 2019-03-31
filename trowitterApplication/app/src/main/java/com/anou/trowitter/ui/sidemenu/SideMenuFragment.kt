package com.anou.trowitter.ui.sidemenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.anou.prototype.core.controller.ApplicationController
import com.anou.prototype.core.usecase.SideMenuUseCase.SetData
import com.anou.prototype.core.usecase.SideMenuUseCase.InitializeModule
import com.anou.prototype.core.usecase.SideMenuUseCase.ShowSuccess
import com.anou.prototype.core.usecase.SideMenuUseCase.ShowEmpty
import com.anou.prototype.core.usecase.SideMenuUseCase.ShowLoading
import com.anou.prototype.core.usecase.SideMenuUseCase.HideLoading
import com.anou.prototype.core.usecase.SideMenuUseCase.ShowError
import com.anou.prototype.core.viewmodel.MainViewModel
import com.anou.trowitter.R
import com.anou.trowitter.base.BaseFragment
import com.anou.trowitter.databinding.FragmentSideMenuBinding
import com.anou.trowitter.navigation.MainRouter
import com.anou.trowitter.ui.MainActivity
import com.anou.trowitter.utils.Constants
import kotlinx.android.synthetic.main.fragment_side_menu.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class SideMenuFragment : BaseFragment() {
    val mainViewModel by viewModel<MainViewModel>()
    val mainRouter: MainRouter by inject()
    val applicationController: ApplicationController by inject()

    lateinit var binding: FragmentSideMenuBinding
    lateinit var adapter: SideMenuAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Bind views
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_side_menu, container, false)
        adapter = SideMenuAdapter(
            this,
            inflater = LayoutInflater.from(activity),
            mainRouter = mainRouter
        )
        binding.setLifecycleOwner(this)
        binding.sideMenuRecyclerView.adapter = adapter
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applicationController.currentUser.let { user ->
            if (!user.email.isNullOrBlank()) {
                usernamTextView.text = getString(R.string.profile_username, user.username, user.firstname, user.lastname)
                emailTextView.text = user.email
            }
        }
        mainViewModel.getModules().observe(this, Observer { usecases ->
            usecases?.let {

                when (usecases) {
                    is SetData -> {
                        adapter.setData(usecases.modules)
                    }
                    is InitializeModule -> {
                        mainRouter.onModuleSelected(activity as MainActivity, usecases.module, true)
                    }
                    is ShowError -> {
                        Toast.makeText(activity, usecases.errorMessage, Toast.LENGTH_LONG).show()
                    }
                    is ShowSuccess -> {
                        Toast.makeText(activity, usecases.successMessage, Toast.LENGTH_LONG).show()
                    }
                    is ShowEmpty -> {
                        Toast.makeText(activity, usecases.emptyMessage, Toast.LENGTH_LONG).show()
                    }
                    ShowLoading -> {
//                        showTransparentProgressDialog()
                    }
                    HideLoading -> {
//                        dismissProgressDialog()
                    }
                }
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.mainViewModel = mainViewModel
    }

}
