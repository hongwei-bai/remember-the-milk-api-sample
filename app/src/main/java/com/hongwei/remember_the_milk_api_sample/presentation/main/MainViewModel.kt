package com.hongwei.remember_the_milk_api_sample.presentation.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.hongwei.remember_the_milk_api_sample.ApiConfig.Constants.REQUEST_CODE_AUTH
import com.hongwei.remember_the_milk_api_sample.domain.model.DueTask
import com.hongwei.remember_the_milk_api_sample.domain.usecase.*
import com.hongwei.remember_the_milk_api_sample.presentation.base.BaseActivity
import com.hongwei.remember_the_milk_api_sample.presentation.base.BaseViewModel
import com.hongwei.remember_the_milk_api_sample.presentation.model.ViewState
import com.hongwei.remember_the_milk_api_sample.presentation.webview.AuthenticationWebViewActivity
import com.hongwei.remember_the_milk_api_sample.presentation.webview.RegisterWebViewActivity
import com.hongwei.remember_the_milk_api_sample.util.toddMMMyyyy
import com.hongwei.remember_the_milk_api_sample.util.toddMMyyyy_HHmmss
import it.bova.rtmapi.ServerException
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject


class MainViewModel @Inject constructor() : BaseViewModel() {
    @Inject
    lateinit var registerUseCase: RegisterUseCase

    @Inject
    lateinit var authenticationUseCase: AuthenticationUseCase

    @Inject
    lateinit var getDocTasksUseCase: GetDocTasksUseCase

    @Inject
    lateinit var getDueTasksUseCase: GetDueTasksUseCase

    @Inject
    lateinit var getTodayTasksUseCase: GetTodayTasksUseCase

    @Inject
    lateinit var checkAuthenticationStatusUseCase: CheckAuthenticationStatusUseCase

    @Inject
    lateinit var setAlarmUseCase: SetAlarmUseCase

    @Inject
    lateinit var getNextAlarmUseCase: GetNextAlarmUseCase

    @Inject
    lateinit var checkApiConfigUseCase: CheckApiConfigUseCase

    @Inject
    lateinit var registerTomorrowWakeupAlarmUseCase: RegisterTomorrowWakeupAlarmUseCase

    val viewState: MutableLiveData<ViewState> = MutableLiveData()

    val authenticationState: MutableLiveData<Boolean> = MutableLiveData()

    val summaryState: MutableLiveData<String> = MutableLiveData()

    val dueSummaryState: MutableLiveData<String> = MutableLiveData()

    val nextAlarmState: MutableLiveData<String> = MutableLiveData()

    val progressState: MutableLiveData<Float> = MutableLiveData()

    companion object {
        const val TAG = "rtm.main.viewmodel"
    }

    enum class ProgressMilestone(val progress: Float) {
        ZERO(0f),
        AUTH(0.17f),
        SUMMARY_TODAY(0.39f),
        DUE_TASK(0.61f),
        NEXT_ALARM(0.84f),
        ALL_DONE(1f)
    }

    fun checkApiConfigExist(): Boolean {
        return checkApiConfigUseCase.execute()
    }

    fun checkAuthenticationStatus(): Boolean {
        val success = checkAuthenticationStatusUseCase.execute()
        authenticationState.value = success
        return success
    }

    fun authenticate(activity: BaseActivity) {
        GlobalScope.launch(Dispatchers.IO) {
            val deferred = async {
                try {
                    authenticationUseCase.prepare()
                } catch (e: ServerException) {
                    GlobalScope.launch(Dispatchers.Main) {
                        authenticationState.value = false
                    }
                    null
                }
            }
            val validationUrl = deferred.await()
            if (validationUrl != null) {
                val urlRequest = AuthenticationWebViewActivity.UrlRequest(
                        title = "API key application",
                        url = validationUrl
                )
                activity.startActivityForResult(
                        AuthenticationWebViewActivity.newIntent(activity, urlRequest),
                        REQUEST_CODE_AUTH
                )
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
            getDocTasksUseCase.execute()
        }
    }

    fun registerTomorrowWakeup() {
        registerTomorrowWakeupAlarmUseCase.execute()
    }

    fun getTodayTask(): List<DueTask> {
        var todayTasks: List<DueTask> = emptyList()

        runBlocking(Dispatchers.IO) {
            todayTasks = getTodayTasksUseCase.execute()
        }

        val summary = getSummaryString(todayTasks)
        Log.i(TAG, "summary: $summary")

        GlobalScope.launch(Dispatchers.Main) { summaryState.value = summary }

        return todayTasks
    }

    fun getDueTask(): List<DueTask> {
        var dueTasks: List<DueTask> = emptyList()

        runBlocking(Dispatchers.IO) {
            dueTasks = getDueTasksUseCase.execute()
        }

        val summary = getDueSummaryString(dueTasks)
        Log.i(TAG, "due summary: $summary")

        GlobalScope.launch(Dispatchers.Main) { dueSummaryState.value = summary }

        return dueTasks
    }

    fun setAlarms(dueTasks: List<DueTask>) {
        setAlarmUseCase.execute(dueTasks)
    }

    fun getNextAlarm() {
        val calendar = getNextAlarmUseCase.execute()

        val text: String
        if (null == calendar) {
            text = "No incoming alarm."
        } else {
            text = "Next alarm: \n" + calendar.toddMMyyyy_HHmmss()
        }

        GlobalScope.launch(Dispatchers.Main) {
            nextAlarmState.value = text
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

    fun setProgress(milestone: ProgressMilestone, sub: Float = 1f) {
        when (milestone) {
            ProgressMilestone.ZERO -> GlobalScope.launch(Dispatchers.Main) { progressState.value = 0f }
            ProgressMilestone.ALL_DONE -> GlobalScope.launch(Dispatchers.Main) { progressState.value = 1f }
            else -> {
                val previousMilestone = when (milestone) {
                    ProgressMilestone.AUTH -> ProgressMilestone.ZERO
                    ProgressMilestone.SUMMARY_TODAY -> ProgressMilestone.AUTH
                    ProgressMilestone.DUE_TASK -> ProgressMilestone.SUMMARY_TODAY
                    ProgressMilestone.NEXT_ALARM -> ProgressMilestone.DUE_TASK
                    else -> ProgressMilestone.ZERO
                }
                val progressCalc = previousMilestone.progress + (milestone.progress - previousMilestone.progress) * sub
                GlobalScope.launch(Dispatchers.Main) {
                    progressState.value = progressCalc
                }
            }
        }
    }

    private fun getDueSummaryString(dueTasks: List<DueTask>): String {
        val calendar = Calendar.getInstance(Locale.ENGLISH)

        val stringBuilder = StringBuilder("Due tasks for incoming hours (${calendar.toddMMMyyyy()})")
        for (task in dueTasks) {
            stringBuilder.append("\n>$task")
        }
        return stringBuilder.toString()
    }

    private fun getSummaryString(tasks: List<DueTask>): String {
        val calendar = Calendar.getInstance(Locale.ENGLISH)

        val stringBuilder = StringBuilder("Summary for today (${calendar.toddMMMyyyy()})")
        for (task in tasks) {
            stringBuilder.append("\n>${task.name}")
        }
        return stringBuilder.toString()
    }
}