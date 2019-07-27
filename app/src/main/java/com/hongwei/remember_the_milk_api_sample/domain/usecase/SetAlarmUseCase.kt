package com.hongwei.remember_the_milk_api_sample.domain.usecase

import android.content.Context
import com.hongwei.remember_the_milk_api_sample.ApiConfig
import com.hongwei.remember_the_milk_api_sample.domain.model.DueTask
import com.hongwei.remember_the_milk_api_sample.injection.annotations.AppContext
import com.hongwei.remember_the_milk_api_sample.presentation.alarm.AlarmLauncher
import javax.inject.Inject

class SetAlarmUseCase @Inject constructor(@AppContext val context: Context) {
    fun execute(dueTasks: List<DueTask>) {
        for (task in dueTasks) {
            AlarmLauncher.addAlarm(context, task.due, ApiConfig.AlarmType.TODO, task.name)
        }
    }
}