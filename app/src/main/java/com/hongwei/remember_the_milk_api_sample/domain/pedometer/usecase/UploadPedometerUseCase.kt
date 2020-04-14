package com.hongwei.remember_the_milk_api_sample.domain.pedometer.usecase

import android.util.Log
import com.hongwei.remember_the_milk_api_sample.data.PedometerDataSource
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class UploadPedometerUseCase @Inject constructor(val pedometerDataSource: PedometerDataSource) {
    companion object {
        private const val TAG = "rtm.pedo"
    }

    fun updateLocalSteps() {
        GlobalScope.launch {
            pedometerDataSource.test()

            val totalSteps = pedometerDataSource.getStepCountFromLastReboot()
            Log.i(TAG, "updateLocalSteps, totalSteps: $totalSteps")

            pedometerDataSource.saveStepCounter(totalSteps)
        }
    }

    fun doUpload() {
        GlobalScope.launch {
            val totalSteps = pedometerDataSource.getStepCountFromLastReboot()

            Log.i(TAG, "doUpload, totalSteps: $totalSteps")
        }


    }
}