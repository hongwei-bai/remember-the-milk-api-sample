package com.hongwei.remember_the_milk_api_sample.util

import java.util.*

fun yesterday() = Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24))

fun now() = Date()

fun today() = Date().let { Date(it.year, it.month, it.date) }

fun tomorrow() = Date(today().time + (1000 * 60 * 60 * 24))

fun tomorrow(hour: Int) = tomorrow().let { Date(it.year, it.month, it.date, hour, 0, 0) }

