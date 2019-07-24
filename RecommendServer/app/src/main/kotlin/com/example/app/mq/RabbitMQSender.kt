package com.example.app.mq

import com.example.app.helpers.toJsonString
import com.example.app.mq.struct.RabbitMQMessage
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.rabbit.support.CorrelationData
import org.springframework.context.support.AbstractApplicationContext
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
@Order(1000)
class RabbitMQSender(
        val rabbitTemplate: RabbitTemplate,
        val applicationContext: AbstractApplicationContext
): RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback{
    val log = LoggerFactory.getLogger(RabbitMQSender::class.java)

    @PostConstruct
    fun init() {
        rabbitTemplate.setConfirmCallback(this)
        rabbitTemplate.setReturnCallback(this)
    }

    override fun confirm(correlationData: CorrelationData?, ack: Boolean, cause: String?) {
        println("消息发送回调-${if (ack) "成功:${correlationData}" else "失败:${cause}"}")
    }

    override fun returnedMessage(message: Message, replyCode: Int, replyText: String?, exchange: String?, routingKey: String?) {
        System.out.println(message.messageProperties.correlationIdString + " 发送失败");
    }

    /**
     * 发送日志
     * */
    fun sendClickLog(rabbitMQMessage: RabbitMQMessage) {
        rabbitTemplate.convertAndSend(
                "news.click",
                "news.click",
                rabbitMQMessage.toJsonString()
        )
        print("发送rabbitmqF消息成功：${rabbitMQMessage.toJsonString()}")
    }
}