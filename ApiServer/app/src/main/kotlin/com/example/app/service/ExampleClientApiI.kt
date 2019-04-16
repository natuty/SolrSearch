package com.example.app.service

import com.googlecode.jsonrpc4j.JsonRpcParam



interface  ExampleClientApiI{
    fun multiplier(@JsonRpcParam(value = "a") a: Int, @JsonRpcParam(value = "b") b: Int): Int
}