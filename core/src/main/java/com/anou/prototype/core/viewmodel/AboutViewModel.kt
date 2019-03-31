package com.anou.prototype.core.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.anou.prototype.core.repository.AboutRepository
import com.anou.prototype.core.strategy.ResourceStatus
import com.anou.prototype.core.usecase.AboutUseCase

/**
 * ViewModel interacts with model and also prepares observable(s) for News that can be observed by a View.
 * ViewModel can optionally provide hooks for the view to pass events to the model.
 * One of the important implementation strategies of this layer is to decouple it from the View, i.e,
 * ViewModel should not be aware about the view who is interacting with,
 * but aware of the (caller views) Fragments or Activities lifecycle.
 */
class AboutViewModel constructor(private val aboutRepository: AboutRepository) : BaseViewModel() {

    fun getAbout(): LiveData<AboutUseCase> {
        val liveSource = MutableLiveData<AboutUseCase>()
        val liveUseCase = MediatorLiveData<AboutUseCase>()
        liveSource.value = AboutUseCase.ShowLoading

        val source = Transformations.switchMap(liveSource) {
            aboutRepository.getAbout()
        }

        liveUseCase.addSource(source) { result ->
            when (result.status) {
                ResourceStatus.LOADING,
                ResourceStatus.FETCHING -> {
                    liveUseCase.value = AboutUseCase.ShowLoading
                }
                ResourceStatus.SUCCESS -> {
                    result.value?.let { data ->
                        liveUseCase.value = AboutUseCase.SetData(data)
                    }
                    liveUseCase.value = AboutUseCase.HideLoading

                }
                ResourceStatus.ERROR -> {
                    result.error?.message?.let { errorMessage ->
                        liveUseCase.value = AboutUseCase.ShowError(errorMessage)
                    }
                    liveUseCase.value = AboutUseCase.HideLoading
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
        aboutRepository.onJobCancelled()
    }
}
