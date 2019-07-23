package com.hongwei.remember_the_milk_api_sample.presentation.main

import android.text.format.DateFormat
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.hongwei.remember_the_milk_api_sample.ApiConfig.Constants.REQUEST_CODE_AUTH
import com.hongwei.remember_the_milk_api_sample.ApiConfig.Cridentials.API_KEY
import com.hongwei.remember_the_milk_api_sample.ApiConfig.Cridentials.SHARED_SECRET
import com.hongwei.remember_the_milk_api_sample.domain.model.DueTask
import com.hongwei.remember_the_milk_api_sample.domain.usecase.*
import com.hongwei.remember_the_milk_api_sample.presentation.base.BaseActivity
import com.hongwei.remember_the_milk_api_sample.presentation.base.BaseViewModel
import com.hongwei.remember_the_milk_api_sample.presentation.model.ViewState
import com.hongwei.remember_the_milk_api_sample.presentation.webview.AuthenticationWebViewActivity
import com.hongwei.remember_the_milk_api_sample.presentation.webview.RegisterWebViewActivity
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject

class MainViewModel @Inject constructor() : BaseViewModel() {
    companion object {
        const val TAG = "rtm.main-viewmodel"
    }

    @Inject
    lateinit var registerUseCase: RegisterUseCase

    @Inject
    lateinit var authenticationUseCase: AuthenticationUseCase

    @Inject
    lateinit var getDocTasksUseCase: GetDocTasksUseCase

    @Inject
    lateinit var getDueTasksUseCase: GetDueTasksUseCase

    @Inject
    lateinit var checkAuthenticationStatusUseCase: CheckAuthenticationStatusUseCase

    @Inject
    lateinit var setAlarmUseCase: SetAlarmUseCase

    val viewState: MutableLiveData<ViewState> = MutableLiveData()

    val authenticationState: MutableLiveData<Boolean> = MutableLiveData()

    val summaryState: MutableLiveData<String> = MutableLiveData()

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

    fun getDocumentTask() {
        GlobalScope.launch(Dispatchers.IO) {
            getDocTasksUseCase.execute(API_KEY, SHARED_SECRET)
        }
    }

    fun getDueTask(): List<DueTask> {
        var dueTasks: List<DueTask> = emptyList()

        runBlocking(Dispatchers.IO) {
            dueTasks = getDueTasksUseCase.execute(API_KEY, SHARED_SECRET)
        }

        val summary = getSummaryString(dueTasks)
        Log.i(TAG, "summary: $summary")

        GlobalScope.launch(Dispatchers.Main) { summaryState.value = summary }

        return dueTasks
    }

    fun setAlarms(dueTasks: List<DueTask>) {
        setAlarmUseCase.execute(dueTasks)
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

    private fun getSummaryString(dueTasks: List<DueTask>): String {
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        val formatedDateString = DateFormat.format("dd-MMM-yyyy", calendar).toString()

        val stringBuilder = StringBuilder("Summary for today ($formatedDateString)")
        for (task in dueTasks) {
            stringBuilder.append("\n>$task")
        }
        return stringBuilder.toString()
    }
}