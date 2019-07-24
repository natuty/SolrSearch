package com.example.app.entity.mysqlRepository

import com.example.app.entity.News
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface NewsRepository: JpaRepository<News, Long>, JpaSpecificationExecutor<News> {
}