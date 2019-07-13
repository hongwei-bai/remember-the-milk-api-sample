package com.hongwei.remember_the_milk_api_sample

import android.app.Application
import com.hongwei.remember_the_milk_api_sample.injection.component.ApplicationComponent
import com.hongwei.remember_the_milk_api_sample.injection.component.DaggerApplicationComponent
import com.hongwei.remember_the_milk_api_sample.injection.modules.ApplicationModule
import com.hongwei.remember_the_milk_api_sample.util.Logger
import javax.inject.Inject

class MyApplication : Application() {
    @Inject
    lateinit var applicationComponent: ApplicationComponent

    @Inject
    lateinit var logger: Logger

    private val TAG = javaClass.simpleName

    override fun onCreate() {
        super.onCreate()
        inject()
    }

    fun inject() {
        DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build().inject(this)
    }
}