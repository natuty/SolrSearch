package com.example.app.entity.Repository

import com.example.app.entity.News
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface NewsRepository: JpaRepository<News, Long>, JpaSpecificationExecutor<News> {
    //fun findByName(name: String): List<News>
}