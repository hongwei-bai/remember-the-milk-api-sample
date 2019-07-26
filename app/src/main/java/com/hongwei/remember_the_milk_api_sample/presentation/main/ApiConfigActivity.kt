package com.hongwei.remember_the_milk_api_sample.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.lifecycle.Observer
import com.hongwei.remember_the_milk_api_sample.R
import com.hongwei.remember_the_milk_api_sample.injection.component.DaggerActivityComponent
import com.hongwei.remember_the_milk_api_sample.injection.modules.ActivityModule
import com.hongwei.remember_the_milk_api_sample.presentation.base.BaseActivity
import kotlinx.android.synthetic.main.activity_apiconfig.*

class ApiConfigActivity : BaseActivity() {
    companion object {
        const val TAG = "rtm.api-config.activity"

        fun intent(context: Context): Intent {
            val intent = Intent(context, ApiConfigActivity::class.java)
            return intent
        }
    }

    lateinit var viewModel: ApiConfigViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apiconfig)

        viewModel = getViewModelProvider(this).get(ApiConfigViewModel::class.java)

        watchTextChange()

        observeViewModel()

        btn_submit.setOnClickListener { viewModel.submit(this, apiKey = edit_apikey.text.toString(), secret = edit_secret.text.toString()) }

        txt_link_api_register.setOnClickListener { viewModel.openApiApplicationLink(this) }
    }

    private fun watchTextChange() {
        fun EditText.afterTextChanged(action: () -> Unit) {
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    action.invoke()
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        }

        edit_apikey.afterTextChanged {
            viewModel.validate(apiKey = edit_apikey.text.toString(), secret = edit_secret.text.toString())
        }

        edit_secret.afterTextChanged {
            viewModel.validate(apiKey = edit_apikey.text.toString(), secret = edit_secret.text.toString())
        }
    }

    private fun observeViewModel() {
        viewModel.buttonEnableState.observe(this, Observer {
            btn_submit.isEnabled = it
        })
    }

    override fun inject() {
        DaggerActivityComponent.builder()
                .applicationComponent(getAppComponent())
                .activityModule(ActivityModule(this))
                .build()
                .inject(this)
    }
}
