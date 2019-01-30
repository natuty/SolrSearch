package com.natuty.entity.repository

import com.natuty.entity.Search
import org.springframework.data.domain.Page

import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.HighlightPage
import org.springframework.data.solr.repository.Highlight;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository


@Repository
interface SearchRepository: SolrCrudRepository<Search, String> {
    @Highlight(prefix = "", postfix = "", fields = arrayOf("text"), fragsize=200)
    fun findByText(text: String, page: Pageable): HighlightPage<Search>

    @Highlight(prefix = "", postfix = "", fields = arrayOf("text"), fragsize=200)
    fun findByFilename(filename: String, page: Pageable): HighlightPage<Search>

    @Highlight(prefix = "", postfix = "", fields = arrayOf("text"), fragsize=200)
    fun findByKeywords(keywords: String, page: Pageable): HighlightPage<Search>

    @Highlight(prefix = "", postfix = "", fields = arrayOf("text"), fragsize=200)
    fun findByFilenameAndText(filename: String, text: String, page: Pageable): HighlightPage<Search>
}

