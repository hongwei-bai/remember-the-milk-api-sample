package com.hongwei.remember_the_milk_api_sample.domain.model

import com.hongwei.remember_the_milk_api_sample.util.toddMMyyyy_HHmmss
import java.util.*

class DueTask(
        val name: String,
        val due: Date
) {
    override fun toString(): String {
        val calendar = Calendar.getInstance()
        calendar.time = due

        return "$name due: ${calendar.toddMMyyyy_HHmmss()}"
    }
}