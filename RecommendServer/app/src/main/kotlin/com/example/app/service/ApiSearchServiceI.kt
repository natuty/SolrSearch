package com.example.app.service

import com.example.app.entity.Search
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.solr.core.query.result.HighlightPage

interface ApiSearchServiceI{
    fun save(search: Search): Search?
    fun delete(id: String)
    fun get(id: String):Search?
    fun findByText(text: String, page: Pageable): HighlightPage<Search>
    fun findByContentAndScoreGreaterThan(text: String, score: Float, page: Pageable): Page<Search>
    //fun findByTextAndType(text: String, type: String, page: Pageable): HighlightPage<Search>

    /*fun findByFilename(filename: String, page: Pageable): HighlightPage<Search>
    fun findByKeywords(keywords: String, page: Pageable): HighlightPage<Search>
    fun findByFilenameAndText(filename: String, text: String, page: Pageable): HighlightPage<Search>*/
}