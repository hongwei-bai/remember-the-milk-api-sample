package com.hongwei.remember_the_milk_api_sample.presentation.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import com.hongwei.remember_the_milk_api_sample.ApiConfig.Alarm.Type.KEY_NAME
import com.hongwei.remember_the_milk_api_sample.ApiConfig.Alarm.Type.KEY_TYPE
import java.util.*

class AlarmLauncher {
    companion object {
        const val TAG = "rtm.alarm-launcher"

        fun addAlarm(context: Context, date: Date, type: String, name: String? = null) {
            val bundle = Bundle()
            bundle.putString(KEY_TYPE, type)
            name?.let { bundle.putString(KEY_NAME, name) }

            val intent = Intent(context, AlarmReceiver::class.java)
            intent.putExtras(bundle)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

            val calendar = Calendar.getInstance()
            calendar.time = date

            Log.i(TAG, "add alarm: type: $type, time: ${DateFormat.format("dd-MM-yyyy HH:mm:ss", calendar)}")

            // register new alarm
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent)
        }
    }
}