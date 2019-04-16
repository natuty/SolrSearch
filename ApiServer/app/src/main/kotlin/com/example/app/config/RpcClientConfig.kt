package com.example.app.config

import com.example.app.service.DataClientServiceI
import com.example.app.service.ExampleClientApiI
import com.googlecode.jsonrpc4j.JsonRpcHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import java.util.HashMap
import java.net.URL
import com.googlecode.jsonrpc4j.ProxyUtil
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcClientProxyCreator


@Configuration
class RpcClientConfig(
        val env: Environment
){

    /*@Bean
    fun jsonRpcHttpClient(): JsonRpcHttpClient {
        val url = URL(env.getRequiredProperty("spring.rpcServer.endpoint"))
        val map = HashMap<String, String>()
        return JsonRpcHttpClient(url, map)
    }

    @Bean
    fun exampleClientApiI(jsonRpcHttpClient: JsonRpcHttpClient): ExampleClientApiI{
        return ProxyUtil.createClientProxy(javaClass.classLoader, ExampleClientApiI::class.java, jsonRpcHttpClient);
    }*/


    @Bean
    fun jsonRpcHttpClient(): JsonRpcHttpClient {
        val url = URL(env.getRequiredProperty("spring.rpcServer.endpoint"))
        val map = HashMap<String, String>()
        return JsonRpcHttpClient(url, map)
    }

    @Bean
    fun dataClientServiceI(jsonRpcHttpClient: JsonRpcHttpClient): DataClientServiceI{
        return ProxyUtil.createClientProxy(javaClass.classLoader, DataClientServiceI::class.java, jsonRpcHttpClient);
    }

}