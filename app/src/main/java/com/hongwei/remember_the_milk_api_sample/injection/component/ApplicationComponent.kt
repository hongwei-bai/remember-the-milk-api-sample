package com.hongwei.remember_the_milk_api_sample.injection.component

import com.hongwei.remember_the_milk_api_sample.MyApplication
import com.hongwei.remember_the_milk_api_sample.injection.modules.ApplicationModule
import com.hongwei.remember_the_milk_api_sample.injection.modules.SingletonModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, SingletonModule::class])
interface ApplicationComponent: Singletons {

    fun inject(application: MyApplication)
}