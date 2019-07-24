package com.example.app.helpers

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

inline fun <reified T : Any> String.jsonStringToObject(isIgnoreUnknow: Boolean = false): T? {
    if (this == "null") {
        return null
    }
    if (this == "") {
        if (T::class != String::class) {
            return null
        } else {
            return "" as T
        }
    }
    val mapper = jacksonObjectMapper()
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, !isIgnoreUnknow)
    return mapper.readValue(this)
}

inline fun <T> T.toJsonString(): String {
    return jacksonObjectMapper().writeValueAsString(this)
}