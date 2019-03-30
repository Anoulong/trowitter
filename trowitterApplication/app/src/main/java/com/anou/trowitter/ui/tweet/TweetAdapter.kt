package com.anou.trowitter.ui.tweet


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.anou.prototype.core.db.tweet.TweetEntity
import com.anou.trowitter.R
import com.anou.trowitter.base.BaseRecyclerViewAdapter
import com.anou.trowitter.databinding.ItemTweetBinding
import com.anou.trowitter.navigation.MainRouter
import com.anou.trowitter.utils.DateTimeUtils


class TweetAdapter(val lifecycleOwner: LifecycleOwner, val inflater: LayoutInflater, val mainRouter: MainRouter) :
    BaseRecyclerViewAdapter<TweetEntity, TweetAdapter.TweetViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetViewHolder {
        return TweetViewHolder(
            lifecycleOwner,
            inflater,
            parent
        )
    }

    override fun onBindViewHolder(holder: TweetViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    override fun getItemViewType(position: Int): Int {
        return position
    }

    class TweetViewHolder(lifecycleOwner: LifecycleOwner, val binding: ItemTweetBinding) :
        RecyclerView.ViewHolder(binding.root) {

        constructor(lifecycleOwner: LifecycleOwner, inflater: LayoutInflater, container: ViewGroup) : this(
            lifecycleOwner,
            DataBindingUtil.inflate(inflater, R.layout.item_tweet, container, false)
        )

        fun bind(tweet: TweetEntity) {
            binding.tweet = tweet
            binding.itemTweetCardImage.visibility = if (tweet.image?.trim().isNullOrBlank()) View.GONE else View.VISIBLE
        }
    }

}