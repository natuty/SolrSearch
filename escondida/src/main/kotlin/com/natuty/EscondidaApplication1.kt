package com.natuty

import com.natuty.context.ContextFilter
import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.data.solr.repository.config.EnableSolrRepositories

@SpringBootApplication(scanBasePackages = arrayOf("com.natuty"))
@EnableSolrRepositories(basePackages = arrayOf( "com.natuty"))
@EntityScan(basePackages = arrayOf( "com.natuty"))
@ServletComponentScan(basePackages = arrayOf("com.natuty"))
class EscondidaApplication1 {
    private val logger = LoggerFactory.getLogger(this.javaClass)
}

fun main(args: Array<String>) {
    SpringApplication.run(EscondidaApplication1::class.java, *args)
}
