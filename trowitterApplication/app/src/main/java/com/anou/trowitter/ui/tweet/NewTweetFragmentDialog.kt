package com.anou.trowitter.ui.tweet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.anou.prototype.core.controller.ApplicationController
import com.anou.prototype.core.db.tweet.TweetEntity
import com.anou.prototype.core.db.user.UserEntity
import com.anou.prototype.core.viewmodel.LoginViewModel
import com.anou.prototype.core.viewmodel.TweetViewModel
import com.anou.trowitter.R
import com.anou.trowitter.databinding.FragmentDialogNewTweetBinding
import com.anou.trowitter.navigation.MainRouter
import com.anou.trowitter.ui.MainActivity
import com.anou.trowitter.utils.DateTimeUtils
import com.anou.trowitter.utils.DrawableUtils
import kotlinx.android.synthetic.main.fragment_dialog_new_tweet.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NewTweetFragmentDialog : DialogFragment() {

    val loginViewModel by viewModel<LoginViewModel>()
    val tweetViewModel by viewModel<TweetViewModel>()
    val applicationController: ApplicationController by inject()

    lateinit var binding: FragmentDialogNewTweetBinding

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Bind views
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dialog_new_tweet, container, false)
        binding.setLifecycleOwner(this)
        applicationController.currentUser.username?.let {name ->
            binding.newTweetUsername.setText(name)
        }
        binding.buttonSendTweet.setOnClickListener {

            if(binding.newTweetEditText.text.isNullOrBlank()){
                Toast.makeText(activity, "Empty Tweet is unpleasant to read!", Toast.LENGTH_SHORT).show()
            }else{
                applicationController.currentUser.username?.let {name ->
                    binding.newTweetUsername.setText(name)
                    val tweetEntity = TweetEntity(name, binding.newTweetEditText.text.toString(), DateTimeUtils.getCurrentDate(),  DateTimeUtils.getCurrentDate() )
                    tweetViewModel.createTweet(this, tweetEntity)
                    Toast.makeText(activity, "Tweet sent!", Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(newTweetToolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        newTweetToolbar.setNavigationOnClickListener { dismiss() }
        newTweetToolbar.navigationIcon = DrawableUtils.getTintedDrawable(
            activity as AppCompatActivity,
            R.drawable.ic_action_close,
            R.color.colorPrimary
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        binding.viewModel = tweetViewModel
    }

}
