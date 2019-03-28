package com.anou.trowitter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anou.trowitter.R
import com.anou.trowitter.base.BaseFragment
import com.anou.trowitter.navigation.MainRouter
import com.anou.trowitter.ui.MainActivity
import org.koin.android.ext.android.inject


class LoadingFragment : BaseFragment() {
    val mainRouter: MainRouter by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mainRouter.onFragmentViewed(activity as MainActivity, "Loading...")
        return inflater.inflate(R.layout.fragment_loading, container, false)
    }
}
