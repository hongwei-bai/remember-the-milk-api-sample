package com.hongwei.remember_the_milk_api_sample.injection.component

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.hongwei.remember_the_milk_api_sample.injection.annotations.ActivityContext
import com.hongwei.remember_the_milk_api_sample.injection.annotations.PerScreen
import com.hongwei.remember_the_milk_api_sample.injection.modules.ActivityModule
import com.hongwei.remember_the_milk_api_sample.injection.modules.ViewModelModule
import com.hongwei.remember_the_milk_api_sample.presentation.main.MainActivity
import com.hongwei.remember_the_milk_api_sample.presentation.webview.WebViewActivity
import dagger.Component

@PerScreen
@Component(dependencies = [ApplicationComponent::class], modules = [ActivityModule::class, ViewModelModule::class])
interface ActivityComponent : Singletons {

    fun provideAppCompatActivity(): AppCompatActivity

    @ActivityContext
    fun provideActivityContext(): Context

    fun inject(mainActivity: MainActivity)

    fun inject(webViewActivity: WebViewActivity)
}