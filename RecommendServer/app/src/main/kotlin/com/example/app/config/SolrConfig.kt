package com.example.app.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.data.solr.repository.config.EnableSolrRepositories
import javax.annotation.Resource
import org.apache.solr.client.solrj.impl.HttpSolrClient
import java.net.MalformedURLException
import org.apache.solr.client.solrj.SolrClient
import org.slf4j.LoggerFactory
import org.springframework.data.solr.core.SolrTemplate
//import org.springframework.ws.soap.saaj.SaajSoapMessageFactory


@Configuration
@EnableSolrRepositories(basePackages = arrayOf("com.example.app.entity.repository"))
class SolrConfig(
        val env: Environment
){
    val logger = LoggerFactory.getLogger(this.javaClass)
    @Bean
    @Throws(MalformedURLException::class, IllegalStateException::class)
    fun solrClient(): SolrClient {
        //return HttpSolrClient.Builder(env.getRequiredProperty("spring.solr.host")).build()
        return HttpSolrClient(env.getRequiredProperty("spring.solr.host"))
    }

    @Bean
    fun solrTemplate(solrClient: SolrClient): SolrTemplate{
        return SolrTemplate(solrClient)
    }

/*    @Bean
    fun messageFactory():SaajSoapMessageFactory{
        return SaajSoapMessageFactory()
    }*/



}