package com.natuty.service

import com.natuty.entity.Search
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.solr.core.query.result.FacetPage
import org.springframework.data.solr.core.query.result.HighlightPage
import org.springframework.data.solr.core.query.result.SolrResultPage

interface ApiSearchServiceI{
    fun save(id: String, filename: String, text: String): Search?
    fun delete(id: String)
    fun get(id: String):Search?
    fun findByFilename(filename: String, page: Pageable): HighlightPage<Search>
    fun findByText(text: String, page: Pageable): HighlightPage<Search>
    fun findByKeywords(keywords: String, page: Pageable): HighlightPage<Search>
    fun findByFilenameAndText(filename: String, text: String, page: Pageable): HighlightPage<Search>
}