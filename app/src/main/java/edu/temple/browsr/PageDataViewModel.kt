package edu.temple.browsr

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PageDataViewModel  : ViewModel(){
    private val currentUrl : MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    private val currentTitle: MutableLiveData<String> by lazy {
        MutableLiveData()
    }

    fun getCurrentUrl() : LiveData<String> = currentUrl
    fun getCurrentTitle(): LiveData<String> = currentTitle

    fun setCurrentUrl(url: String) {
        currentUrl.value = url
    }

    fun setCurrentTitle(title: String) {
        currentTitle.value = title
    }
}