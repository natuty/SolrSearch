package com.example.app.service.impl

import com.example.app.entity.Search
import com.example.app.entity.solrRepository.SearchRepository
import com.example.app.helpers.getFileText
import com.example.app.service.ApiSearchServiceI
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.transaction.annotation.Transactional
import java.io.File

@Service
@Transactional
class ApiSearchServiceImpl(
        val searchRepository: SearchRepository
) : ApiSearchServiceI {
    val log = LoggerFactory.getLogger(ApiSearchServiceImpl::class.java)

    override fun save(search: Search):Search? {
        val id = search.id
        val isExist = searchRepository.existsById(id)
        if(isExist){
            throw Exception("id already exists")
        }else{
            val search = searchRepository.save(search)
            return search
        }
    }

    override fun delete(id: String) {
        val isExist = searchRepository.existsById(id)
        if(isExist){
            searchRepository.deleteById(id)
        }else{
            throw Exception("delete failed")
        }
    }

    override fun findByText(text: String, page: Pageable): Page<Search> {
        return searchRepository.findByKeywords(text,page)
    }

    override fun findByKeywordsAndType(keywords: String, type: String, page: Pageable): Page<Search> {
        return searchRepository.findByKeywordsAndType(keywords, type, page)
    }


    override fun get(id: String): Search? {
        if(searchRepository.existsById(id)){
            return searchRepository.findById(id).get()
        }
        return null
    }

    //获取文件文本 pdf. doc. zip  rar  excel
    override fun getTextFromFile(file: File): String{
        return getFileText(file)
    }
}


