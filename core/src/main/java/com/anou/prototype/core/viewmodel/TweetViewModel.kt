package com.anou.prototype.core.viewmodel

import androidx.lifecycle.*
import com.anou.prototype.core.controller.ApplicationController
import com.anou.prototype.core.db.tweet.TweetEntity
import com.anou.prototype.core.repository.TweetRepository
import com.anou.prototype.core.strategy.ResourceStatus
import com.anou.prototype.core.usecase.TweetUseCase

class TweetViewModel(val applicationController: ApplicationController, val tweetRepository: TweetRepository) :
    BaseViewModel() {
    val tweetLiveData= MutableLiveData<Boolean>()

    fun getTweets(): LiveData<TweetUseCase> {
        val liveUseCase = MediatorLiveData<TweetUseCase>()
        tweetLiveData.value = true

        val source = Transformations.switchMap(tweetLiveData) {
            tweetRepository.loadTweets()
        }

        liveUseCase.addSource(source) { result ->
            when (result.status) {
                ResourceStatus.LOADING,
                ResourceStatus.FETCHING -> {
                    liveUseCase.value = TweetUseCase.ShowLoading
                }
                ResourceStatus.SUCCESS -> {
                    result.value?.let { data ->
                        liveUseCase.value = TweetUseCase.SetData(data)
                    }

                    liveUseCase.value = TweetUseCase.HideLoading

                }
                ResourceStatus.ERROR -> {
                    result.error?.message?.let { errorMessage ->
                        liveUseCase.value = TweetUseCase.ShowError(errorMessage)
                    }
                    liveUseCase.value = TweetUseCase.HideLoading
                }
                ResourceStatus.UNKNOWN,
                ResourceStatus.INVALID -> {

                }
            }
        }

        return liveUseCase
    }

    fun refresh(){
        tweetLiveData.value = true
    }
    fun createTweet(lifecycleOwner: LifecycleOwner, tweetEntity: TweetEntity) {
        tweetRepository.insertTweets(tweetEntity).observe(lifecycleOwner, Observer { result ->
            when (result.status) {
                ResourceStatus.SUCCESS -> {

                }
                ResourceStatus.ERROR -> {

                }
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        tweetRepository.onJobCancelled()
    }
}