package com.example.app.dao

import com.example.app.controller.Type
import com.example.app.entity.Recommend
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface RecommendDaoI{
    fun findByName(name: String): List<Recommend>
    fun findByCompanyId(companyId: String, pageable: Pageable): Page<Recommend>
    fun findByCompanyIdAndType(companyId: String, type: Type, pageable: Pageable): Page<Recommend>

    fun findByCompanyIdAndMatchingGreaterThanEqual(companyId: String,matching: Float,pageable: Pageable): Page<Recommend>
    fun findByCompanyIdAndTypeAndMatchingGreaterThanEqual(companyId: String, type: Type,matching: Float, pageable: Pageable): Page<Recommend>


    fun save(recommend: Recommend): Recommend
    fun deleteByCompanyId(companyId: String)
    fun deleteByCompanyIdAndType(companyId: String, type: Type)

    fun deleteByType(type: Type)
}