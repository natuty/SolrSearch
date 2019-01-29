package com.natuty.service

import com.natuty.entity.Search
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.solr.core.query.result.FacetPage

interface ApiSearchServiceI{
    fun save(id: String, filename: String, text: String): Search?
    fun delete(id: String)
    fun findByFilename(filename: String, page: Pageable): Page<Search>
    fun findByText(text: String, page: Pageable): Page<Search>
    fun findByKeywords(keywords: String, page: Pageable): Page<Search>
    fun findByFilenameAndText(filename: String, text: String, page: Pageable): Page<Search>
}