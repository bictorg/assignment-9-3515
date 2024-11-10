package edu.temple.browsr

import androidx.lifecycle.ViewModel

class BrowserViewModel : ViewModel(){
    private var numberOfTabs = 1

    fun addTab() {
        numberOfTabs++
    }

    fun getNumberOfTabs() = numberOfTabs

}