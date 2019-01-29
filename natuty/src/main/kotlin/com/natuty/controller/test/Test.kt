package com.natuty.controller.test

import com.natuty.exception.BusinessException
import org.springframework.context.support.AbstractApplicationContext
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

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