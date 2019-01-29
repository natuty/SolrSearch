package com.natuty.service.impl

import com.natuty.entity.Search
import com.natuty.entity.repository.SearchRepository
import com.natuty.exception.BusinessException
import com.natuty.service.ApiSearchServiceI
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.SolrResultPage;
import org.springframework.stereotype.Service;
import org.slf4j.LoggerFactory
import org.apache.commons.math3.stat.descriptive.summary.Product
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
            throw BusinessException("索引已存在")
        }else{
            val search = searchRepository.save(Search(filename = filename, text = text))
            return search
        }
    }

    override fun delete(id: String) {
        val isExist = searchRepository.existsById(id)
        if(!isExist){
            throw BusinessException("索引不存在")
        }else{
            searchRepository.deleteById(id)
        }
    }

    override fun findByFilename(filename: String, page: Pageable): Page<Search>  {
        return searchRepository.findByFilename(filename, page)
    }

    override fun findByText(text: String, page: Pageable): Page<Search> {
        return searchRepository.findByText(text, page)
    }

    override fun findByKeywords(keywords: String, page: Pageable): Page<Search> {
        return searchRepository.findByKeywords(keywords, page)
    }

    override fun findByFilenameAndText(filename: String, text: String, page: Pageable): Page<Search>{
        return searchRepository.findByFilenameAndText(filename = filename, text = text, page = page)
    }
}