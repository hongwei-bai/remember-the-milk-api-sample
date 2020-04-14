package com.hongwei.remember_the_milk_api_sample.domain.todo.usecase

import android.util.Log
import com.hongwei.remember_the_milk_api_sample.ApiConfig.AppString.LIST_ALL_TASKS
import com.hongwei.remember_the_milk_api_sample.data.DataSource
import com.hongwei.remember_the_milk_api_sample.domain.model.entities.Task
import com.hongwei.remember_the_milk_api_sample.domain.model.mappers.TaskMapper
import com.hongwei.remember_the_milk_api_sample.util.now
import com.hongwei.remember_the_milk_api_sample.util.today
import it.bova.rtmapi.RtmApi
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(val dataSource: DataSource) {
    companion object {
        const val TAG = "rtm.task.usecase"
    }

    fun execute(progressNotify: (p: Float) -> Unit = {}): List<Task> {
        val authToken = dataSource.retriveToken()
        progressNotify.invoke(0.03f)
        Log.i(TAG, "authToken: $authToken")
        val api = RtmApi(dataSource.apiKey, dataSource.sharedSecret, authToken)
        progressNotify.invoke(0.06f)

        val lists = api.listsGetList()
        progressNotify.invoke(0.1f)
        val resultList = mutableListOf<Task>()

        val list2 = lists.filter { !it.isArchived && !it.isDeleted && it.name != LIST_ALL_TASKS }
        list2.forEachIndexed { i, list ->
            val listProgress = 0.1f + 0.9f * i / list2.size
            val listProgressNt = 0.1f + 0.9f * (i + 1) / list2.size
            progressNotify.invoke(listProgress)
            return api.tasksGetByList(list).filter {
                null == it.completed || null == it.deleted || it.completed > now() || it.deleted > now()
            }.filter {
                it.due?.after(today()) ?: false
            }.map {
                TaskMapper.map(it)
            }
        }

        Log.i(TAG, "-- end --")
        return resultList
    }
}