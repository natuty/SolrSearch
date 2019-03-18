package com.example.app.client

import com.googlecode.jsonrpc4j.JsonRpcMethod
import com.googlecode.jsonrpc4j.JsonRpcService

@JsonRpcService("/data")
interface DataClientServiceI{

    @JsonRpcMethod("file.get_guide_text")
    fun get_guide_text(guide_id: String): String

    @JsonRpcMethod("file.get_policy_text")
    fun get_policy_text(policy_id: String): String

    @JsonRpcMethod("file.register")
    fun register(url: String, use: String, id: String): String
}