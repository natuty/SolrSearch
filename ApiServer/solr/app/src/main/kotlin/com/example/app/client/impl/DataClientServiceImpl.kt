package com.example.app.client.impl

import com.example.app.service.DataClientServiceI
import com.googlecode.jsonrpc4j.JsonRpcClient
import com.googlecode.jsonrpc4j.JsonRpcHttpClient
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcClientProxyCreator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DataClientServiceImpl(
        val dataClientServiceI: DataClientServiceI
){

    fun get_guide_text(guide_id: String): String{
        return dataClientServiceI.get_guide_text(guide_id)
    }

    fun get_policy_text(policy_id: String): String{
        return dataClientServiceI.get_policy_text(policy_id)
    }

    fun register(url: String, use: String, id: String): String{
        return dataClientServiceI.register(url, use, id)
    }
}