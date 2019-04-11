package com.example.app.helpers

import org.springframework.core.io.Resource
import org.springframework.util.FileCopyUtils
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.Charset
import java.security.KeyStore
import java.security.SecureRandom
import javax.net.ssl.*

typealias HttpParams = List<Pair<String, Any>>
typealias HttpHeads = List<Pair<String, Any>>
typealias HttpBody = String
typealias HttpUrl = String
val timeout = 1000 * 60
fun HttpUrl.webGet(timeOut: Int = timeout, head: HttpHeads = arrayListOf(), charset: Charset = Charset.forName("utf-8")): String {
    var con = URL(this).openConnection() as HttpURLConnection
    try {
        con.readTimeout = timeOut
        con.requestMethod = "GET"
        head.forEach { con.addRequestProperty(it.first, it.second.toString()) }
        return con.inputStream.readBytes().toString(charset)
    } finally {
        con.disconnect()
    }
}

inline fun String.encodeURL(charSet: String = "utf-8"): String {
    return URLEncoder.encode(this, charSet)
}

fun HttpParams.getFromURL(url: String, head: HttpHeads = arrayListOf(), timeOut: Int = timeout, charset: Charset = Charset.forName("utf-8"),
                          sslSocketFactory: SSLSocketFactory? = null
): String {
    var con = URL("$url?${this.urlParam()}".encodeURL()).openConnection() as HttpURLConnection
    try {
        if (sslSocketFactory != null) {
            con = con as HttpsURLConnection
            con.sslSocketFactory = sslSocketFactory
        }
        con.readTimeout = timeOut
        con.requestMethod = "GET"
        head.forEach { con.addRequestProperty(it.first, it.second.toString()) }
        return con.inputStream.readBytes().toString(charset)
    } finally {
        con.disconnect()
    }
}

fun HttpParams.urlParam(): String {
    return this.joinToString("&") {
        "${it.first}=${it.second.toString().encodeURL()}"
    }
}

fun HttpParams.postToURL(url: String, head: HttpHeads = arrayListOf(), timeOut: Int = timeout,
                         charset: Charset = Charset.forName("utf-8"),
                         sslSocketFactory: SSLSocketFactory? = null
): String {

    var con = URL("$url").openConnection() as HttpURLConnection
    try {
        if (sslSocketFactory != null) {
            con = con as HttpsURLConnection
            con.sslSocketFactory = sslSocketFactory
        }
        con.readTimeout = timeOut
        con.requestMethod = "POST"
        con.doInput = true
        con.doOutput = true
        head.forEach { con.addRequestProperty(it.first, it.second.toString()) }
        con.outputStream.write(this.urlParam().toByteArray(charset))
        return con.inputStream.readBytes().toString(charset)
    } finally {
        con.disconnect()
    }
}

fun HttpBody.postToURL(url: String, head: HttpHeads = arrayListOf(),
                       timeOut: Int = timeout, charset: Charset = Charset.forName("utf-8"),
                       sslSocketFactory: SSLSocketFactory? = null
): String {
    var con = URL(url).openConnection() as HttpURLConnection
    try {
        if (sslSocketFactory != null) {
            con = con as HttpsURLConnection
            con.sslSocketFactory = sslSocketFactory
        }
        con.readTimeout = timeOut
        con.requestMethod = "POST"
        con.doInput = true
        con.doOutput = true
        head.forEach { con.addRequestProperty(it.first, it.second.toString()) }
        con.outputStream.write(this.toByteArray(charset))
        return con.inputStream.readBytes().toString(charset)
    } finally {
        con.disconnect()
    }
}

object HttpHelper {
    fun getSSLSocketFactory(pk12File: Resource, pemFile: Resource, pwd: String): SSLSocketFactory {
        var keystore = KeyStore.getInstance("PKCS12")
        pk12File.inputStream.use {
            keystore.load(it, pwd.toCharArray())
        }
        var keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
        keyFactory.init(keystore, pwd.toCharArray())
        var keyManagers = keyFactory.keyManagers
        var context = SSLContext.getInstance("SSL", "SunJSSE")
        context.init(keyManagers, null, SecureRandom())
        return context.socketFactory
    }
}

