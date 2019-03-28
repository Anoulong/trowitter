package com.anou.trowitter.ui.tweet

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
import kotlinx.android.synthetic.main.fragment_tweet.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class TweetFragment : BaseFragment() {

    val mainViewModel by viewModel<MainViewModel>()
    val mainRouter: MainRouter by inject()
    lateinit var moduleEid: String
    lateinit var moduleTitle: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        arguments?.let { bundle ->
            bundle.get(Constants.MODULE_ID)?.let { eid ->
                moduleEid = eid.toString()
            }
            bundle.get(Constants.MODULE_TITLE)?.let { title ->
                moduleTitle = title.toString()
            }
        }
        return inflater.inflate(R.layout.fragment_tweet, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val navBuilder = NavOptions.Builder()
//        val navOptions = navBuilder.setPopUpTo(R.id.loadingFragment, true).build()
//        NavHostFragment.findNavController(this).navigate(R.id.welcomeFragment, null, navOptions)


        textViewTitleTweet?.text = moduleEid
        mainRouter.onFragmentViewed(activity as MainActivity, moduleTitle)
    }

}
