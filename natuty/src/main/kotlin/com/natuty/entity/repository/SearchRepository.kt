package com.natuty.entity.repository

import com.natuty.entity.Search
import org.springframework.data.domain.Page

import com.natuty.entity.reportInterface.SearchInterface
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository
import org.springframework.data.solr.core.query.Query.Operator;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.repository.Facet;
import org.springframework.data.solr.repository.Highlight;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository


@Repository
interface SearchRepository: SolrCrudRepository<Search, String> {

    fun findByText(text: String, page: Pageable): Page<Search>

    /*@Query(value = "*:*")
    fun findAllFacetOnName(page: Pageable):FacetPage<Search>*/


    /*@Highlight(prefix = "<b>", postfix = "</b>")
    fun findByText(text: String): HighlightPage<Search>*/

    /*fun findByFilename(filename: String, page: Pageable): Page<Search>

    fun findByFilenameLike(filename: String): List<Search>*/


    //fun findByFilenameStartingWith(filename: String): List<Search>
    //fun findByFilenameIn(filename: Collection<String>, page: Pageable): HighlightPage<Search>
    //fun findByFilenameStartsWith(filenameFragments: Collection<String>, pagebale: Pageable): FacetPage<Search>



    /*@Highlight(prefix = "<b>", postfix = "</b>")
    @Query(fields = arrayOf(SearchInterface.Companion.ID_FIELD_NAME,
            SearchInterface.Companion.FILENAME_FIELD_NAME,
            SearchInterface.Companion.TEXT_FIELD_NAME
            ), defaultOperator = Operator.AND)
    fun findByNameIn(names: Collection<String>, page: Pageable): HighlightPage<Search>

    @Facet(fields = arrayOf(SearchInterface.Companion.FILENAME_FIELD_NAME))
    fun findByFilenameStartsWith(filenameFragments: Collection<String>, pagebale: Pageable): FacetPage<Search>*/
}

