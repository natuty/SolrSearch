package com.example.app.service.impl

import com.example.app.entity.Search
import com.example.app.entity.solrRepository.SearchRepository
import com.example.app.service.ApiSearchServiceI
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.transaction.annotation.Transactional


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
            throw Exception()
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
            throw Exception()
        }
    }

    override fun findByText(text: String, page: Pageable): Page<Search> {
        return searchRepository.findByKeywords(text,page)
    }


    override fun get(id: String): Search? {
        if(searchRepository.existsById(id)){
            return searchRepository.findById(id).get()
        }
        return null
    }
}