package com.hongwei.remember_the_milk_api_sample.presentation.main

import android.content.Intent
import android.os.Bundle
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

class MainActivity : BaseActivity() {

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

                viewModel.getDueTask()
            }
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
