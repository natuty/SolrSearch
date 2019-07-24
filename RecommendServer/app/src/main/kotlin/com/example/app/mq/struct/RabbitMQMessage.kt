package com.example.app.mq.struct

import com.example.app.controller.Type

//企业id，新闻id，新闻类型，      点击时间
data class RabbitMQMessage(
        var company_id: String,
        var news_id: String,
        var type: Type
)

object RabbitMQMsgType{
    val policy="policy" //政策
    val guide="guide" //指南
    val news="news" //新闻
    val announce="announce" //公告
    val publicity="publicity" //公示
    val all="all" //所有类型
}