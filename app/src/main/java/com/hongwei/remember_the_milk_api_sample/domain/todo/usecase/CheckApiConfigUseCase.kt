package com.hongwei.remember_the_milk_api_sample.domain.todo.usecase

import com.hongwei.remember_the_milk_api_sample.data.DataSource
import javax.inject.Inject

class CheckApiConfigUseCase @Inject constructor(val dataSource: DataSource) {
    fun execute(): Boolean {
        return dataSource.retriveApiKey() != null && dataSource.retriveSharedSecret() != null
    }
}