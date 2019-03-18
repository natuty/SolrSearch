package com.example.app.service.impl

import com.example.app.service.DataServerServiceI
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
@AutoJsonRpcServiceImpl
class DataServerServiceImpl: DataServerServiceI{
    val log = LoggerFactory.getLogger(DataServerServiceImpl::class.java)

    override fun get_guide_text(guide_id: String): String {
        return "1231sd32f3将以下信息填入到body中2d1f23d1f"
    }

    override fun get_policy_text(guide_id: String): String {
        return "1231sd32f3将以下信息填入到body中2d1f23d1f"
    }

    override fun register(url: String, use: String, id: String): String {
        return "1231sd32f3将以下信息填入到body中2d1f23d1f"
    }
}