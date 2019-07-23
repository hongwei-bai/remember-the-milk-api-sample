package com.hongwei.remember_the_milk_api_sample.domain

import android.util.Log
import com.hongwei.remember_the_milk_api_sample.ApiConfig.AppString.TAG_DOC
import com.hongwei.remember_the_milk_api_sample.data.DataSource
import it.bova.rtmapi.RtmApi
import java.util.*
import javax.inject.Inject

class GetDocTasksUseCase @Inject constructor(val dataSource: DataSource) {
    companion object {
        const val TAG = "rtm.doc-task.usecase"
    }

    fun execute(apiKey: String, sharedSecret: String) {
        val authToken = dataSource.retriveToken()
        Log.i(TAG, "authToken: $authToken")
        val api = RtmApi(apiKey, sharedSecret, authToken)

        val t0 = System.currentTimeMillis()
        val lists = api.listsGetList()
        Log.i(TAG, "listsGetList use: ${System.currentTimeMillis() - t0}")
        for (list in lists) {
//            val tasksInList = api.tasksGetByList(list)
//            count += tasksInList.size
//            Log.i(TAG, "list: ${list.name}, size: ${tasksInList.size}")

            if (list.name == TAG_DOC) {
                val t1 = System.currentTimeMillis()
                val tasks = api.tasksGetByList(list)
                Log.i(TAG, "tasksGetByList use: ${System.currentTimeMillis() - t1}")
                for (task in tasks) {
                    if (task.name == ">>>git opr") {
                        Log.i(TAG, "tags: ${Arrays.toString(task.tags)}")
//                        for (note in task.notes) {
//                            Log.i(TAG, "note: $note")
//                        }
                    }
                }
            }
        }

    }
}