package com.example.app.service.impl


import com.example.app.dao.NewsDaoI
import com.example.app.entity.News
import com.example.app.service.CompanyServiceI
import com.example.app.service.NewsServiceI
import org.apache.axis.client.Call
import org.apache.http.params.HttpParams
import org.springframework.stereotype.Service

@Service
class CompanyServiceImpl: CompanyServiceI {

    override fun getdescribe(vShxydm:String, token: String?, vDataitems: String?): String {

        //companyId: String
        val s = org.apache.axis.client.Service()
        var call = s.createCall() as Call
        call.setOperationName("getParamInfo")
        call.targetEndpointAddress = "http://47.106.70.192/webService/services/ws?wsdl"
/*        val TOKEN = "F30FD00E373FD16544C308A6BD5CFDE2"    //身份验证
        val V_SHXYDM = "91440101717852200L" //社会信用代码
        val V_DATAITEMS = "DR1.OPSCOPE" //数据项列表（格式为数据资源编号.数据项编号，多项用逗号分隔）
        */

        val token = "F30FD00E373FD16544C308A6BD5CFDE2"
        val vDataitems = "DR1.OPSCOPE"
        val re = call.invoke(arrayOf<Any>(token, vShxydm, vDataitems)) as String
        return re
    }






}