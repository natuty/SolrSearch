package com.example.app.service.impl


import com.example.app.controller.Type
import com.example.app.dao.ClickLogDaoI
import com.example.app.entity.ClickLog
import com.example.app.service.ClickLogServiceI
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class ClickLogServiceImpl(
        val clickLogDaoI: ClickLogDaoI
) : ClickLogServiceI {

    override fun get(id: Long): ClickLog {
        return clickLogDaoI.get(id = id)
    }

    override fun save(clickLog: ClickLog): ClickLog {
        return clickLogDaoI.save(clickLog = clickLog)
    }

    override fun findFirstByCompanyIdAndType(companyId: String, type: Type): ClickLog?{
        return clickLogDaoI.findFirstByCompanyIdAndType(companyId, type)
    }
}