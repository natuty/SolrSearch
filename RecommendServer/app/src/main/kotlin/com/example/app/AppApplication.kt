package com.example.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.data.solr.repository.config.EnableSolrRepositories
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication(scanBasePackages = arrayOf("com.example.app"))
@EnableSolrRepositories(basePackages = arrayOf( "com.example.app.entity.solrRepository"))
@EntityScan(basePackages = arrayOf( "com.example.app"))
@ServletComponentScan(basePackages = arrayOf("com.example.app"))
@EnableJpaRepositories(basePackages = arrayOf("com.example.app.entity.mysqlRepository"))
@EnableScheduling
class AppApplication

fun main(args: Array<String>) {
    runApplication<AppApplication>(*args)
}
