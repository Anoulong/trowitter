package com.anou.trowitter.ui.fragment.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anou.trowitter.R
import com.anou.trowitter.utils.Constants
import com.anou.prototype.core.viewmodel.MainViewModel
import com.anou.trowitter.base.BaseFragment
import com.anou.trowitter.navigation.MainRouter
import com.anou.trowitter.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_about.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class AccountFragment : BaseFragment() {

    val mainViewModel by viewModel<MainViewModel>()
    val mainRouter: MainRouter by inject()
    lateinit var moduleEid: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        arguments?.let { bundle ->
            bundle.get(Constants.MODULE_ID)?.let { eid ->
                moduleEid = eid.toString()
            }
        }
        return inflater.inflate(R.layout.fragment_about, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val navBuilder = NavOptions.Builder()
//        val navOptions = navBuilder.setPopUpTo(R.id.loadingFragment, true).build()
//        NavHostFragment.findNavController(this).navigate(R.id.welcomeFragment, null, navOptions)


        textViewTitleAbout?.text = moduleEid
        mainRouter.onFragmentViewed(activity as MainActivity, "About Fragment")
    }

}
