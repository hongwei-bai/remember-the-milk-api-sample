package com.hongwei.remember_the_milk_api_sample.injection.component

import android.content.Context
import com.hongwei.remember_the_milk_api_sample.injection.annotations.AppContext
import com.hongwei.remember_the_milk_api_sample.util.Logger

interface Singletons {
    @AppContext
    fun provideContext(): Context

    fun provideLogger(): Logger
}