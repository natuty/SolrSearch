package com.natuty.controller

import com.natuty.entity.Search
import com.natuty.entity.Type
import com.natuty.exception.SearchIdIsNotExistException
import com.natuty.service.ApiSearchServiceI
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("api")
class ApiSearchController(
        val apiSearchServiceI: ApiSearchServiceI
) {

    // 1. 添加索引
    @RequestMapping(value = ["/index"], method = [RequestMethod.POST])
    fun  addIndex(search: Search):Any? {
        apiSearchServiceI.save(search)
        return hashMapOf("status" to true)
    }

    // 2. 删除索引
    @RequestMapping(value = ["/index/{id:.+}"], method = [RequestMethod.DELETE])
    fun deleteIndex(@PathVariable("id")id: String):Any? {

        apiSearchServiceI.delete(id = id)
        return hashMapOf("status" to true)
    }

    // 3. 检索
    @RequestMapping(value = ["/index"], method = [RequestMethod.PATCH])
    fun searchIndex(keyword: String, type:Type, current: Int? = 0, pageSize: Int? = 10): Any? {

        var cur = (current ?: 0) - 1
        var page = pageSize ?: 10
        if (cur < 0) cur = 0
        if (page < 0) page = 10
        else if (page > 560) page = 100

        val pageRequest = PageRequest(cur, page)
        val pageList = when(type){
            Type.Filename -> apiSearchServiceI.findByFilename(filename = keyword, page = pageRequest)
            Type.Text -> apiSearchServiceI.findByText(text = keyword, page = pageRequest)
            Type.FilenameOrText -> apiSearchServiceI.findByKeywords(keywords = keyword, page = pageRequest)
            Type.FilenameAndText -> apiSearchServiceI.findByFilenameAndText(filename = keyword, text = keyword, page = pageRequest)
        }

        var highLightMap: HashMap<String,String> = hashMapOf()
        pageList.highlighted.forEach {
            var abstract = ""
            val id = it.entity.id
            val text = it.entity.text
            val highLights = it.highlights
            if(highLights.size > 0){
                val snipplets = highLights[0].snipplets
                if(snipplets.size > 0){
                    abstract = snipplets[0]
                }
            }

            if(abstract == ""){
                abstract = if(text.length > 200) text.substring(0,200) else text
            }

            if(!highLightMap.containsKey(id)){
                highLightMap.put(id, abstract)
            }
        }

        return pageList.content.map {
            val abstract = highLightMap.get(it.id)?: ""
            hashMapOf(
                    "id" to it.id,
                    "filename" to it.filename,
                    "abstract" to abstract
            )
        }
    }

    // 4. 获取索引
    @RequestMapping(value = ["/index/{id:.+}"], method = [RequestMethod.GET])
    fun get(@PathVariable("id")id: String):Any? {
        return apiSearchServiceI.get(id = id)?: throw SearchIdIsNotExistException()
    }
}
