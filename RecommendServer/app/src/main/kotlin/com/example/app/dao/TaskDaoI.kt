package com.example.app.dao

import com.example.app.entity.Task

interface TaskDaoI{
    fun get(id: Long): Task

    fun findByName(name: String): List<Task>
    fun save(task: Task): Task

}