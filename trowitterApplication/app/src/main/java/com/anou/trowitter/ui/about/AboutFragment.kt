package com.anou.trowitter.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.anou.prototype.core.usecase.AboutUseCase
import com.anou.prototype.core.viewmodel.AboutViewModel
import com.anou.trowitter.R
import com.anou.trowitter.base.BaseFragment
import com.anou.trowitter.navigation.MainRouter
import com.anou.trowitter.ui.MainActivity
import com.anou.trowitter.utils.Constants
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_about.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class AboutFragment : BaseFragment() {

    val aboutViewModel by viewModel<AboutViewModel>()
    val mainRouter: MainRouter by inject()

    lateinit var moduleTitle: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        arguments?.let { bundle ->
            bundle.get(Constants.MODULE_TITLE)?.let { title ->
                moduleTitle = title.toString()
            }
        }

        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainRouter.onFragmentViewed(activity as MainActivity, moduleTitle)

        aboutViewModel.getAbout().observe(this, Observer { usecases ->
            usecases?.let {

                when (usecases) {
                    is AboutUseCase.SetData -> {
                        usecases.about?.let { about ->
                            if(!about.image.isNullOrBlank()) {
                                Glide.with(this).load(about.image).into(fragmentAboutImage)
                            }
                            fragmentAboutTitle.text = about.title
                            fragmentAboutSubtitle.text = about.subtitle
                            fragmentAboutDescription.text = about.description

                        }
                    }
                    is AboutUseCase.ShowError -> {
                        Toast.makeText(activity, usecases.errorMessage, Toast.LENGTH_LONG).show()
                    }
                    AboutUseCase.ShowLoading -> {
//                        showTransparentProgressDialog()
                    }
                    AboutUseCase.HideLoading -> {
//                        dismissProgressDialog()
                    }
                }
            }
        })
    }
}
