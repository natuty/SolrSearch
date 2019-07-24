package com.example.app.dao.impl

import com.example.app.controller.Type
import com.example.app.dao.ClickLogDaoI
import com.example.app.dao.redisDefaultTimeout
//import com.example.app.entity.Repository.ClickLogRepository
import com.example.app.entity.ClickLog
import com.example.app.entity.mysqlRepository.ClickLogRepository
import com.example.app.helpers.jsonGet
import com.example.app.helpers.jsonSet
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class ClickLogDaoImpl(
        val clickLogRepository: ClickLogRepository
):ClickLogDaoI{
    
    override fun get(id: Long): ClickLog {
        return clickLogRepository.getOne(id)
    }

    override fun save(clickLog: ClickLog): ClickLog {
        val clickLog = clickLogRepository.save(clickLog)
        return clickLog
    }

    override fun findFirstByCompanyIdAndType(companyId: String, type: Type): ClickLog?{
        return clickLogRepository.findFirstByCompanyIdAndType(companyId, type)
    }
}