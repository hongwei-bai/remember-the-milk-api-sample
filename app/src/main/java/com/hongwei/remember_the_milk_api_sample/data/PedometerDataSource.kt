package com.hongwei.remember_the_milk_api_sample.data

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.hongwei.remember_the_milk_api_sample.data.local.entity.AppDatabase
import com.hongwei.remember_the_milk_api_sample.data.local.entity.PedometerDay
import com.hongwei.remember_the_milk_api_sample.data.local.entity.PedometerDayDao
import com.hongwei.remember_the_milk_api_sample.injection.annotations.AppContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PedometerDataSource @Inject constructor(@AppContext private val context: Context) {
    companion object {
        private const val TAG = "rtm.pedo"
    }

    private val pedometerDayDao: PedometerDayDao = AppDatabase.getInstance(context).pedometerDayDao()

    suspend fun test() {
        GlobalScope.launch(Dispatchers.IO) {
            val list = pedometerDayDao.getPedometerDays()
            list.forEach {
                Log.d(TAG, "list all: $it")
            }
        }
    }

    suspend fun saveStepCounter(steps: Long) {
        val today = Calendar.getInstance()
        val day1970 = Calendar.getInstance().apply {
            timeInMillis = 0L
        }
        val dayDiff = (today.timeInMillis - day1970.timeInMillis) / 1000 / 3600 / 24

        val record = PedometerDay(
                id = 0,
                dayDiffFrom1970 = dayDiff,
                steps = steps
        )

        GlobalScope.launch(Dispatchers.IO) {
            if (pedometerDayDao.getPedometerDay(dayDiff) != null) {
                pedometerDayDao.update(record)
            } else {
                pedometerDayDao.insert(record)
            }
            Log.d(TAG, "saveStepCounter dayDiff: $dayDiff, steps: $steps")
        }
    }

    suspend fun getStepCountFromLastReboot(): Long {
        return suspendCancellableCoroutine { continuation ->
            val manager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            val stepSensor = manager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
            stepSensor?.let {
                manager.registerListener(object : SensorEventListener {
                    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
                        Log.d(TAG, "onAccuracyChanged, sensor: $sensor, accuracy: $accuracy")
                    }

                    override fun onSensorChanged(event: SensorEvent) {
                        Log.d(TAG, "onSensorChanged, event: $event")

                        if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                            Log.d(TAG, "onSensorChanged: current step count：${event.values[0]}")
                            continuation.resume(event.values[0].toLong(), {})
                        } else {
                            continuation.resume(-1, {})
                        }
                    }

                }, stepSensor, SensorManager.SENSOR_DELAY_NORMAL)
            } ?: continuation.resume(-2, {})
        }
    }
}