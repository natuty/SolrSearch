package com.example.app.service

import com.googlecode.jsonrpc4j.JsonRpcMethod

interface DataClientServiceI{
    //fun multiplier(@JsonRpcParam(value = "a") a: Int, @JsonRpcParam(value = "b") b: Int): Int
    @JsonRpcMethod("file.get_guide_text")
    fun get_guide_text(guide_id: String): String

    @JsonRpcMethod("file.get_policy_text")
    fun get_policy_text(guide_id: String): String

    @JsonRpcMethod("file.register")
    fun register(url: String, use: String, id: String): String
}