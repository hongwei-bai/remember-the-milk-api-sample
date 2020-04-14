package com.hongwei.remember_the_milk_api_sample.domain.todo.usecase

import com.hongwei.remember_the_milk_api_sample.ApiConfig
import javax.inject.Inject

class RegisterUseCase @Inject constructor() {
    fun getRegisterUrl(): String {
        return ApiConfig.Urls.NON_COMMERCIAL_KEY_APPLY
    }
}