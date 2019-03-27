package com.anou.prototype.core.viewmodel

import com.anou.prototype.core.repository.AboutRepository
/**
 * ViewModel interacts with model and also prepares observable(s) for News that can be observed by a View.
 * ViewModel can optionally provide hooks for the view to pass events to the model.
 * One of the important implementation strategies of this layer is to decouple it from the View, i.e,
 * ViewModel should not be aware about the view who is interacting with,
 * but aware of the (caller views) Fragments or Activities lifecycle.
 */
class AboutViewModel  constructor(private val aboutRepository: AboutRepository) : BaseViewModel() {

    companion object {
        private val TAG = AboutViewModel::class.java.simpleName
    }

    fun getAbout(moduleEid: String) = aboutRepository.getAbout(moduleEid)

    override fun onCleared() {
        super.onCleared()
        aboutRepository.onJobCancelled()
    }
}
