package com.example.app.helpers

import ch.qos.logback.core.util.TimeUtil
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.data.redis.core.*
import java.math.BigDecimal
import java.util.concurrent.TimeUnit
import com.example.app.helpers.jsonStringToObject
import com.example.app.helpers.toJsonString

inline fun <reified T : Number> String.toNumberT(): T {
    return when (T::class) {
        Float::class -> toFloat() as T
        Double::class -> toDouble() as T
        Long::class -> toLong() as T
        Int::class -> toInt() as T
        Byte::class -> toByte() as T
        Short::class -> toShort() as T
        else -> 0 as T
    }
}

inline fun String.toBigDecimal(): BigDecimal {
    return BigDecimal(this)
}


inline fun writeJson(value: Any?): String {
    return value?.run { this.toJsonString() } ?: "null"
}

inline fun <reified T : Any> readJson(value: String?): T? {
    return value?.run { this.jsonStringToObject<T>() } ?: null
}

inline fun writeMapJson(value: Map<String, Any?>): Map<String, String?>? {
    return value?.mapValues { writeJson(it.value) }
}

inline fun ValueOperations<String, String?>.jsonSet(key: String, value: Any?) {
    set(key, writeJson(value))
}

inline fun ValueOperations<String, String?>.jsonSet(key: String, value: Any?, timeout: Long, unit: TimeUnit) {
    set(key, jacksonObjectMapper().writeValueAsString(value), timeout, unit)
}





inline fun <reified T : Any> ValueOperations<String, String?>.jsonGet(key: String): T? {
    return get(key)?.run {
        return this.jsonStringToObject()
    } ?: null
}

inline fun <reified T : Any> ValueOperations<String, String?>.jsonGetAndSet(key: String, value: T?): T? {
    return readJson(getAndSet(key, writeJson(value)))
}

inline fun <reified T : Any> HashOperations<String, String, String?>.jsonGet(key: String, hKey: String?): T? {
    if (hKey == null) return null
    return readJson(get(key, hKey))
}


inline fun <reified T : Any> HashOperations<String, String, String?>.jsonMultiGet(key: String, hKeys: Collection<String>): List<T?> {
    return multiGet(key, hKeys).map { readJson<T>(it) }
}

inline fun HashOperations<String, String, String?>.jsonPutAll(key: String, values: Map<String, Any?>) {
    putAll(key, values.mapValues { writeJson(it) })
}

inline fun HashOperations<String, String, String?>.jsonPut(key: String, hk: String, value: Any?) {
    put(key, hk, writeJson(value))
}

inline fun HashOperations<String, String, String?>.jsonPutIfAbsent(key: String, hKey: String, value: Any?) {
    putIfAbsent(key, hKey, writeJson(value))
}

inline fun <reified T : Any> HashOperations<String, String, String?>.jsonValues(key: String): List<T?> {
    return values(key).map { readJson<T>(it) }
}

inline fun <reified T : Any> HashOperations<String, String, String?>.jsonEntries(key: String): Map<String, T?> {
    return entries(key).mapValues { readJson<T>(it.value) }
}

inline fun ListOperations<String, String?>.jsonLetPush(key: String, value: Any?) {
    leftPush(key, writeJson(value))
}
inline fun ListOperations<String, String?>.jsonLeftPushAll(key:String,vararg values:Any?){
    leftPushAll(key,values.map { writeJson(it) })
}
inline fun ListOperations<String, String?>.jsonLeftPushAll(key:String,values:Collection<Any?>){
    leftPushAll(key,values.map { writeJson(it) })
}
inline fun ListOperations<String, String?>.jsonLeftPushIfPresent(key:String,value:Any?){
    leftPushIfPresent(key, writeJson(value))
}

inline fun ListOperations<String, String?>.jsonLeftPush(key:String,value:Any?){
    leftPush(key, writeJson(value))
}
inline fun ListOperations<String, String?>.jsonRightPush(key:String,value:Any?){
    rightPush(key, writeJson(value))
}
inline fun ListOperations<String, String?>.jsonRightPushAll(key:String,values:Collection<Any?>){
    rightPushAll(key,values.map { writeJson(it) })
}
inline fun ListOperations<String, String?>.jsonRightPushAll(key:String,vararg values:Any?){
    rightPushAll(key,values.map { writeJson(it) })
}
inline fun ListOperations<String, String?>.jsonRightPushIfPresent(key:String,value:Any?){
    rightPushIfPresent(key, writeJson(value))
}
inline fun ListOperations<String, String?>.jsonSet(key:String,index:Long,value:Any?){
    set(key, index,writeJson(value))
}
inline fun ListOperations<String, String?>.jsonRemove(key:String,count:Long,value:Any?){
    remove(key, count,writeJson(value))
}
inline fun <reified T:Any> ListOperations<String, String?>.jsonIndex(key:String,index:Long):T?{
    return readJson(index(key,index))
}
inline fun <reified T:Any> ListOperations<String, String?>.jsonLeftPop(key:String):T?{
    return readJson(leftPop(key))
}
inline fun <reified T:Any> ListOperations<String, String?>.jsonLeftPop(key:String,timeout: Long,unit: TimeUnit):T?{
    return readJson(leftPop(key,timeout,unit))
}
inline fun <reified T:Any> ListOperations<String, String?>.jsonRightPop(key:String):T?{
    return readJson(rightPop(key))
}
inline fun <reified T:Any> ListOperations<String, String?>.jsonRightPop(key:String,timeout: Long,unit: TimeUnit):T?{
    return readJson(rightPop(key,timeout,unit))
}
inline fun <reified T:Any> ListOperations<String, String?>.jsonRightPopAndLeftPush(sourceKey:String,destinationKey:String):T?{
    return readJson(rightPopAndLeftPush(sourceKey,destinationKey))
}
inline fun <reified T:Any> ListOperations<String, String?>.jsonRightPopAndLeftPush(sourceKey:String,destinationKey:String,timeout: Long,unit: TimeUnit):T?{
    return readJson(rightPopAndLeftPush(sourceKey,destinationKey,timeout,unit))
}


inline fun <reified T : Any> StringRedisTemplate.jsonGetValue(key: String): T? {
    return this.opsForValue().get(key)?.run {
        if (this == "null") return null
        if (this == "") {
            return if (T::class != String::class) {
                null
            } else {
                "" as T
            }
        }
        return jacksonObjectMapper().readValue(this)
    } ?: null
}

inline fun StringRedisTemplate.jsonSetValue(key: String, value: Any?) {
    opsForValue().set(key, jacksonObjectMapper().writeValueAsString(value))
}

inline fun <reified T : Any> StringRedisTemplate.jsonGetHashValue(key: String, hashKey: String): T? {
    return this.opsForHash<String, String>().get(key, hashKey)?.run {
        if (this == "null") return null
        if (this == "") {
            return if (T::class != String::class) {
                null
            } else {
                "" as T
            }
        }
        return jacksonObjectMapper().readValue(this)
    } ?: null
}

inline fun StringRedisTemplate.numberSetValue(key: String, value: Number) {
    this.opsForValue().set(key, value.toString())
}

inline fun <reified T : Number> StringRedisTemplate.numberGetValue(key: String): T? {
    return opsForValue().get(key)?.run {
        return when (T::class) {
            Float::class -> toFloat() as T
            Double::class -> toDouble() as T
            Long::class -> toLong() as T
            Int::class -> toInt() as T
            Byte::class -> toByte() as T
            Short::class -> toShort() as T
            else -> 0 as T
        }
    } ?: 0 as T
}

inline fun StringRedisTemplate.numberSetHashValue(key: String, hashKey: String, value: Number) {
    this.opsForHash<String, String>().put(key, hashKey, value.toString())
}