package com.example.app.service

import com.example.app.entity.Task

interface TaskServiceI{
    fun get(id: Long): Task

    fun findByName(name: String): List<Task>

    fun save(task: Task): Task
}