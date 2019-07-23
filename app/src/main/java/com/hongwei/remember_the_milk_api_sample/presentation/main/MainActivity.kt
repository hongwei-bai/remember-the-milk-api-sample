package com.hongwei.remember_the_milk_api_sample.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
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
        const val TAG = "rtm.main-activity"

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

        if (!viewModel.checkAuthenticationStatus()) {
            viewModel.authenticate(this)
        }
    }

    private fun observeViewModel() {
        viewModel.authenticationState.observe(this, Observer { pass ->
            if (pass) {
                txt_hello.text = "Authentication passed."

                GlobalScope.async {
                    val dueTasks = viewModel.getDueTask()

                    viewModel.setAlarms(dueTasks)
                }
            }
        })

        viewModel.summaryState.observe(this, Observer {
            Log.i(TAG, "observe summaryState -> $it")
            txt_summary.text = it
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
        if (requestCode != REQUEST_CODE_AUTH) {
            return
        }

        when (resultCode) {
            RESULT_CODE_SUCCESS -> {
                viewModel.authenticate2()
            }
            RESULT_CODE_FAILURE -> {
                Toast.makeText(this, "authentication not complete!", Toast.LENGTH_LONG).show()
            }
        }
    }
}
