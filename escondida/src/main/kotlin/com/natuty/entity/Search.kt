package com.natuty.entity

import com.natuty.entity.reportInterface.SearchInterface
import com.natuty.entity.reportInterface.SearchInterface.Companion.FILENAME_FIELD_NAME
import com.natuty.entity.reportInterface.SearchInterface.Companion.TEXT_FIELD_NAME
import com.natuty.entity.reportInterface.SearchInterface.Companion.ID_FIELD_NAME
import com.natuty.entity.reportInterface.SearchInterface.Companion.KEYWORDS_FIELD_NAME
import org.apache.solr.client.solrj.beans.Field
import org.springframework.data.annotation.Id
import org.springframework.data.solr.core.mapping.Indexed
import org.springframework.data.solr.core.mapping.SolrDocument

/**
 * @author cbf
 */
@SolrDocument(solrCoreName = "core")
data class Search(
        @Id
        @Indexed(ID_FIELD_NAME)
        var id: String,

        @Indexed(FILENAME_FIELD_NAME)
        var filename: String,

        @Indexed(TEXT_FIELD_NAME)
        var text: String,

        @Indexed(KEYWORDS_FIELD_NAME)
        var keywords: String = ""

): SearchInterface


enum class Type(val remark: String) {
        FilenameOrText("文件名or内容"),
        FilenameAndText("文件名and内容"),
        Filename("文件名"),
        Text("内容"),
}