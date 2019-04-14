package com.example.app.service.impl


import com.example.app.dao.NewsDaoI
import com.example.app.entity.News
import com.example.app.helpers.jsonStringToObject
import com.example.app.service.CompanyServiceI
import com.example.app.service.NewsServiceI
import org.apache.axis.client.Call
import org.apache.http.params.HttpParams
import org.springframework.stereotype.Service

@Service
class CompanyServiceImpl : CompanyServiceI {

    override fun getdescribe(vShxydm: String, token: String, vDataitems: String): String {

        val s = org.apache.axis.client.Service()
        var call = s.createCall() as Call
        call.setOperationName("getParamInfo")
        call.targetEndpointAddress = "http://47.106.70.192/webService/services/ws?wsdl"
        /* val TOKEN = "F30FD00E373FD16544C308A6BD5CFDE2"    //身份验证
        val V_SHXYDM = "91440101717852200L" //社会信用代码
        val V_DATAITEMS = "DR1.OPSCOPE" //数据项列表（格式为数据资源编号.数据项编号，多项用逗号分隔）
        */

        val response = call.invoke(arrayOf<Any>(token, vShxydm, vDataitems)) as String


        if (response.jsonStringToObject<HashMap<String, Any?>>()!!["Status"] ?: "" != "Success")
            throw Exception("get company info fail")

        response.jsonStringToObject<DescribeResult>()
        val describeResult = response.jsonStringToObject<DescribeResult>() ?: throw Exception("get company info fail")
        var describe = describeResult.Result!!.get(0)!![vDataitems] ?: throw Exception("get company info fail")

        if (vDataitems == "DR1.OPSCOPE") {
            val list = describe.split(";")
            if (list.size > 5) {
                describe = list.subList(0, 4).joinToString { ";" }
            }
        }
        return describe
    }

}

data class DescribeResult(
        var Status: String,
        var Message: String,
        var Result: List<HashMap<String, String>?>?
)