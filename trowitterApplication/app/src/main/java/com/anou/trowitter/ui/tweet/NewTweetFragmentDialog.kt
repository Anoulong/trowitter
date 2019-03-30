package com.anou.trowitter.ui.tweet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.anou.prototype.core.viewmodel.TweetViewModel
import com.anou.trowitter.R
import com.anou.trowitter.navigation.MainRouter
import com.anou.trowitter.utils.DrawableUtils
import kotlinx.android.synthetic.main.fragment_dialog_new_tweet.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewTweetFragmentDialog : DialogFragment() {

    val tweetViewModel by viewModel<TweetViewModel>()
    val mainRouter: MainRouter by inject()

    lateinit var moduleId: String

    companion object {
        val TAG = NewTweetFragmentDialog::class.java.simpleName
        fun newInstance(): NewTweetFragmentDialog {
            val fragment = NewTweetFragmentDialog()
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.ThemeOverlay_Material_ActionBar);
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getDialog().getWindow()
            .getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_dialog_new_tweet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(newTweetToolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        newTweetToolbar.setNavigationOnClickListener { dismiss() }
        newTweetToolbar.navigationIcon = DrawableUtils.getTintedDrawable(activity as AppCompatActivity, R.drawable.ic_action_close, R.color.colorPrimary)

    }


}
