package com.anou.trowitter.ui.tweet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.anou.prototype.core.usecase.TweetUseCase
import com.anou.prototype.core.viewmodel.TweetViewModel
import com.anou.trowitter.R
import com.anou.trowitter.base.BaseFragment
import com.anou.trowitter.navigation.MainRouter
import com.anou.trowitter.ui.MainActivity
import com.anou.trowitter.utils.Constants
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class TweetFragment : BaseFragment() {

    val tweetViewModel by viewModel<TweetViewModel>()
    val mainRouter: MainRouter by inject()

    lateinit var binding: FragmentTweetBinding
    lateinit var adapter: TweetAdapter
    lateinit var categoryEid: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        arguments?.let { bundle ->
            bundle.get(Constants.CATEGORY_EID)?.let { eid ->
                categoryEid = eid.toString()
            }
            bundle.get(Constants.CATEGORY_TITLE)?.let { title ->
                mainRouter.onFragmentViewed(activity as MainActivity, title.toString())
            }
        }

        // Bind views
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tweet, container, false)
        adapter = TweetAdapter(
            this,
            inflater = LayoutInflater.from(activity),
            mainRouter = mainRouter
        )
        binding.setLifecycleOwner(this)
        binding.fragmentTweetList.adapter = adapter
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tweetViewModel.getTweets().observe(this, Observer { usecases ->
            usecases?.let {

                when (usecases) {
                    is TweetUseCase.SetData -> {
                        adapter.setData(usecases.tweets)
                    }
                    is TweetUseCase.InitializeModule -> {
//                        mainRouter.onModuleSelected(activity as MainActivity, usecases.tweet, true)
                    }
                    is TweetUseCase.ShowError -> {
                        Toast.makeText(activity, usecases.errorMessage, Toast.LENGTH_LONG).show()
                    }
                    is TweetUseCase.ShowSuccess -> {
                        Toast.makeText(activity, usecases.successMessage, Toast.LENGTH_LONG).show()
                    }
                    is TweetUseCase.ShowEmpty -> {
                        Toast.makeText(activity, usecases.emptyMessage, Toast.LENGTH_LONG).show()
                    }
                    TweetUseCase.ShowLoading -> {
//                        showTransparentProgressDialog()
                    }
                    TweetUseCase.HideLoading -> {
//                        dismissProgressDialog()
                    }
                }
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.viewModel = tweetViewModel
    }

}
