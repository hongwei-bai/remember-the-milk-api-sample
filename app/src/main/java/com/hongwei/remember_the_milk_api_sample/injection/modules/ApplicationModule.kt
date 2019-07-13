package com.hongwei.remember_the_milk_api_sample.injection.modules

import android.app.Application
import android.content.Context
import com.hongwei.remember_the_milk_api_sample.injection.annotations.AppContext
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: Application) {

    @Provides
    @Singleton
    @AppContext
    fun provideContext(): Context = application
}