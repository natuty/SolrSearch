package com.natuty.context

import javax.servlet.http.HttpServletRequest

/**
 * servlet 请求上下文
 */
object Context {
    private val context: ThreadLocal<MutableMap<String, Any?>> = ThreadLocal()

    fun init(request: HttpServletRequest) {
        context.remove()
        context.set(mutableMapOf())
        try {
            initParams(request = request)
        } finally {
            securityCheck()
        }
    }


    /**
     * 初始化请求上下文
     */
    private fun initParams(request: HttpServletRequest) {
        request.paramSafe(param = "projectId") { Context.projectId = this.toLong() }
    }

    /**
     * 上下文安全检测
     */
    private fun securityCheck() {

    }

    /**
     * 清理线程上的上下文
     */
    fun clean() {
        context.remove()
    }

    /**
     * 获取当前的项目ID
     */
    var projectId: Long?
        get() {
            return context.get().get("projectId") as? Long
        }
        set(value) {
            context.get().put("projectId", value)
        }
    private inline fun  <T> HttpServletRequest.paramSafe(param: String, block: String.() -> T):T? {
        try {
          return  this.getHeader(param)?.block()
        } catch (e: Exception) {

        }
        return null
    }
}