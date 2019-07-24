package com.example.app.service

import com.example.app.entity.Search
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.solr.core.query.result.HighlightPage
import java.io.File

interface ApiSearchServiceI{
    fun save(search: Search): Search?
    fun delete(id: String)
    fun get(id: String):Search?
    fun findByText(text: String, page: Pageable): Page<Search>
    fun findByKeywordsAndType(keywords: String, type: String, page: Pageable): Page<Search>

    fun getTextFromFile(file: File): String

}