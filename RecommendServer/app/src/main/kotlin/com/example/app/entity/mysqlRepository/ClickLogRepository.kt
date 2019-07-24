package com.example.app.entity.mysqlRepository

import com.example.app.controller.Type
import com.example.app.entity.ClickLog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface ClickLogRepository: JpaRepository<ClickLog, Long>, JpaSpecificationExecutor<ClickLog> {
    fun findFirstByCompanyIdAndType(companyId: String, type: Type): ClickLog?
}