package com.hongwei.remember_the_milk_api_sample.presentation.main

import android.os.Bundle
import com.hongwei.remember_the_milk_api_sample.R
import com.hongwei.remember_the_milk_api_sample.injection.component.DaggerActivityComponent
import com.hongwei.remember_the_milk_api_sample.injection.modules.ActivityModule
import com.hongwei.remember_the_milk_api_sample.presentation.base.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun inject() {
        DaggerActivityComponent.builder()
            .applicationComponent(getAppComponent())
            .activityModule(ActivityModule(this))
            .build()
            .inject(this)
    }
}
