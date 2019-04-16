package com.example.app.controller

import com.example.app.entity.Recommend
import com.example.app.entity.Search
import com.example.app.entity.Task
import com.example.app.helpers.postToURL
import com.example.app.service.*
import org.springframework.data.domain.PageRequest
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.bind.annotation.*
import java.lang.Exception


@RestController
class RecommendController(
        val newsServiceI: NewsServiceI,
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
        return mapOf("status" to true, "message" to "")
    }


    //2.删除新闻
    @DeleteMapping("/news/recommend_news")
    fun delete_recommend_news(news_id: String): Any {
        try {
            deleteIndex(id = news_id)
        } catch (e: Exception) {
            return mapOf("status" to false, "message" to e.message)
        }
        return mapOf("status" to true, "message" to "")
    }


    //3.获取推荐信息
    @GetMapping("/news/recommend")
    fun recommend(company_id: String?, matching: Float?, task_id: String?, type: String): Any? {

        if (company_id == null && task_id == null) {
            throw Exception("company_id or task_id must not null")
        }

        if (company_id != null) {
            matching ?: throw Exception("matching must not null")
            val pageRequest = PageRequest(0, 100)
            val resList = recommendServiceI.findByCompanyId(companyId = company_id, pageable = pageRequest)

            if (resList.content.size == 0) {
                val task = taskServiceI.save(Task(companyIds = company_id, matching = matching))
                taskList.add(task.id)
                return hashMapOf("task_id" to task.id, "result" to "resList")
            }


            val rt = resList.content.map { it ->
                hashMapOf(
                        "newsId" to it.newsId,
                        "type" to "news",  //policy、guide
                        "matching" to it.matching
                )
            }
            return hashMapOf("task_id" to "", "result" to hashMapOf("company_id" to rt))

        } else {
            val task = taskServiceI.get(id = task_id!!.toLong())

            if (task.isCompleted) {
                val rt = task.companyIds.split(",").map { companyId ->
                    val pageRequest = PageRequest(0, 100)
                    val recommendPagelist = recommendServiceI.findByCompanyId(companyId, pageRequest)
                    val recommendRt = recommendPagelist.content.map {
                        hashMapOf(
                                "newsId" to it.newsId,
                                "matching" to it.matching
                        )
                    }
                    hashMapOf(
                            "companyId" to companyId,
                            "data" to recommendRt
                    )
                }
                return hashMapOf("status" to true, "result" to rt, "message" to "")
            } else {
                return hashMapOf("status" to false, "result" to "", "message" to "未处理完成")
            }
        }
    }

    //4.推送新闻资讯
    @PostMapping("/news/checkRecommend")
    fun checkRecommend(company_ids: String, threshold: Float, callbackUrl: String): Any? {
        if (!callbackTest(callbackUrl)) {
            return hashMapOf("task_id" to "", "message" to hashMapOf("status" to false, "traceback" to "CALLBACK_FAIL"))
        }

        try {
            val task = taskServiceI.save(Task(companyIds = company_ids, matching = threshold, callbackUrl = callbackUrl))
            taskList.add(task.id)
            return hashMapOf("task_id" to task.id, "message" to hashMapOf("status" to true, "traceback" to ""))
        } catch (e: Exception) {
            return hashMapOf("task_id" to "", "message" to hashMapOf("status" to false, "traceback" to e.message))
        }
    }

    @RequestMapping("/test")
    fun test(task_id: String, result: String): Any? {
        return result
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
    fun ttt() {
        if (taskList.size > 0) {
            val taskId = taskList.removeAt(0)
            val task = taskServiceI.get(id = taskId)
            val matching = task.matching
            task.companyIds.split(",").forEach { companyId ->
                val companyDescribe = companyServiceI.getdescribe(companyId)
                val pageRequest = PageRequest(0, 100)
                val pagelist = apiSearchServiceI.findByText(companyDescribe, pageRequest)

                recommendServiceI.deleteByCompanyId(companyId)
                pagelist.content.filter { it.score >= matching }.forEach { search ->
                    val recommend = Recommend(
                            companyId = companyId,
                            newsId = search.id,
                            title = search.title,
                            content = search.content,
                            matching = search.score
                    )
                    recommendServiceI.save(recommend)
                }
            }

            task.isCompleted = true
            taskServiceI.save(task)

            if (task.callbackUrl.length > 0) {
                val dataList = mutableListOf<Pair<String, Any>>()
                task.companyIds.split(",").forEach { companyId ->
                    val pageRequest = PageRequest(0, 100)
                    val pagelist = recommendServiceI.findByCompanyId(companyId = companyId, pageable = pageRequest)
                    val rt = pagelist.content.map { it ->
                        hashMapOf(
                                "newsId" to it.newsId,
                                "matching" to it.matching
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
        }
    }


    //4.测试回调接口
    fun callbackTest(url: String): Boolean {
        val matching = Math.random()
        val result = hashMapOf(
                "test" to hashMapOf(
                        "matching" to matching,
                        "status" to "TEST"
                )
        )
        val httpParams = listOf(
                Pair("task_id", "test"),
                Pair("result", result.toString())
        )
        val res = responseUrl(url, httpParams)
        print("\ntesturlresult:" + res + "\n")
        return res.contains("" + matching)
    }

    fun responseUrl(url: String, httpParams: List<Pair<String, Any>>): String {
        return httpParams.postToURL(url)
    }
}