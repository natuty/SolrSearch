package com.example.app.service.impl


import com.example.app.dao.RecommendDaoI
import com.example.app.entity.Recommend
import com.example.app.service.RecommendServiceI
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class RecommendServiceImpl(
        val recommendDaoI: RecommendDaoI
) : RecommendServiceI {
    override fun findByName(name: String): List<Recommend> {
        return recommendDaoI.findByName(name = name)
    }

    override fun findByCompanyId(companyId: String, pageable: Pageable): Page<Recommend> {
        return recommendDaoI.findByCompanyId(companyId = companyId, pageable = pageable)
    }

    override fun deleteByCompanyId(companyId: String) {
        return recommendDaoI.deleteByCompanyId(companyId = companyId)
    }

    override fun save(recommend: Recommend): Recommend {
        return recommendDaoI.save(recommend)
    }

}