package com.hongwei.remember_the_milk_api_sample.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "pedometerDay", indices = [Index(value = ["dayDiffFrom1970"], unique = true)])
data class PedometerDay(
        @PrimaryKey(autoGenerate = true) val id: Int,
        val dayDiffFrom1970: Long, // days from 1970-1-1
        val steps: Long
) {
    override fun toString() = Calendar.getInstance().apply {
        timeInMillis = dayDiffFrom1970 * 24 * 3600 * 1000
    }.let {
        "${it.get(Calendar.YEAR)}-${it.get(Calendar.MONTH) + 1}-${it.get(Calendar.DAY_OF_MONTH)}: $steps"
    }
}