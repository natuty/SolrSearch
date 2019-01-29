package com.natuty.service.impl

import com.natuty.entity.Search
import com.natuty.entity.repository.SearchRepository
import com.natuty.service.ApiSearchServiceI
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
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


/*@Service
@Transactional
@ConfigurationProperties("scut.file")*/
@Service
@Transactional
class ApiSearchServiceImpl(
        val searchRepository: SearchRepository
) : ApiSearchServiceI {

    val log = LoggerFactory.getLogger(ApiSearchServiceImpl::class.java)
    private val IGNORED_CHARS_PATTERN = Pattern.compile("\\p{Punct}")

    override fun findByFilename(searchTerm: String, pageable: Pageable): Page<Search>? {
        if(StringUtils.isBlank(searchTerm)){
            //return searchRepository.findAll(pageable)
            return null
        }
        //return searchRepository.findByNameIn(splitSearchTermAndRemoveIgnoredCharacters(searchTerm), pageable)
        //return searchRepository.findByFilenameIn(splitSearchTermAndRemoveIgnoredCharacters(searchTerm), pageable)
        return null
    }

    override fun findById(id: String): Search {
        return searchRepository.findById(id).get()
    }

    override fun autocompleteFilenameFragment(fragment: String, pageable: Pageable): FacetPage<Search>? {
        if (StringUtils.isBlank(fragment)) {
            return SolrResultPage<Search>(emptyList())
        }
        //return searchRepository.findByFilenameStartsWith(splitSearchTermAndRemoveIgnoredCharacters(fragment), pageable);
        return null
    }

    fun splitSearchTermAndRemoveIgnoredCharacters(searchTerm: String): ArrayList<String> {
        val searchTerms = StringUtils.split(searchTerm, " ")
        val result = ArrayList<String>(searchTerms.size)
        for (term in searchTerms) {
            if (StringUtils.isNotEmpty(term)) {
                result.add(IGNORED_CHARS_PATTERN.matcher(term).replaceAll(" "))
            }
        }
        return result
    }

    override fun test(filename: String, page: Pageable): Any{
        return searchRepository.findByText(filename, page)
        return "null"
    }

}