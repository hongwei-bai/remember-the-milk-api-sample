package com.hongwei.remember_the_milk_api_sample.domain.usecase

import android.util.Log
import com.hongwei.remember_the_milk_api_sample.ApiConfig.AppString.LIST_ALL_TASKS
import com.hongwei.remember_the_milk_api_sample.data.DataSource
import com.hongwei.remember_the_milk_api_sample.domain.model.DueTask
import it.bova.rtmapi.RtmApi
import java.util.*
import javax.inject.Inject

class GetTodayTasksUseCase @Inject constructor(val dataSource: DataSource) {
    companion object {
        const val TAG = "rtm.due-task.usecase"
    }

    fun execute(): List<DueTask> {
        val authToken = dataSource.retriveToken()
        Log.i(TAG, "authToken: $authToken")
        val api = RtmApi(dataSource.apiKey, dataSource.sharedSecret, authToken)

        val yesterday = Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24))
        val now = Date()
        val today = Date(now.year, now.month, now.date)
        val tomorrow0 = Date(today.time + (1000 * 60 * 60 * 24))

//        Log.i(TAG, "yesterday: $yesterday")
//        Log.i(TAG, "today: $today")
//        Log.i(TAG, "today0: $today0")
//        Log.i(TAG, "tomorrow0: $tomorrow0")

        val lists = api.listsGetList()
        val dueTaskList = mutableListOf<DueTask>()
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
                    if (taskDueDate.after(today) && taskDueDate.before(tomorrow0)) {
                        Log.i(TAG, "[TODAY]task: ${task.name}, due: ${task.due}")
                        dueTaskList.add(DueTask(task.name, task.due))
                    }
                }
            }
        }
        Log.i(TAG, "-- end --")
        return dueTaskList
    }
}