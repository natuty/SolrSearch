package com.natuty.service

import com.natuty.entity.Search
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.solr.core.query.result.FacetPage

interface ApiSearchServiceI{
    fun findByFilename(searchTerm: String, pageable: Pageable): Page<Search>?
    fun findById(id: String): Search
    fun autocompleteFilenameFragment(fragment: String, pageable: Pageable): FacetPage<Search>?

    fun test(filename: String, page: Pageable): Any
}