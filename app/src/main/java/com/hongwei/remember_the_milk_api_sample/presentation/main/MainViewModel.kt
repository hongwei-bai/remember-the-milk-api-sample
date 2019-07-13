package com.hongwei.remember_the_milk_api_sample.presentation.main

import androidx.lifecycle.MutableLiveData
import com.hongwei.remember_the_milk_api_sample.domain.RegisterUseCase
import com.hongwei.remember_the_milk_api_sample.presentation.base.BaseActivity
import com.hongwei.remember_the_milk_api_sample.presentation.base.BaseViewModel
import com.hongwei.remember_the_milk_api_sample.presentation.model.ViewState
import com.hongwei.remember_the_milk_api_sample.presentation.webview.WebViewActivity
import javax.inject.Inject

class MainViewModel @Inject constructor() : BaseViewModel() {

    @Inject
    lateinit var registerUseCase: RegisterUseCase

    val viewState: MutableLiveData<ViewState> = MutableLiveData()

    fun applyForApiKey(activity: BaseActivity) {
        registerUseCase.getRegisterUrl().let { url ->
            val urlRequest = WebViewActivity.UrlRequest(
                    title = "API key application",
                    url = url
            )
            activity.startActivity(WebViewActivity.newIntent(activity, urlRequest))
        }
    }
}