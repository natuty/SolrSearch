package com.example.app.entity.solrRepository

import com.example.app.entity.Search
import org.springframework.data.domain.Page

import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.Highlight;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository


@Repository
interface SearchRepository: SolrCrudRepository<Search, String> {
    fun findByKeywords(keywords: String, page: Pageable): Page<Search>
    fun findByKeywordsAndType(keywords: String, type: String, page: Pageable): Page<Search>
    //fun findById()
}

