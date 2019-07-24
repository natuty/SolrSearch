package com.example.app.service.impl


import com.example.app.dao.NewsDaoI
import com.example.app.dao.redisDefaultTimeout
import com.example.app.entity.News
import com.example.app.helpers.jsonGet
import com.example.app.helpers.jsonSet
import com.example.app.helpers.jsonStringToObject
import com.example.app.service.CompanyServiceI
import com.example.app.service.NewsServiceI
import org.apache.axis.client.Call
import org.apache.http.params.HttpParams
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.StringRedisTemplate
import java.util.concurrent.TimeUnit

//@Service
@Component
@ConfigurationProperties(prefix = "recommend.data")
class CompanyServiceImpl(
        val stringRedisTemplate: StringRedisTemplate
) : CompanyServiceI {

    var targetEndpointAddress = ""
    var token = ""
    var operationName = ""
    val log= LoggerFactory.getLogger(CompanyServiceImpl::class.java)

    var redisCacheTimeout=redisDefaultTimeout*60 //一小时
    val keyGroup = "entity:describe:"
    fun getKey(vShxydm: String, vDataitems: String) = "${keyGroup}${vShxydm}${vDataitems}"
    val valueOps by lazy {
        stringRedisTemplate.opsForValue()
    }

    /**
     * token 身份验证
     * V_SHXYDM 社会信用代码
     * vDataitems 数据项列表（格式为数据资源编号.数据项编号，多项用逗号分隔）
     * */
    override fun getdescribe(vShxydm: String, vDataitems: String): String {

        var key = getKey(vShxydm, vDataitems)
        var desc: String? = valueOps.jsonGet(key)
        if (desc != null) {
            stringRedisTemplate.expire(key, redisCacheTimeout, TimeUnit.MILLISECONDS)
            return desc
        }

        val s = org.apache.axis.client.Service()
        var call = s.createCall() as Call
        call.setOperationName(operationName)
        call.targetEndpointAddress = targetEndpointAddress
        val response = call.invoke(arrayOf<Any>(token, vShxydm, vDataitems)) as String

        if (response.jsonStringToObject<HashMap<String, Any?>>()!!["Status"] ?: "" != "Success")
            throw Exception("get company info fail")

        response.jsonStringToObject<DescribeResult>()
        val describeResult = response.jsonStringToObject<DescribeResult>() ?: throw Exception("get company info fail")
        var describe = describeResult.Result!!.get(0)!![vDataitems] ?: throw Exception("get company info fail")

        println(vDataitems+":"+describe)

        if (vDataitems == "DR1.OPSCOPE") {
            val list = describe.split(";")

            if (list.size > 5) {
                describe = list.subList(0, 4).joinToString(";")
            }else{
                describe = list.joinToString(";")
            }
        }

        if(describe.length>0){
            valueOps.jsonSet(key, describe, redisCacheTimeout, TimeUnit.MILLISECONDS)
        }


        return describe
    }

}

data class DescribeResult(
        var Status: String,
        var Message: String,
        var Result: List<HashMap<String, String>?>?
)