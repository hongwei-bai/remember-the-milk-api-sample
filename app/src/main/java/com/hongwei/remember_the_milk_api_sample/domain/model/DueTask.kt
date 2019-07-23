package com.hongwei.remember_the_milk_api_sample.domain.model

import android.text.format.DateFormat
import java.util.*

class DueTask(
        val name: String,
        val due: Date
) {
    override fun toString(): String {
        val calendar = Calendar.getInstance()
        calendar.time = due

        return "$name due: ${DateFormat.format("dd-MM-yyyy HH:mm:ss", calendar)}"
    }
}