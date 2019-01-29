package com.natuty.controller

import com.natuty.entity.Search
import com.natuty.exception.BusinessException
import com.natuty.entity.Type
import com.natuty.service.ApiSearchServiceI
import org.apache.solr.client.solrj.SolrQuery
import org.apache.solr.client.solrj.impl.HttpSolrClient
//import com.natuty.service.*
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam

import java.util.ArrayList
import org.springframework.boot.context.properties.ConfigurationProperties
import java.io.File
import org.springframework.web.multipart.MultipartFile
import org.slf4j.LoggerFactory
import java.util.regex.Pattern
import org.apache.solr.common.SolrInputDocument
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.solr.core.query.result.HighlightPage
import org.springframework.data.solr.core.query.result.SolrResultPage
import java.lang.Exception


@RestController
@RequestMapping("/api/apisearch")
class ApiSearchController(
        val apiSearchServiceI: ApiSearchServiceI
) {

    val log = LoggerFactory.getLogger(this.javaClass)

    /**
     * 1. 添加索引
     */
    @RequestMapping("/addIndex")
    fun addIndex(id: String, filename: String, text: String): Any {
        var status = true
        try {
            val search = apiSearchServiceI.save(id, filename, text)
        }catch (e: Exception){
            e.printStackTrace()
            status = false
        }

        val rt = hashMapOf(
                "status" to status
        )
        return rt
    }

    /**
     * 2. 删除索引
     */
    @RequestMapping("/deleteIndex")
    fun deleteIndex(id: String): Any {
        var status = true
        try {
            apiSearchServiceI.delete(id = id)
        }catch (e: Exception){
            e.printStackTrace()
            status = false
        }

        val rt = hashMapOf(
                "status" to status
        )
        return rt
    }

    /**
     * 3. 检索
     */
    @RequestMapping("/searchIndex")
    fun searchIndex(keyword: String, type:Type, current: Int? = 0, pageSize: Int? = 10): Any {
        var status = true
        var pageList: HighlightPage<Search>? = null

        var cur = (current ?: 0) - 1
        var page = pageSize ?: 10
        if (cur < 0) cur = 0
        if (page < 0) page = 10
        else if (page > 560) page = 100

        try {
            val page = PageRequest(cur, page)
            when(type){
                Type.Filename -> { pageList =  apiSearchServiceI.findByFilename(filename = keyword, page = page) }
                Type.Text -> { pageList =  apiSearchServiceI.findByText(text = keyword, page = page) }
                Type.FilenameOrText -> { pageList = apiSearchServiceI.findByKeywords(keywords = keyword, page = page)}
                Type.FilenameAndText -> { pageList = apiSearchServiceI.findByFilenameAndText(filename = keyword, text = keyword, page = page) }
            }
        }catch (e: Exception){
            e.printStackTrace()
            return hashMapOf("data" to "", "status" to false)
        }


        val highlighted = pageList.highlighted
        var highLightMap: HashMap<String,String> = hashMapOf()
        highlighted.forEach {
            var abstract = ""
            val entity = it.entity
            val id = entity.id
            val text = entity.text

            val highLights = it.highlights
            if(highLights.size > 0){
                val snipplets = highLights[0].snipplets
                if(snipplets.size > 0){
                    abstract = snipplets[0]
                }
            }

            if(abstract == ""){
                if(text.length > 200){
                    abstract = text.substring(0,200)
                }else{
                    abstract = text
                }
            }

            if(!highLightMap.containsKey(id)){
                highLightMap.put(id, abstract)
            }
        }

        val contentList = pageList.content.map {
            val abstract = highLightMap.get(it.id)?: ""
            hashMapOf(
                    "id" to it.id,
                    "filename" to it.filename,
                    "abstract" to abstract
            )
        }
        return hashMapOf(
                "data" to contentList,
                "status" to status
        )
    }
}
