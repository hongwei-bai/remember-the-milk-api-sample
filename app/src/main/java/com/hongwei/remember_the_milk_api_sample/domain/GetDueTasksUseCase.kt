package com.hongwei.remember_the_milk_api_sample.domain

import android.util.Log
import com.hongwei.remember_the_milk_api_sample.ApiConfig.AppString.LIST_ALL_TASKS
import com.hongwei.remember_the_milk_api_sample.data.DataSource
import it.bova.rtmapi.RtmApi
import java.util.*
import javax.inject.Inject

class GetDueTasksUseCase @Inject constructor(val dataSource: DataSource) {
    companion object {
        const val TAG = "rtm.due-task.usecase"
    }

    fun execute(apiKey: String, sharedSecret: String) {
        val authToken = dataSource.retriveToken()
        Log.i(TAG, "authToken: $authToken")
        val api = RtmApi(apiKey, sharedSecret, authToken)

        val yesterday = Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24))
        val today = Date()
        val today0 = Date(today.year, today.month, today.date)
        val tomorrow0 = Date(today0.time + (1000 * 60 * 60 * 24))

//        Log.i(TAG, "yesterday: $yesterday")
//        Log.i(TAG, "today: $today")
//        Log.i(TAG, "today0: $today0")
//        Log.i(TAG, "tomorrow0: $tomorrow0")

        val lists = api.listsGetList()
        for (list in lists) {
//            val tasksInList = api.tasksGetByList(list)
//            count += tasksInList.size
//            Log.i(TAG, "list: ${list.name}, size: ${tasksInList.size}")
//            Log.i(TAG, "list: ${list.name}")
            if (list.isArchived || list.isDeleted || list.name == LIST_ALL_TASKS) {
                continue
            }

            val tasks = api.tasksGetByList(list)

            for (task in tasks) {
                task.due?.let { taskDueDate ->
                    if (taskDueDate.after(tomorrow0)) {
                        Log.i(TAG, "task: ${task.name}, due: ${task.due}")
                    }

                    if (taskDueDate.after(today0) && taskDueDate.before(tomorrow0)) {
                        Log.i(TAG, "[TODAY]task: ${task.name}, due: ${task.due}")
                    }
                }
            }
        }
        Log.i(TAG, "-- end --")
    }
}