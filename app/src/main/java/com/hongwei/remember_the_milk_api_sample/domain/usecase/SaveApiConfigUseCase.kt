package com.hongwei.remember_the_milk_api_sample.domain.usecase

import android.util.Log
import com.hongwei.remember_the_milk_api_sample.data.DataSource
import javax.inject.Inject

class SaveApiConfigUseCase @Inject constructor(val dataSource: DataSource) {
    fun execute(apiKey: String, sharedSecret: String) {
        Log.i("aaaa", "dataSource: $dataSource")
        dataSource.saveApiKey(apiKey)
        dataSource.saveSharedSecret(sharedSecret)
    }
}