package com.hongwei.remember_the_milk_api_sample.util

import android.text.format.DateFormat
import java.util.*

fun Calendar.toddMMyyyy_HHmmss(): String {
    return DateFormat.format("dd-MM-yyyy HH:mm:ss", this).toString()
}

fun Calendar.toddMMMyyyy(): String {
    return DateFormat.format("dd-MMM-yyyy", this).toString()
}