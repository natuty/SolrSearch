package com.example.app.mq

import com.example.app.controller.Type
import com.example.app.helpers.jsonStringToObject
import com.example.app.mq.struct.RabbitMQMessage
import com.example.app.mq.struct.RabbitMQMsgType
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message

abstract class AbstractReceiver{
    val log = LoggerFactory.getLogger(javaClass)

    fun onMessage(msg: String, message: Message){
        println("onMessage...")
        var rabbitMQMessage = msg.jsonStringToObject<RabbitMQMessage>() ?: throw Exception("jsonStringToObject failed")
        val companyId = rabbitMQMessage.company_id
        val newsId = rabbitMQMessage.news_id
        val type = rabbitMQMessage.type

        onClick(companyId, newsId, type)
    }

    abstract fun onReceive(msg: String, message: Message)
    abstract fun onClick(companyId: String, newsId: String, type: Type)
}