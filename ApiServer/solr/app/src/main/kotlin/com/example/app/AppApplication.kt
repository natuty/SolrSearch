package com.example.app

import com.googlecode.jsonrpc4j.JsonRpcService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.data.solr.repository.config.EnableSolrRepositories


@SpringBootApplication(scanBasePackages = arrayOf("com.example.app"))
@EnableSolrRepositories(basePackages = arrayOf( "com.example.app"))
@EntityScan(basePackages = arrayOf( "com.example.app"))
@ServletComponentScan(basePackages = arrayOf("com.example.app"))
@JsonRpcService("com.example.app")
class AppApplication

fun main(args: Array<String>) {
    runApplication<AppApplication>(*args)
}
