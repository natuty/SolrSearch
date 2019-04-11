package com.example.app.entity.repository

import com.example.app.entity.Search
import org.springframework.data.domain.Page

import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.HighlightPage
import org.springframework.data.solr.repository.Highlight;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository


@Repository
interface SearchRepository: SolrCrudRepository<Search, String> {
    @Highlight(prefix = "<b>", postfix = "</b>", fields = arrayOf("text"), fragsize=200)
    fun findByContent(text: String, page: Pageable): HighlightPage<Search>

    fun findByContentAndScoreGreaterThan(text: String, score: Float, page: Pageable): Page<Search>

/*    @Highlight(prefix = "<b>", postfix = "</b>", fields = arrayOf("text"), fragsize=200)
    fun findByTextAndType(text: String, type: String, page: Pageable): HighlightPage<Search>*/


   /* @Highlight(prefix = "<b>", postfix = "</b>", fields = arrayOf("text"), fragsize=200)
    fun findByFilename(filename: String, page: Pageable): HighlightPage<Search>

    @Highlight(prefix = "<b>", postfix = "</b>", fields = arrayOf("text"), fragsize=200)
    fun findByKeywords(keywords: String, page: Pageable): HighlightPage<Search>

    @Highlight(prefix = "<b>", postfix = "</b>", fields = arrayOf("text"), fragsize=200)
    fun findByFilenameAndText(filename: String, text: String, page: Pageable): HighlightPage<Search>*/
}

