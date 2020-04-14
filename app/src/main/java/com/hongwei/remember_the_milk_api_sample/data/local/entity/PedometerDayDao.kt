package com.hongwei.remember_the_milk_api_sample.data.local.entity

import androidx.room.*

@Dao
interface PedometerDayDao {

    @Query("SELECT * FROM pedometerDay")
    fun getPedometerDays(): List<PedometerDay>

    @Query("SELECT * FROM pedometerDay WHERE dayDiffFrom1970= :dayDiff")
    fun getPedometerDay(dayDiff: Long): PedometerDay?

    @Insert
    fun insert(pedometerDay: PedometerDay)

    @Update
    fun update(pedometerDay: PedometerDay)

    @Delete
    fun delete(pedometerDay: PedometerDay)
}