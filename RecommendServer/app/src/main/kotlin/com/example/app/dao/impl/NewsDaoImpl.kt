package com.example.app.dao.impl

import com.example.app.dao.NewsDaoI
import com.example.app.entity.News
import com.example.app.entity.mysqlRepository.NewsRepository
import org.springframework.stereotype.Component

@Component
class NewsDaoImpl(
        val newsRepository: NewsRepository
):NewsDaoI{
    override fun findByName(name: String): List<News> {
        return listOf()
    }
}