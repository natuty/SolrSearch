package com.natuty.entity.repository

import com.natuty.entity.Search
import org.springframework.data.domain.Page

import com.natuty.entity.reportInterface.SearchInterface
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository
import org.springframework.data.solr.core.query.Query.Operator;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.repository.Facet;
import org.springframework.data.solr.repository.Highlight;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository


@Repository
interface SearchRepository: SolrCrudRepository<Search, String> {
    @Highlight(prefix = "<b>", postfix = "</b>", fields = arrayOf("text"), fragsize=200)
    fun findByText(text: String, page: Pageable): Page<Search>

    @Highlight(prefix = "<b>", postfix = "</b>", fields = arrayOf("text"), fragsize=200)
    fun findByFilename(filename: String, page: Pageable): Page<Search>

    @Highlight(prefix = "<b>", postfix = "</b>", fields = arrayOf("text"), fragsize=200)
    fun findByKeywords(keywords: String, page: Pageable): Page<Search>

    @Highlight(prefix = "<b>", postfix = "</b>", fields = arrayOf("text"), fragsize=200)
    fun findByFilenameAndText(filename: String, text: String, page: Pageable): Page<Search>
}

