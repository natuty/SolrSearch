package com.example.app.entity.mysqlRepository

import com.example.app.entity.Recommend
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface RecommendRepository: JpaRepository<Recommend, Long>, JpaSpecificationExecutor<Recommend> {
    fun findByCompanyId(companyId: String,pageable: Pageable): Page<Recommend>
    fun deleteByCompanyId(companyId: String)
}