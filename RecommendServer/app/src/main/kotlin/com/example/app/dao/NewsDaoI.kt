package com.example.app.dao

import com.example.app.entity.News

interface NewsDaoI{
    fun findByName(name: String): List<News>

}