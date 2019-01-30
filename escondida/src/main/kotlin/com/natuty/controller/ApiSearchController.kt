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
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.lang.Exception


@RestController
@RequestMapping("/api/apisearch")
class ApiSearchController(
        val apiSearchServiceI: ApiSearchServiceI
) {

    // 1. 添加索引
    @RequestMapping("/addIndex")
    fun addIndex(id: String, filename: String, text: String): Any {
        var status = HttpStatus.OK
        try {
            apiSearchServiceI.save(id, filename, text)
        }catch (e: Exception){
            e.printStackTrace()
            status = HttpStatus.UNPROCESSABLE_ENTITY
        }

        return ResponseEntity("", status)
    }

    // 2. 删除索引
    @RequestMapping("/deleteIndex")
    fun deleteIndex(id: String): Any {
        var status = HttpStatus.OK
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

    // 3. 检索
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


    /**
     * 3. 检索
     */
    @RequestMapping("/test")
    fun searchIndex(id: String): Any {
        when(id){
            "1" -> return ResponseEntity(id, HttpStatus.OK)  //[GET]：服务器成功返回用户请求的数据 200
            "2" -> return ResponseEntity(id, HttpStatus.CREATED) //[POST/PUT/PATCH]：用户新建或修改数据成功 201
            "3" -> return ResponseEntity(id, HttpStatus.NO_CONTENT)  //[DELETE]：表示数据删除成功 204
            "4" -> return ResponseEntity(id, HttpStatus.BAD_REQUEST) //[POST/PUT/PATCH]：用户发出的请求有错误
            "5" -> return ResponseEntity(id, HttpStatus.NOT_FOUND) // [*]：用户发出的请求针对的是不存在的记录
            "6" -> return ResponseEntity(id, HttpStatus.NOT_ACCEPTABLE) // [*]：用户请求格式不可得
            "7" -> return ResponseEntity(id, HttpStatus.INTERNAL_SERVER_ERROR) // [*] ：服务器内部发生错误
            "8" -> return ResponseEntity(id, HttpStatus.UNPROCESSABLE_ENTITY) // [POST/PUT/PATCH]：当创建一个对象时，发生一个验证错误
            else -> return ResponseEntity(id, HttpStatus.NOT_ACCEPTABLE)
        }
    }
}
