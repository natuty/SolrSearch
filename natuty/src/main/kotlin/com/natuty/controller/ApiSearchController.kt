package com.natuty.controller

import com.natuty.exception.BusinessException
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
import org.springframework.data.domain.PageRequest
import java.lang.Exception


@RestController
@RequestMapping("/api/apisearch")
class ApiSearchController(
        val apiSearchServiceI: ApiSearchServiceI
) {

    val log = LoggerFactory.getLogger(this.javaClass)

    /**
     * 添加索引
     */
    @RequestMapping("/addIndex")
    fun addIndex(id: String, filename: String, text: String): Any {
        var status = true
        val rt = hashMapOf(
                "status" to status
        )
        return rt
    }

    @RequestMapping("/test")
    fun test(id: String): Any {
        var status = true
        try {
            return apiSearchServiceI.findById(id)
        }catch (e: Exception){
            e.printStackTrace()
            status = false
        }
        val rt = hashMapOf(
                "status" to status
        )
        return rt
    }

    @RequestMapping("/test2")
    fun test2(filename: String): Any {
        var status = true
        try {
            return apiSearchServiceI.test(filename = filename, page = PageRequest(0,500))
        }catch (e: Exception){
            e.printStackTrace()
            status = false
        }
        val rt = hashMapOf(
                "status" to status
        )
        return rt
    }

}
