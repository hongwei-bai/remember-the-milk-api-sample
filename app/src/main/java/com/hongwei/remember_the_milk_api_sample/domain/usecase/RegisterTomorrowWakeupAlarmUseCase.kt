package com.hongwei.remember_the_milk_api_sample.domain.usecase

import android.content.Context
import com.hongwei.remember_the_milk_api_sample.ApiConfig
import com.hongwei.remember_the_milk_api_sample.injection.annotations.AppContext
import com.hongwei.remember_the_milk_api_sample.presentation.alarm.AlarmLauncher
import java.util.*
import javax.inject.Inject

class RegisterTomorrowWakeupAlarmUseCase @Inject constructor(@AppContext val context: Context) {
    fun execute() {
        val now = Date()
        val tomorrow7am = Date(now.year, now.month, now.date, 7, 0, 0)

        AlarmLauncher.addAlarm(context, tomorrow7am, ApiConfig.Alarm.Type.WAKE_UP, "Morning wake up")
    }
}