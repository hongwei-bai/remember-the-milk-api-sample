package com.hongwei.remember_the_milk_api_sample.domain.usecase

import android.content.Context
import com.hongwei.remember_the_milk_api_sample.ApiConfig
import com.hongwei.remember_the_milk_api_sample.injection.annotations.AppContext
import com.hongwei.remember_the_milk_api_sample.presentation.alarm.AlarmLauncher
import com.hongwei.remember_the_milk_api_sample.util.tomorrow
import javax.inject.Inject

class RegisterTomorrowWakeupAlarmUseCase @Inject constructor(@AppContext val context: Context) {
    fun execute() {
        val tomorrow7am = tomorrow(7)
        AlarmLauncher.addAlarm(context, tomorrow7am, ApiConfig.Alarm.Type.WAKE_UP, "Morning wake up")
    }
}