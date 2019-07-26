package com.hongwei.remember_the_milk_api_sample.domain.usecase

import android.util.Log
import com.hongwei.remember_the_milk_api_sample.ApiConfig.AppString.LIST_ALL_TASKS
import com.hongwei.remember_the_milk_api_sample.data.DataSource
import com.hongwei.remember_the_milk_api_sample.domain.model.DueTask
import com.hongwei.remember_the_milk_api_sample.util.now
import com.hongwei.remember_the_milk_api_sample.util.today
import com.hongwei.remember_the_milk_api_sample.util.tomorrow
import it.bova.rtmapi.RtmApi
import javax.inject.Inject

class GetTodayTasksUseCase @Inject constructor(val dataSource: DataSource) {
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
//            val tasksInList = api.tasksGetByList(list)
//            count += tasksInList.size
//            Log.i(TAG, "list: ${list.name}, size: ${tasksInList.size}")
//            Log.i(TAG, "list: ${list.name}")
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

                task.due?.let { taskDueDate ->
                    if (taskDueDate.after(today()) && taskDueDate.before(tomorrow())) {
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
}