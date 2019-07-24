package com.example.app.service

import com.example.app.controller.Type
import com.example.app.entity.ClickLog

interface ClickLogServiceI{
    fun get(id: Long): ClickLog
    fun save(clickLog: ClickLog): ClickLog
    fun findFirstByCompanyIdAndType(companyId: String, type: Type): ClickLog?
}