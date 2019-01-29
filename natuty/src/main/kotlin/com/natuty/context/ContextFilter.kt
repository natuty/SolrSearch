package com.natuty.context

import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import javax.servlet.*
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest

/**
 * 初始化请求的上下文
 */
@WebFilter(*["/api/*"])
@Order(100000)
class ContextFilter : Filter {
    val log = LoggerFactory.getLogger(javaClass)
    init {
        log.info("初始化Context filter")
    }
    override fun destroy() {

    }

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        try {
            Context.init(request as HttpServletRequest)
            chain.doFilter(request, response)
        } finally {
            Context.clean()
        }
    }

    override fun init(filterConfig: FilterConfig?) {

    }
}