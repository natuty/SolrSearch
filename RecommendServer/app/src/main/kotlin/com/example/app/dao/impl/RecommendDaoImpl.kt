package com.example.app.dao.impl

import com.example.app.controller.Type
import com.example.app.dao.RecommendDaoI
import com.example.app.dao.redisDefaultTimeout
import com.example.app.entity.Recommend
import com.example.app.entity.mysqlRepository.RecommendRepository
import com.example.app.helpers.jsonGet
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.TimeUnit

@Component
@Transactional
class RecommendDaoImpl(
        val recommendRepository: RecommendRepository,
        val stringRedisTemplate: StringRedisTemplate
):RecommendDaoI{

    /*var redisCacheTimeout= redisDefaultTimeout;
    val keyGroup = "entity:recommend:"
    fun getKey(companyId: String, pageNumber: Int, pageSize: Int) = "$keyGroup:$pageNumber:$pageSize"
    val valueOps by lazy {
        stringRedisTemplate.opsForValue()
    }*/


    override fun findByName(name: String): List<Recommend> {
        return listOf()
    }

    override fun findByCompanyId(companyId: String, pageable: Pageable): Page<Recommend> {
        return recommendRepository.findByCompanyId(companyId = companyId, pageable = pageable)
    }

    override fun findByCompanyIdAndType(companyId: String, type: Type, pageable: Pageable): Page<Recommend> {
        return recommendRepository.findByCompanyIdAndType(companyId = companyId, type = type, pageable = pageable)
    }


    override fun findByCompanyIdAndMatchingGreaterThanEqual(companyId: String,matching: Float, pageable: Pageable): Page<Recommend> {
        return recommendRepository.findByCompanyIdAndMatchingGreaterThanEqual(companyId = companyId, matching = matching, pageable = pageable)
    }

    override fun findByCompanyIdAndTypeAndMatchingGreaterThanEqual(companyId: String, type: Type,matching: Float, pageable: Pageable): Page<Recommend> {
        return recommendRepository.findByCompanyIdAndTypeAndMatchingGreaterThanEqual(companyId = companyId, type = type, matching = matching, pageable = pageable)
    }




    override fun save(recommend: Recommend): Recommend{
        return recommendRepository.save(recommend)
    }

    override fun deleteByCompanyId(companyId: String) {
        return recommendRepository.deleteByCompanyId(companyId = companyId)
    }

    override fun deleteByCompanyIdAndType(companyId: String, type: Type) {
        return recommendRepository.deleteByCompanyIdAndType(companyId = companyId, type = type)
    }

    override fun deleteByType(type: Type){
        return recommendRepository.deleteByType(type = type)
    }
}