package com.example.app.dao

import com.example.app.entity.Recommend
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface RecommendDaoI{
    fun findByName(name: String): List<Recommend>

    fun findByCompanyId(companyId: String, pageable: Pageable): Page<Recommend>
    fun save(recommend: Recommend): Recommend
    fun deleteByCompanyId(companyId: String)
}