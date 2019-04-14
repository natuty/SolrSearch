package com.example.app.entity

import com.example.app.entity.reportInterface.SearchInterface
import com.example.app.entity.reportInterface.SearchInterface.Companion.TEXT_FIELD_TYPE
import com.example.app.entity.reportInterface.SearchInterface.Companion.TEXT_FIELD_NAME
import com.example.app.entity.reportInterface.SearchInterface.Companion.ID_FIELD_NAME
import com.example.app.entity.reportInterface.SearchInterface.Companion.TEXT_FIELD_KEYWORDS
import org.apache.solr.client.solrj.beans.Field
import org.springframework.data.annotation.Id
import org.springframework.data.solr.core.mapping.Indexed
import org.springframework.data.solr.core.mapping.SolrDocument
import org.springframework.data.solr.repository.Score

/**
 * @author cbf
 */
@SolrDocument(solrCoreName = "mycore")
data class Search(
        @Id
        @Indexed(ID_FIELD_NAME)
        var id: String,

        @Indexed(TEXT_FIELD_NAME)
        var title: String,

        @Indexed(TEXT_FIELD_TYPE)
        var content: String,

        @Indexed(TEXT_FIELD_KEYWORDS)
        var keywords: String?="",

        @Score
        var score: Float = 0.0f

): SearchInterface

enum class FileType(val remark: String) {
        Guide("指南文件"),
        Policy("政策文件"),
        GuideOrPolicy("指南文件or政策文件")
}