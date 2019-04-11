package com.example.app.service.impl


import com.example.app.dao.TaskDaoI
import com.example.app.entity.Task
import com.example.app.service.TaskServiceI
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class TaskServiceImpl(
        val taskDaoI: TaskDaoI
) : TaskServiceI {

    override fun get(id: Long): Task {
        return taskDaoI.get(id = id)
    }

    override fun findByName(name: String): List<Task> {
        return taskDaoI.findByName(name = name)
    }

    override fun save(task: Task): Task {
        return taskDaoI.save(task = task)
    }
}