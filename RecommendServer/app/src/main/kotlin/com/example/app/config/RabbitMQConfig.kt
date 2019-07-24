package com.example.app.config

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order

@Configuration
@Order(999)
class RabbitMQConfig{

    /*队列*/
    @Bean
    fun fileEventQueue(): Queue {
        return Queue("news.click", true)
    }

    /*路由*/
    @Bean
    fun fileEventExchange(): FanoutExchange {
        return FanoutExchange("news.click", true, false)
    }

    /*绑定*/
    @Bean
    fun fileEventBind(): Binding {
        return BindingBuilder.bind(fileEventQueue()).to(fileEventExchange())
    }
}