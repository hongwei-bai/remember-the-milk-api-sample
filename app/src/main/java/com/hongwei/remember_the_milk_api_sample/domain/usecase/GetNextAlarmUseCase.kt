package com.hongwei.remember_the_milk_api_sample.domain.usecase

import android.content.Context
import com.hongwei.remember_the_milk_api_sample.injection.annotations.AppContext
import com.hongwei.remember_the_milk_api_sample.presentation.alarm.AlarmLauncher
import java.util.*
import javax.inject.Inject

class GetNextAlarmUseCase @Inject constructor(@AppContext val context: Context) {
    fun execute(): Calendar? {
        return AlarmLauncher.getNextAlarm(context)
    }
}