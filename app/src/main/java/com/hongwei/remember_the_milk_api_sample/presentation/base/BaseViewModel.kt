package com.hongwei.remember_the_milk_api_sample.presentation.base

import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    override fun onCleared() {
        super.onCleared()
    }
}