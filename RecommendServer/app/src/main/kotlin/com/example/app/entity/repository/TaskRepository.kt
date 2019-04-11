package com.example.app.entity.Repository

import com.example.app.entity.Task
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface TaskRepository: JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    //fun findByName(name: String): List<Task>
}