package com.example.app.config

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImplExporter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RpcServerConfig{

    @Bean
    fun autoJsonRpcServiceImplExporter(): AutoJsonRpcServiceImplExporter {
        return AutoJsonRpcServiceImplExporter()
    }
}