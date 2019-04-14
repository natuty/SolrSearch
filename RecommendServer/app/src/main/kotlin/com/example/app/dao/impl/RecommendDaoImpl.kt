package com.example.app.dao.impl

import com.example.app.dao.RecommendDaoI
import com.example.app.entity.Recommend
import com.example.app.entity.mysqlRepository.RecommendRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class RecommendDaoImpl(
        val recommendRepository: RecommendRepository
):RecommendDaoI{
    override fun findByName(name: String): List<Recommend> {
        return listOf()
    }

    override fun findByCompanyId(companyId: String, pageable: Pageable): Page<Recommend> {
        return recommendRepository.findByCompanyId(companyId = companyId, pageable = pageable)
    }

    override fun save(recommend: Recommend): Recommend{
        return recommendRepository.save(recommend)
    }

    override fun deleteByCompanyId(companyId: String) {
        return recommendRepository.deleteByCompanyId(companyId = companyId)
    }
}