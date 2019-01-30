package com.natuty.controller.test

import org.springframework.context.support.AbstractApplicationContext
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/open")
class Test(
        val applicationContext: AbstractApplicationContext
) {

    @RequestMapping("/hello")
    fun hello(str: String? = "hello"):Any{
        return "hello " + str
    }

}