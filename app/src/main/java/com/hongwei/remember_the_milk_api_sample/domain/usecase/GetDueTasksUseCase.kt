package com.hongwei.remember_the_milk_api_sample.domain.usecase

import android.util.Log
import com.hongwei.remember_the_milk_api_sample.ApiConfig.AppString.LIST_ALL_TASKS
import com.hongwei.remember_the_milk_api_sample.data.DataSource
import com.hongwei.remember_the_milk_api_sample.domain.model.DueTask
import com.hongwei.remember_the_milk_api_sample.util.now
import com.hongwei.remember_the_milk_api_sample.util.tomorrow
import it.bova.rtmapi.RtmApi
import javax.inject.Inject

class GetDueTasksUseCase @Inject constructor(val dataSource: DataSource) {
    companion object {
        const val TAG = "rtm.due-task.usecase"
    }

    fun execute(listener: (p: Float) -> Unit = {}): List<DueTask> {
        val authToken = dataSource.retriveToken()
        Log.i(TAG, "authToken: $authToken")
        val api = RtmApi(dataSource.apiKey, dataSource.sharedSecret, authToken)
        listener.invoke(0.1f)

        val lists = api.listsGetList()
        listener.invoke(0.2f)
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
                if ((task.completed != null && task.completed < now())
                        || (task.deleted != null && task.deleted < now())
                ) {
                    continue
                }

                task.due?.let { taskDueDate ->
                    if (taskDueDate.after(now()) && taskDueDate.before(tomorrow())) {
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