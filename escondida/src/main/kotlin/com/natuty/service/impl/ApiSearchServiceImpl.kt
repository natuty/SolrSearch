package com.natuty.service.impl

import com.natuty.entity.Search
import com.natuty.entity.repository.SearchRepository
import com.natuty.exception.SearchIdIsExistException
import com.natuty.exception.SearchIdIsNotExistException
import com.natuty.service.ApiSearchServiceI
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.slf4j.LoggerFactory
import org.springframework.data.solr.core.query.result.HighlightPage
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class ApiSearchServiceImpl(
        val searchRepository: SearchRepository
) : ApiSearchServiceI {
    val log = LoggerFactory.getLogger(ApiSearchServiceImpl::class.java)

    override fun save(id: String, filename: String, text: String):Search? {
        val isExist = searchRepository.existsById(id)
        if(isExist){
            throw SearchIdIsExistException()
        }else{
            val search = searchRepository.save(Search(id = id, filename = filename, text = text))
            return search
        }
    }

    override fun delete(id: String) {
        val isExist = searchRepository.existsById(id)
        if(isExist){
            searchRepository.deleteById(id)
        }else{
            throw SearchIdIsNotExistException()
        }
    }

    override fun findByFilename(filename: String, page: Pageable): HighlightPage<Search>  {
        return searchRepository.findByFilename(filename, page)
    }

    override fun findByText(text: String, page: Pageable): HighlightPage<Search> {
        return searchRepository.findByText(text, page)
    }

    override fun findByKeywords(keywords: String, page: Pageable): HighlightPage<Search> {
        return searchRepository.findByKeywords(keywords, page)
    }

    override fun findByFilenameAndText(filename: String, text: String, page: Pageable): HighlightPage<Search> {
        return searchRepository.findByFilenameAndText(filename = filename, text = text, page = page)
    }

    override fun get(id: String): Search? {
        if(searchRepository.existsById(id)){
            return searchRepository.findById(id).get()
        }
        return null
    }
}