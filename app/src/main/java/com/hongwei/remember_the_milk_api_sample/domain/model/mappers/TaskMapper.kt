package com.hongwei.remember_the_milk_api_sample.domain.model.mappers

import it.bova.rtmapi.Priority
import com.hongwei.remember_the_milk_api_sample.domain.model.entities.Priority as PriorityLocal
import it.bova.rtmapi.Task
import com.hongwei.remember_the_milk_api_sample.domain.model.entities.Task as TaskLocal

class TaskMapper {
    companion object {
        fun map(task: Task): TaskLocal {
            return TaskLocal(
                name = task.name,
                priority = mapPriority(task.priority),
                tags = task.tags.toList(),
                due = task.due,
                created = task.created,
                deleted = task.deleted,
                completed = task.completed
            )
        }

        private fun mapPriority(priority: Priority) = when (priority) {
            Priority.NONE -> PriorityLocal.NONE
            Priority.LOW -> PriorityLocal.LOW
            Priority.MEDIUM -> PriorityLocal.MEDIUM
            Priority.HIGH -> PriorityLocal.HIGH
        }
    }
}