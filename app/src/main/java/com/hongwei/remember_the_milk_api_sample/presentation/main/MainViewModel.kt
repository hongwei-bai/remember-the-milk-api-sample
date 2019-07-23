package com.hongwei.remember_the_milk_api_sample.presentation.main

import androidx.lifecycle.MutableLiveData
import com.hongwei.remember_the_milk_api_sample.ApiConfig.Constants.REQUEST_CODE_AUTH
import com.hongwei.remember_the_milk_api_sample.ApiConfig.Cridentials.API_KEY
import com.hongwei.remember_the_milk_api_sample.ApiConfig.Cridentials.SHARED_SECRET
import com.hongwei.remember_the_milk_api_sample.domain.AuthenticationUseCase
import com.hongwei.remember_the_milk_api_sample.domain.CheckAuthenticationStatusUseCase
import com.hongwei.remember_the_milk_api_sample.domain.GetDueTasksUseCase
import com.hongwei.remember_the_milk_api_sample.domain.RegisterUseCase
import com.hongwei.remember_the_milk_api_sample.presentation.base.BaseActivity
import com.hongwei.remember_the_milk_api_sample.presentation.base.BaseViewModel
import com.hongwei.remember_the_milk_api_sample.presentation.model.ViewState
import com.hongwei.remember_the_milk_api_sample.presentation.webview.AuthenticationWebViewActivity
import com.hongwei.remember_the_milk_api_sample.presentation.webview.RegisterWebViewActivity
import kotlinx.coroutines.*
import javax.inject.Inject

class MainViewModel @Inject constructor() : BaseViewModel() {

    @Inject
    lateinit var registerUseCase: RegisterUseCase

    @Inject
    lateinit var authenticationUseCase: AuthenticationUseCase

    @Inject
    lateinit var getDueTasksUseCase: GetDueTasksUseCase

    @Inject
    lateinit var checkAuthenticationStatusUseCase: CheckAuthenticationStatusUseCase

    val viewState: MutableLiveData<ViewState> = MutableLiveData()

    val authenticationState: MutableLiveData<Boolean> = MutableLiveData()

    fun checkAuthenticationStatus(): Boolean {
        val success = checkAuthenticationStatusUseCase.execute()
        authenticationState.value = success
        return success
    }

    fun authenticate(activity: BaseActivity) {
        GlobalScope.launch(Dispatchers.IO) {
            val deferred = async {
                authenticationUseCase.prepare(API_KEY, SHARED_SECRET)
            }
            val validationUrl = deferred.await()
            if (validationUrl != null) {
                val urlRequest = AuthenticationWebViewActivity.UrlRequest(
                        title = "API key application",
                        url = validationUrl
                )
                activity.startActivityForResult(AuthenticationWebViewActivity.newIntent(activity, urlRequest), REQUEST_CODE_AUTH)
            }
        }
    }

    fun authenticate2() {
        var result = false
        runBlocking(Dispatchers.IO) {
            result = authenticationUseCase.execute()
        }
        authenticationState.value = result
    }

    fun getDueTask() {
        GlobalScope.launch(Dispatchers.IO) {
            getDueTasksUseCase.execute(API_KEY, SHARED_SECRET)
        }
    }

    fun applyForApiKey(activity: BaseActivity) {
        registerUseCase.getRegisterUrl().let { url ->
            val urlRequest = RegisterWebViewActivity.UrlRequest(
                    title = "API key application",
                    url = url
            )
            activity.startActivity(RegisterWebViewActivity.newIntent(activity, urlRequest))
        }
    }
}