package com.hongwei.remember_the_milk_api_sample.domain.model.mappers

import com.hongwei.remember_the_milk_api_sample.domain.model.DueTask
import it.bova.rtmapi.Task
import it.bova.rtmapi.Frequency
import java.util.Date

class RecurrenceTaskMapper {
    companion object {
        fun map(task: Task): DueTask? {
            if (task.due == null) {
                return null
            }

            if (task.recurrence == null) {
                return null
            }

            return task.recurrence?.let {
                when (it.frequency) {
                    Frequency.DAILY -> DueTask(task.name, Date())
                    Frequency.WEEKLY -> DueTask(task.name, Date())
                    Frequency.MONTHLY -> DueTask(task.name, Date())
                    Frequency.YEARLY -> DueTask(task.name, Date())
                    else -> null
                }
            }
        }

        private fun getDueDaily(task: Task) : Date? {
            return DueTask(task.name, Date().apply {
                val dueDate = Date(task.due.time)
                hours = dueDate.hours
            })
        }

    }
}