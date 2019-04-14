package com.example.app.entity.mysqlRepository

import com.example.app.entity.Task
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository: JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
}