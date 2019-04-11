package com.example.app.service.impl


import com.example.app.dao.NewsDaoI
import com.example.app.entity.News
import com.example.app.service.ApiSearchServiceI
import com.example.app.service.NewsServiceI
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class NewsServiceImpl(
        val newsDaoI: NewsDaoI
) : NewsServiceI {
    override fun findByName(name: String): List<News> {
        return newsDaoI.findByName(name = name)
    }
}