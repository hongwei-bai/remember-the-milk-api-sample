package com.hongwei.remember_the_milk_api_sample.domain.usecase

import android.util.Log
import com.hongwei.remember_the_milk_api_sample.ApiConfig.AppString.LIST_ALL_TASKS
import com.hongwei.remember_the_milk_api_sample.data.DataSource
import com.hongwei.remember_the_milk_api_sample.domain.model.DueTask
import com.hongwei.remember_the_milk_api_sample.util.dayDiff
import com.hongwei.remember_the_milk_api_sample.util.monthDiff
import com.hongwei.remember_the_milk_api_sample.util.now
import com.hongwei.remember_the_milk_api_sample.util.tomorrow
import com.hongwei.remember_the_milk_api_sample.util.weekDiff
import com.hongwei.remember_the_milk_api_sample.util.yearDiff
import it.bova.rtmapi.Frequency.DAILY
import it.bova.rtmapi.Frequency.MONTHLY
import it.bova.rtmapi.Frequency.WEEKLY
import it.bova.rtmapi.Frequency.YEARLY
import it.bova.rtmapi.RtmApi
import it.bova.rtmapi.Task
import java.util.Date
import javax.inject.Inject

class GetDueTasksUseCase @Inject constructor(val dataSource: DataSource) {
    companion object {
        const val TAG = "rtm.due-task.usecase"
    }

    fun execute(progressNotify: (p: Float) -> Unit = {}): List<DueTask> {
        val authToken = dataSource.retriveToken()
        progressNotify.invoke(0.03f)
        Log.i(TAG, "authToken: $authToken")
        val api = RtmApi(dataSource.apiKey, dataSource.sharedSecret, authToken)
        progressNotify.invoke(0.06f)

        val lists = api.listsGetList()
        progressNotify.invoke(0.1f)
        val dueTaskList = mutableListOf<DueTask>()

        var i = 0
        for (list in lists) {
            val listProgress = 0.1f + 0.9f * i / lists.size
            val listProgressNt = 0.1f + 0.9f * (i + 1) / lists.size
            progressNotify.invoke(listProgress)

            if (list.isArchived || list.isDeleted || list.name == LIST_ALL_TASKS) {
                continue
            }

            val tasks = api.tasksGetByList(list)

            var j = 0
            for (task in tasks) {
                val taskProgress = listProgress + (listProgressNt - listProgress) * j / tasks.size
                progressNotify.invoke(taskProgress)

                if ((task.completed != null && task.completed < now())
                    || (task.deleted != null && task.deleted < now())
                ) {
                    continue
                }

                task.due?.apply {
                    if (task.recurrence != null) {
                        getRecurrenceDue(task)?.let { dueRecurrence ->
                            dueRecurrence.apply {
                                hours = 9
                                if (after(now())) {
                                    Log.i(TAG, "[RECURRENCE MOR]task: ${task.name}, due: ${this}")
                                    dueTaskList.add(DueTask(task.name, this))
                                }
                            }
                            dueRecurrence.apply {
                                hours = 19
                                if (after(now())) {
                                    Log.i(TAG, "[RECURRENCE MOR]task: ${task.name}, due: ${this}")
                                    dueTaskList.add(DueTask(task.name, this))
                                }
                            }
                        }
                    } else if (after(now()) && before(tomorrow())) {
                        Log.i(TAG, "[TODAY]task: ${task.name}, due: ${task.due}")
                        dueTaskList.add(DueTask(task.name, task.due))
                    }
                }
                j++
            }
            i++
        }
        Log.i(TAG, "-- end --")
        return dueTaskList
    }

    private fun getRecurrenceDue(task: Task): Date? {
        if (null == task.recurrence) {
            return null
        }

        if (!task.recurrence.isEvery) {
            return null
        }

        var isToday = false
        when (task.recurrence.frequency) {
            DAILY -> {
                if (now().dayDiff(task.due) % task.recurrence.interval == 0L) {
                    isToday = true
                }
            }
            WEEKLY -> {
                val weekDiff = now().weekDiff(task.due)
                if (weekDiff != null && weekDiff % task.recurrence.interval == 0) {
                    isToday = true
                }
            }
            MONTHLY -> {
                val monthDiff = now().monthDiff(task.due)
                if (monthDiff != null && monthDiff % task.recurrence.interval == 0) {
                    isToday = true
                }
            }
            YEARLY -> {
                val yearDiff = now().yearDiff(task.due)
                if (yearDiff != null && yearDiff % task.recurrence.interval == 0) {
                    isToday = true
                }
            }
            else -> {
            }
        }

        if (isToday) {
            return Date().apply {
                hours = task.due.hours
                minutes = task.due.minutes
                seconds = task.due.seconds
            }
        }
        return null
    }
}