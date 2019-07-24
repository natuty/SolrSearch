package com.example.app.entity.mysqlRepository

import com.example.app.controller.Type
import com.example.app.entity.Recommend
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface RecommendRepository: JpaRepository<Recommend, Long>, JpaSpecificationExecutor<Recommend> {
    fun findByCompanyId(companyId: String,pageable: Pageable): Page<Recommend>
    fun findByCompanyIdAndType(companyId: String, type: Type, pageable: Pageable): Page<Recommend>

    fun findByCompanyIdAndMatchingGreaterThanEqual(companyId: String,matching: Float,pageable: Pageable): Page<Recommend>
    fun findByCompanyIdAndTypeAndMatchingGreaterThanEqual(companyId: String, type: Type,matching: Float, pageable: Pageable): Page<Recommend>

    fun deleteByCompanyId(companyId: String)
    fun deleteByCompanyIdAndType(companyId: String, type: Type)

    fun deleteByType(type: Type)
}