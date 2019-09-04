package com.hongwei.remember_the_milk_api_sample.util

import java.util.*

fun yesterday() = Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24))

fun now() = Date()

fun today() = Date().let { Date(it.year, it.month, it.date) }

fun tomorrow() = Date(today().time + (1000 * 60 * 60 * 24))

fun tomorrow(hour: Int) = tomorrow().let { Date(it.year, it.month, it.date, hour, 0, 0) }

private val MS_IN_DAY = 1000.0 * 3600 * 24

fun Date.dayDiff(ref: Date) = Math.round((this.apply {
    hours = 2
    minutes = 0
    seconds = 0
}.time - ref.apply {
    hours = 2
    minutes = 0
    seconds = 0
}.time) / MS_IN_DAY)

fun Date.weekDiff(ref: Date) = if (this.day == ref.day) {
    val days = this.dayDiff(ref)
    if (days % 7 == 0L) (days / 7).toInt() else null
} else null

fun Date.monthDiff(ref: Date) = if (this.date == ref.date) {
    (this.year - ref.year) * 12 + this.month - ref.month
} else null

fun Date.yearDiff(ref: Date) = if (this.date == ref.date && this.month == ref.month) {
    this.year - ref.year
} else null

fun Date.dayStart() = this.apply {
    hours = 0
    minutes = 0
    seconds = 0
}

fun Date.dayEnd() = this.apply {
    hours = 23
    minutes = 59
    seconds = 59
}
