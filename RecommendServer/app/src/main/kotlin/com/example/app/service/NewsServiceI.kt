package com.example.app.service

import com.example.app.entity.News
import org.springframework.data.domain.Pageable

interface NewsServiceI{
    fun findByName(name: String): List<News>
}