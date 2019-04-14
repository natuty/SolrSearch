package com.example.app.controller

import com.example.app.entity.FileType
import com.example.app.service.ApiSearchServiceI
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SearchController(
        val apiSearchServiceI: ApiSearchServiceI
) {

    @RequestMapping("/search")
    fun searchIndex(keyword: String, fileType: FileType, current: Int?, pageSize: Int?): Any? {
        var cur = (current ?: 0) - 1
        var page = pageSize ?: 10
        if (cur < 0) cur = 0
        if (page < 0) page = 10
        else if (page > 560) page = 100

        val pageRequest = PageRequest(cur, page)

        val pageList = when(fileType){
            FileType.Guide -> apiSearchServiceI.findByTextAndType(text = keyword, type = Type.guide.name, page = pageRequest)
            FileType.Policy -> apiSearchServiceI.findByTextAndType(text = keyword, type = Type.policy.name, page = pageRequest)
            FileType.GuideOrPolicy -> apiSearchServiceI.findByText(text = keyword, page = pageRequest)
        }

        val tmpPageList = pageList
        val totalElements = pageList.totalElements
        val totalPages = pageList.totalPages

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

        var data =  pageList.content.sortedByDescending { it.score }.map {
            val abstract = highLightMap.get(it.id)?: ""
            hashMapOf(
                    "id" to it.id.substring(1),
                    "type" to it.type,
                    "abstract" to abstract,
                    "score" to it.score
            )
        }

        return hashMapOf(
                "data" to data,
                "totalElements" to totalElements,
                "totalPages" to totalPages
        )
    }
}