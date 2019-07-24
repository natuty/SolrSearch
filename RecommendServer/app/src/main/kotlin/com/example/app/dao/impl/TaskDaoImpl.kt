package com.example.app.dao.impl

import com.example.app.controller.Type
import com.example.app.dao.TaskDaoI
import com.example.app.dao.redisDefaultTimeout
//import com.example.app.entity.Repository.TaskRepository
import com.example.app.entity.Task
import com.example.app.entity.mysqlRepository.TaskRepository
import com.example.app.helpers.jsonGet
import com.example.app.helpers.jsonSet
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class TaskDaoImpl(
        val taskRepository: TaskRepository
):TaskDaoI{



    override fun get(id: Long): Task {
        return taskRepository.getOne(id)
    }

    override fun findByName(name: String): List<Task> {
        return listOf()
    }

    override fun save(task: Task): Task {
        val task = taskRepository.save(task)
        return task
    }
}