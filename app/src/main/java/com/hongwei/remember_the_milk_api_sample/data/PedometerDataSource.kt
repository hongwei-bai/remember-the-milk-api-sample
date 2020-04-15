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
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PedometerDataSource @Inject constructor(@AppContext private val context: Context) {
    companion object {
        private const val TAG = "rtm.pedo"

        const val UNINITIALIZED = -1L
    }

    private val pedometerDayDao: PedometerDayDao = AppDatabase.getInstance(context).pedometerDayDao()
    private var oneTimeSensorRequireListener: OneTimeSensorRequireListener? = null

    suspend fun test() {
        withContext(Dispatchers.IO) {
            val list = pedometerDayDao.getPedometerDays()
            var previous = 0L
            list.forEach {
                Log.d(TAG, "list all: $it, diff: ${it.steps - previous}")
                previous = it.steps
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

        withContext(Dispatchers.IO) {
            pedometerDayDao.getPedometerDay(dayDiff)?.let {
                pedometerDayDao.update(record.apply { id = it.id })
                Log.d(TAG, "saveStepCounter dayDiff: $dayDiff exists, update id: ${it.id}")
            } ?: pedometerDayDao.insert(record)
            Log.d(TAG, "saveStepCounter dayDiff: $dayDiff, insert steps: $steps")
        }
    }

    suspend fun getStepCountFromLastReboot(): Long {
        return suspendCancellableCoroutine { continuation ->
            val manager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            val stepSensor = manager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
            oneTimeSensorRequireListener = OneTimeSensorRequireListener(continuation) {
                manager.unregisterListener(oneTimeSensorRequireListener)
                oneTimeSensorRequireListener = null
            }
            stepSensor?.let {
                manager.registerListener(oneTimeSensorRequireListener, stepSensor, SensorManager.SENSOR_DELAY_NORMAL)
            } ?: continuation.resume(UNINITIALIZED, {})
        }

    }

    private class OneTimeSensorRequireListener(private val continuation: CancellableContinuation<Long>, private val unregisterAction: () -> Unit) : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
            Log.d(TAG, "onAccuracyChanged, sensor: $sensor, accuracy: $accuracy")
        }

        override fun onSensorChanged(event: SensorEvent) {
            Log.d(TAG, "onSensorChanged, event: $event")

            if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                Log.d(TAG, "onSensorChanged: current step count：${event.values[0]}")
                continuation.resume(event.values[0].toLong(), {})
                unregisterAction.invoke()
            }
        }
    }
}