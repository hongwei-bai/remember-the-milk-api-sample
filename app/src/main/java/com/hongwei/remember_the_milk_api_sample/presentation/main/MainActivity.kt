package com.hongwei.remember_the_milk_api_sample.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import com.hongwei.remember_the_milk_api_sample.ApiConfig.Constants.REQUEST_CODE_APICONFIG
import com.hongwei.remember_the_milk_api_sample.ApiConfig.Constants.REQUEST_CODE_AUTH
import com.hongwei.remember_the_milk_api_sample.ApiConfig.Constants.RESULT_CODE_FAILURE
import com.hongwei.remember_the_milk_api_sample.ApiConfig.Constants.RESULT_CODE_SUCCESS
import com.hongwei.remember_the_milk_api_sample.R
import com.hongwei.remember_the_milk_api_sample.injection.component.DaggerActivityComponent
import com.hongwei.remember_the_milk_api_sample.injection.modules.ActivityModule
import com.hongwei.remember_the_milk_api_sample.presentation.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class MainActivity : BaseActivity() {
    companion object {
        const val TAG = "rtm.main.activity"

        fun intent(context: Context): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            return intent
        }
    }

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = getViewModelProvider(this).get(MainViewModel::class.java)

        observeViewModel()

        viewModel.setProgress(MainViewModel.ProgressMilestone.ZERO)
        val bl = viewModel.checkApiConfigExist()
        viewModel.setProgress(MainViewModel.ProgressMilestone.AUTH, 0.25f)
        Log.i(TAG, "checkApiConfigExist: $bl")
        if (bl) {
            auth()
        } else {
            startActivityForResult(ApiConfigActivity.intent(this), REQUEST_CODE_APICONFIG)
        }
    }

    private fun auth() {
        if (!viewModel.checkAuthenticationStatus()) {
            viewModel.setProgress(MainViewModel.ProgressMilestone.AUTH, 0.5f)
            viewModel.authenticate(this)
            viewModel.setProgress(MainViewModel.ProgressMilestone.AUTH, 0.75f)
        }
    }

    private fun observeViewModel() {
        viewModel.progressState.observe(this, Observer {
            view_drawing.progress = it
            view_drawing.invalidate()
        })

        viewModel.authenticationState.observe(this, Observer { pass ->
            if (pass) {
                viewModel.setProgress(MainViewModel.ProgressMilestone.AUTH, 1f)
                txt_hello.text = "Authentication passed."

                GlobalScope.async {
                    viewModel.setProgress(MainViewModel.ProgressMilestone.SUMMARY_TODAY, 0.1f)
                    viewModel.registerTomorrowWakeup()
                    viewModel.setProgress(MainViewModel.ProgressMilestone.SUMMARY_TODAY, 0.5f)
                    viewModel.getTodayTask()
                    viewModel.setProgress(MainViewModel.ProgressMilestone.SUMMARY_TODAY, 1f)

                    val dueTasks = viewModel.getDueTask()
                    viewModel.setProgress(MainViewModel.ProgressMilestone.DUE_TASK, 1f)

                    viewModel.setAlarms(dueTasks)
                    viewModel.setProgress(MainViewModel.ProgressMilestone.NEXT_ALARM, 0.5f)

                    viewModel.getNextAlarm()
                    viewModel.setProgress(MainViewModel.ProgressMilestone.NEXT_ALARM, 1f)
                    kotlinx.coroutines.delay(100)
                    viewModel.setProgress(MainViewModel.ProgressMilestone.ALL_DONE)
                }
            } else {
                txt_hello.text = "Authentication failure."
            }
        })

        viewModel.summaryState.observe(this, Observer {
            txt_summary.text = it
        })

        viewModel.dueSummaryState.observe(this, Observer {
            txt_due_summary.text = it
        })

        viewModel.nextAlarmState.observe(this, Observer {
            txt_next_alarm.text = it
        })
    }

    override fun inject() {
        DaggerActivityComponent.builder()
                .applicationComponent(getAppComponent())
                .activityModule(ActivityModule(this))
                .build()
                .inject(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_AUTH -> {
                when (resultCode) {
                    RESULT_CODE_SUCCESS -> {
                        viewModel.authenticate2()
                    }
                    RESULT_CODE_FAILURE -> {
                        Toast.makeText(this, "authentication not complete!", Toast.LENGTH_LONG).show()
                    }
                }
            }

            REQUEST_CODE_APICONFIG -> auth()
        }
    }
}
