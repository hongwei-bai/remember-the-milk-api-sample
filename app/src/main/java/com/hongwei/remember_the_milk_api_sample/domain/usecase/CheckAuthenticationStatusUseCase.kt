package com.hongwei.remember_the_milk_api_sample.domain.usecase

import com.hongwei.remember_the_milk_api_sample.data.DataSource
import javax.inject.Inject

class CheckAuthenticationStatusUseCase @Inject constructor(val dataSource: DataSource) {
    companion object {
        const val TAG = "rtm.check-auth.usecase"
    }

    fun execute(): Boolean {
        return dataSource.retriveToken() != null
    }
}