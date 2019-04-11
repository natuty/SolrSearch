package com.example.app.dao.impl

import com.example.app.dao.TaskDaoI
import com.example.app.entity.Repository.TaskRepository
import com.example.app.entity.Task
import org.springframework.stereotype.Component

@Component
class TaskDaoImpl(
        val taskRepository: TaskRepository
):TaskDaoI{
    override fun get(id: Long): Task {
        return taskRepository.getOne(id)
    }

    override fun findByName(name: String): List<Task> {
        //return taskRepository.findByName(name = name)
        return listOf()
    }

    override fun save(task: Task): Task {
        return taskRepository.save(task)
    }
}