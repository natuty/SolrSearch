package com.example.app.controller

import com.example.app.entity.Recommend
import com.example.app.entity.Search
import com.example.app.entity.Task
import com.example.app.entity.TaskStatus
import com.example.app.helpers.jsonStringToObject
import com.example.app.helpers.postToURL
import com.example.app.helpers.toJsonString
import com.example.app.service.*
import com.example.app.service.impl.DescribeResult
import org.apache.axis.client.Call
import org.springframework.data.domain.PageRequest
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.bind.annotation.*
import java.lang.Exception
import java.util.*


@RestController
class RecommendController(
        val recommendServiceI: RecommendServiceI,
        val taskServiceI: TaskServiceI,
        val apiSearchServiceI: ApiSearchServiceI,
        val companyServiceI: CompanyServiceI
) {

    var taskList = mutableListOf<Long>() //任务列表

    //1.添加新闻
    @PostMapping("/news/recommend_news")
    fun upload_news(news_id: String, title: String, content: String): Any {
        try {
            addIndex(id = news_id, title = title, content = content)
        } catch (e: Exception) {
            return mapOf("status" to false, "message" to e.message)
        }
        return mapOf("status" to true, "message" to "success")
    }


    //2.删除新闻
    @DeleteMapping("/news/recommend_news")
    fun delete_recommend_news(news_id: String): Any {
        try {
            deleteIndex(id = news_id)
        } catch (e: Exception) {
            return mapOf("status" to false, "message" to e.message)
        }
        return mapOf("status" to true, "message" to "success")
    }


    //3.获取推荐信息
    @PostMapping("/news/recommend")
    fun recommend(company_id: String?, matching: Float?, task_id: String?): Any? {

        try {
            if (company_id == null && task_id == null) {
                throw Exception("company_id or task_id must not null")
            }

            if (company_id != null) {
                matching ?: throw Exception("matching must not null")
                val pageRequest = PageRequest(0, 100)
                val resList = recommendServiceI.findByCompanyId(companyId = company_id, pageable = pageRequest)

                if (resList.content.size == 0) {

                    if(taskList.size>1000){
                        throw Exception("too many tasks at present")
                    }

                    val token = "F30FD00E373FD16544C308A6BD5CFDE2"
                    companyServiceI.getdescribe(vShxydm = company_id, vDataitems = "DR1.OPSCOPE", token = token)
                    val task = taskServiceI.save(Task(companyIds = company_id, matching = matching))
                    taskList.add(task.id)
                    return hashMapOf("task_id" to task.id, "result" to "")
                }


                val rt = resList.content.map { it ->
                    hashMapOf(
                            "newsId" to it.newsId,
                            "matching" to it.matching,
                            "title" to it.title,
                            "content" to String(it.content),
                            "time" to it.time
                    )
                }
                return hashMapOf("task_id" to "", "result" to hashMapOf("company_id" to rt))

            } else {
                val task = taskServiceI.get(id = task_id!!.toLong())

                when (task.status) {
                    TaskStatus.Completed -> {
                        val rt = task.companyIds.split(",").map { companyId ->
                            val pageRequest = PageRequest(0, 100)
                            val recommendPagelist = recommendServiceI.findByCompanyId(companyId, pageRequest)
                            val recommendRt = recommendPagelist.content.sortedByDescending { it.matching }.map {
                                hashMapOf(
                                        "newsId" to it.newsId,
                                        "matching" to it.matching,
                                        "title" to it.title,
                                        "content" to String(it.content),
                                        "time" to it.time
                                )
                            }
                            hashMapOf(
                                    "companyId" to companyId,
                                    "data" to recommendRt
                            )
                        }
                        return hashMapOf("status" to TaskStatus.Completed, "result" to rt, "message" to "")
                    }
                    TaskStatus.Fail -> {
                        return hashMapOf("status" to TaskStatus.Fail, "result" to "", "message" to "任务失败")
                    }
                    TaskStatus.UnCompleted -> {
                        return hashMapOf("status" to TaskStatus.UnCompleted, "result" to "", "message" to "未处理完成")
                    }
                }
            }
        } catch (e: Exception) {
            return hashMapOf("status" to false, "result" to "", "message" to e.message)
        }
    }

    //4.推送新闻资讯
    @PostMapping("/news/checkRecommend")
    fun checkRecommend(companies: String, threshold: Float, callbackUrl: String): Any? {
        if (!callbackTest(callbackUrl)) {
            return hashMapOf("task_id" to "", "message" to hashMapOf("status" to false, "traceback" to "CALLBACK_FAIL"))
        }

        try {
            val task = taskServiceI.save(Task(companyIds = companies, matching = threshold, callbackUrl = callbackUrl))
            taskList.add(task.id)
            return hashMapOf("task_id" to task.id, "message" to hashMapOf("status" to true, "traceback" to ""))
        } catch (e: Exception) {
            return hashMapOf("task_id" to "", "message" to hashMapOf("status" to false, "traceback" to e.message))
        }
    }

    @RequestMapping("/test")
    fun test(task_id: String, result: String): Any? {
        return hashMapOf(
                "task_id" to task_id,
                "message" to ""
        )
    }

    fun addIndex(id: String, title: String, content: String) {
        val search = Search(id = id, title = title, content = title + content)
        apiSearchServiceI.save(search)
    }

    fun deleteIndex(id: String) {
        apiSearchServiceI.delete(id = id)
    }

    fun updateIndex(id: String, title: String, content: String) {
        val search = Search(id = id, title = title, content = content)
        apiSearchServiceI.get(id = id) ?: throw Exception()
        apiSearchServiceI.delete(id = id)
        apiSearchServiceI.save(search)
    }


    @Scheduled(fixedRate = 1000)
    fun t() {
        if (taskList.size > 0) {
            val taskId = taskList.removeAt(0)
            val task = taskServiceI.get(id = taskId)
            val matching = task.matching

            try {
                task.companyIds.split(",").forEach { companyId ->
                    val token = "F30FD00E373FD16544C308A6BD5CFDE2"
                    val companyDescribe = companyServiceI.getdescribe(companyId, vDataitems = "DR1.OPSCOPE", token = token)
                    val companyIndustryco = companyServiceI.getdescribe(companyId, vDataitems = "DR1.INDUSTRYCO", token = token)

                    val pageRequest = PageRequest(0, 100)

                    val describePagelist = apiSearchServiceI.findByText(companyDescribe, pageRequest)
                    val industryPagelist = apiSearchServiceI.findByText(companyIndustryco, pageRequest)

                    val describePageSize = if(describePagelist.content.size > 5) 5 else  describePagelist.content.size
                    val industryPageSize = if(industryPagelist.content.size > 5) 5 else  industryPagelist.content.size

                    var coredes:Float = 0f
                    var coreindus:Float = 0f

                    describePagelist.content.filterIndexed { index, search -> index < describePageSize  }.forEach { coredes += it.score }
                    industryPagelist.content.filterIndexed { index, search -> index < industryPageSize  }.forEach { coreindus += it.score }

                    coredes = if(describePageSize>0) coredes/(1.0f* describePageSize) else 0.0f
                    coreindus = if(industryPageSize>0) coreindus/(1.0f* describePageSize) else 0.0f
                    val pagelist = if(coredes>coreindus) describePagelist else industryPagelist

                    recommendServiceI.deleteByCompanyId(companyId)
                    pagelist.content.filter { it.score >= matching }.forEach { search ->
                        val recommend = Recommend(
                                companyId = companyId,
                                newsId = search.id,
                                title = search.title,
                                content = search.content.toByteArray(),
                                matching = search.score,
                                time = Date()
                        )
                        recommendServiceI.save(recommend)
                    }
                }
                if (task.callbackUrl.length > 0) {
                    val dataList = mutableListOf<Pair<String, Any>>()
                    task.companyIds.split(",").forEach { companyId ->
                        val pageRequest = PageRequest(0, 100)
                        val pagelist = recommendServiceI.findByCompanyId(companyId = companyId, pageable = pageRequest)
                        val rt = pagelist.content.map { it ->
                            hashMapOf(
                                    "newsId" to it.newsId,
                                    "matching" to it.matching,
                                    "title" to it.title,
                                    "content" to String(it.content),
                                    "time" to it.time
                            )
                        }
                        dataList.add(Pair(companyId, rt))
                    }

                    val httpParams = listOf(
                            Pair("task_id", taskId),
                            Pair("result", dataList.toString())
                    )

                    responseUrl(task.callbackUrl, httpParams)
                }

                task.status = TaskStatus.Completed
                taskServiceI.save(task)
            } catch (e: Exception) {
                e.printStackTrace()
                task.status = TaskStatus.Fail //处理任务失败
                taskServiceI.save(task)
            }
        }
    }


    //4.测试回调接口
    fun callbackTest(url: String): Boolean {
        try {
            val matching = Math.random()
            val oneRecord = hashMapOf(
                    "data" to listOf(
                            hashMapOf(
                            "matching" to matching,
                            "newsId" to "6")
                    ),
                    "companyId" to "123"
            )
            val httpParams = listOf(
                    Pair("task_id", "test"),
                    Pair("result", listOf(oneRecord).toJsonString())
            )
            val res = responseUrl(url, httpParams).jsonStringToObject<HashMap<String, Any>>()?.get("task_id")?: ""
            if(res != ""){
                return true
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
        return false
    }

    fun responseUrl(url: String, httpParams: List<Pair<String, Any>>): String {
        return httpParams.postToURL(url)
    }

}