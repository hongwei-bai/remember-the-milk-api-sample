package com.hongwei.remember_the_milk_api_sample.presentation.main

import androidx.lifecycle.MutableLiveData
import com.hongwei.remember_the_milk_api_sample.presentation.base.BaseViewModel
import com.hongwei.remember_the_milk_api_sample.presentation.model.ViewState
import javax.inject.Inject

class MainViewModel @Inject constructor() : BaseViewModel() {

    val viewState: MutableLiveData<ViewState> = MutableLiveData()
}