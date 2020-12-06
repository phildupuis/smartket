package com.lmi.myapplication.ui.main.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecommendationRowViewModel(date: String, stockValue: String, recommendation: String) :
    ViewModel() {

    val date: LiveData<String>

    val stockValue: LiveData<String>

    val recommendation: LiveData<String>

    init {
        this.date = MutableLiveData(date)
        this.stockValue = MutableLiveData(stockValue)
        this.recommendation = MutableLiveData(recommendation)
    }
}