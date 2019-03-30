package com.anou.prototype.core.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.anou.prototype.core.controller.ApplicationController
import com.anou.prototype.core.repository.TweetRepository
import com.anou.prototype.core.strategy.ResourceStatus
import com.anou.prototype.core.usecase.TweetUseCase

class TweetViewModel(val applicationController: ApplicationController, val tweetRepository: TweetRepository) : BaseViewModel() {


    fun getTweets(): LiveData<TweetUseCase> {
         val liveSource = MutableLiveData<TweetUseCase>()
         val liveUseCase = MediatorLiveData<TweetUseCase>()
        liveSource.value = TweetUseCase.ShowEmpty("")

        val source = Transformations.switchMap(liveSource) {
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

                        if (data.size > 0) {
                            data.get(0).let { firstModule ->
                                liveUseCase.value = TweetUseCase.InitializeModule(firstModule)
                                //                                mainRouter.onModuleSelected(activity as MainActivity, firstModule, true)
                            }
                        } else {

                            liveUseCase.value = TweetUseCase.ShowEmpty("No modules")
                        }

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

    override fun onCleared() {
        super.onCleared()
        tweetRepository.onJobCancelled()
    }
}