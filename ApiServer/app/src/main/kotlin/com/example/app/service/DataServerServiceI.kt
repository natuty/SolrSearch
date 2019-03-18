package com.example.app.service

import com.googlecode.jsonrpc4j.JsonRpcMethod
import com.googlecode.jsonrpc4j.JsonRpcService

@JsonRpcService("/data_test")
interface DataServerServiceI{

    @JsonRpcMethod("file.get_guide_text")
    fun get_guide_text(guide_id: String): String

    @JsonRpcMethod("file.get_policy_text")
    fun get_policy_text(guide_id: String): String

    @JsonRpcMethod("file.register")
    fun register(url: String, use: String, id: String): String
}