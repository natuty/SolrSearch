package com.example.app.controller

import com.example.app.dao.redisDefaultTimeout
import com.example.app.entity.Recommend
import com.example.app.entity.Search
import com.example.app.entity.Task
import com.example.app.entity.TaskStatus
import com.example.app.helpers.*
import com.example.app.mq.RabbitMQSender
import com.example.app.mq.struct.RabbitMQMessage
import com.example.app.service.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.lang.Exception
import java.util.*
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit
import com.hankcs.hanlp.HanLP
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.support.AbstractApplicationContext
import org.springframework.stereotype.Component


@RestController
class RecommendController(
        val recommendServiceI: RecommendServiceI,
        val taskServiceI: TaskServiceI,
        val apiSearchServiceI: ApiSearchServiceI,
        val companyServiceI: CompanyServiceI,
        val stringRedisTemplate: StringRedisTemplate,
        val applicationContext: AbstractApplicationContext,
        val clickLogServiceI: ClickLogServiceI
) {

    var itemSearchKeywordSize = 5
    var companyKeywordSize = 10

    var redisCacheTimeout= redisDefaultTimeout;
    val keyGroup = "entity:task:"
    fun getKey(companyIds: String, matching: Float, type: Type) = "${keyGroup}${matching}:${type.remark}"
    val valueOps by lazy {
        stringRedisTemplate.opsForValue()
    }

    var taskList = mutableListOf<Long>() //任务列表

    val rabbitMQSender: RabbitMQSender by lazy {
        applicationContext.getBean(RabbitMQSender::class.java)
    }

    //1.添加新闻
    @PostMapping("/news/recommend_news")
    fun upload_news(news_id: String, title: String?, content: String?, type: Type, files: List<MultipartFile>?): Any? {


        if(type != Type.policy){
            if(title==null || content==null){
                throw Exception("title or content is null")
            }
        }

        val title = title?:" "
        val content = content?:" "


        val tempDir = File("./temp")
        if(tempDir.isDirectory){
            tempDir.deleteRecursively()
        }
        tempDir.mkdir()

        try {
            files?.forEach { file ->
                val filename = file.originalFilename ?: ""
                val extension = filename.split('.').run { this[this.size - 1] }
                val isContain = arrayListOf("pdf", "doc", "docx", "zip", "rar","xls","xlsx", "ppt", "txt").contains(extension)
                if (!isContain){
                    throw Exception("附件仅支持txt,pdf,doc,docx,xls,xlsx,ppt,zip,rar的文件格式")
                }
            }

            var content = content
            files?.forEach { file ->
                val filename = file.originalFilename ?: ""
                File("./temp/"+file.originalFilename).apply {
                    this.createNewFile()
                    FileOutputStream(this).use {
                        it.write(file.bytes)
                        it.close()
                    }
                    val fileContent = apiSearchServiceI.getTextFromFile(this)
                    content = "$content $filename $fileContent"
                    println("\nfileContent:"+fileContent)
                }
            }

            val newsId = "" + type.ordinal + news_id // 区分文件类型
            addIndex(id = newsId, title = title, content = content, type = type)
            recommendServiceI.deleteByType(type)
        } catch (e: Exception) {
            e.printStackTrace()
            val message = e.cause?.message?: e.message
            return mapOf("status" to false, "message" to message)
        }
        return mapOf("status" to true, "message" to "success")



        //return files
        /*try {
            var content = content
            //添加上传文件
            if (file !== null) {
                val filename = file.originalFilename ?: ""
                val extension = filename.split('.').run { this[this.size - 1] }
                val isContain = arrayListOf("pdf", "doc", "docx").contains(extension)
                if (!isContain) return Exception("附件仅支持pdf,doc,dox的文件格式")

                val convFile = File(file.originalFilename)
                convFile.createNewFile()
                val fos = FileOutputStream(convFile)
                fos.write(file.bytes)
                fos.close()
                val fileContent = apiSearchServiceI.getTextFromFile(convFile)
                content = "$content $filename $fileContent"
            }

            val news_id = "" + type.ordinal + news_id // 区分文件类型
            addIndex(id = news_id, title = title, content = content, type = type)
        } catch (e: Exception) {
            return mapOf("status" to false, "message" to e.message)
        }
        return mapOf("status" to true, "message" to "success")*/
    }


    //2.删除新闻
    @DeleteMapping("/news/recommend_news")
    fun delete_recommend_news(news_id: String, type: Type): Any {
        try {
            val news_id = "" + type.ordinal + news_id // 区分文件类型
            deleteIndex(id = news_id)
        } catch (e: Exception) {
            e.printStackTrace()
            return mapOf("status" to false, "message" to e.cause?.message)
        }
        return mapOf("status" to true, "message" to "success")
    }


    //3.获取推荐信息
    @PostMapping("/news/recommend")
    fun recommend(company_id: String?, matching: Float?, task_id: String?, type: Type?): Any? {

        try {
            if (company_id == null && task_id == null) {
                throw Exception("company_id or task_id must not null")
            }

            if (company_id != null) {
                matching ?: throw Exception("matching must not null")
                val pageRequest = PageRequest(0, 100)

                val resList = when (type!!) {
                    Type.all -> recommendServiceI.findByCompanyIdAndMatchingGreaterThanEqual(company_id,matching, pageRequest)
                    else -> recommendServiceI.findByCompanyIdAndTypeAndMatchingGreaterThanEqual(company_id, type,matching, pageRequest)
                }

                if (resList.content.size == 0) {
                    if (taskList.size > 1000) {
                        throw Exception("too many tasks at present")
                    }

                    var key = getKey(company_id, matching, type)
                    var taskId: Long? = valueOps.jsonGet(key)
                    if (taskId == null){
                        //companyServiceI.getdescribe(vShxydm = company_id, vDataitems = "DR1.OPSCOPE")
                        val task = taskServiceI.save(Task(companyIds = company_id, matching = matching, type = type))
                        taskList.add(task.id)
                        return hashMapOf("task_id" to task.id, "result" to "")
                    }else{
                        //return hashMapOf("task_id" to "", "result" to hashMapOf("data" to "[]"))
                        return hashMapOf("task_id" to taskId, "result" to "")
                    }
                }


                val rt = resList.content.map { it ->
                    hashMapOf(
                            "newsId" to it.newsId,
                            "matching" to it.matching,
                            "type" to it.type,
                            "time" to it.time
                    )
                }
                return hashMapOf("task_id" to "", "result" to hashMapOf("data" to rt))

            } else {
                val task = taskServiceI.get(id = task_id!!.toLong())

                when (task.status) {
                    TaskStatus.Completed -> {
                        val rt = task.companyIds.split(",").map { companyId ->
                            val pageRequest = PageRequest(0, 100)

                            val recommendPagelist = when (task.type) {
                                Type.all -> recommendServiceI.findByCompanyIdAndMatchingGreaterThanEqual(companyId, task.matching, pageRequest)
                                else -> recommendServiceI.findByCompanyIdAndTypeAndMatchingGreaterThanEqual(companyId, task.type, task.matching, pageRequest)
                            }

                            val recommendRt = recommendPagelist.content.sortedByDescending { it.matching }.map {
                                hashMapOf(
                                        "newsId" to it.newsId,
                                        "matching" to it.matching,
                                        "type" to it.type,
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
            e.printStackTrace()
            return hashMapOf("status" to false, "result" to "", "message" to e.cause?.message)
        }
    }

    //4.推送新闻资讯
    @PostMapping("/news/checkRecommend")
    fun checkRecommend(companies: String, threshold: Float, callbackUrl: String, type: Type): Any? {
        if (!callbackTest(callbackUrl)) {
            return hashMapOf("task_id" to "", "message" to hashMapOf("status" to false, "traceback" to "CALLBACK_FAIL"))
        }

        try {
            val task = taskServiceI.save(Task(companyIds = companies, matching = threshold, callbackUrl = callbackUrl, type = type))
            taskList.add(task.id)
            return hashMapOf("task_id" to task.id, "message" to hashMapOf("status" to true, "traceback" to ""))
        } catch (e: Exception) {
            e.printStackTrace()
            return hashMapOf("task_id" to "", "message" to hashMapOf("status" to false, "traceback" to e.cause?.message))
        }
    }

    //5.新闻资讯点击
    @PostMapping("/news/click")
    fun click(company_id: String, news_id: String, type: Type): Any? {
        try{
            rabbitMQSender.sendClickLog(rabbitMQMessage = RabbitMQMessage(company_id = company_id, news_id=news_id, type=type))
            return mapOf("status" to true, "message" to "success")
        } catch(e: Exception){
            e.printStackTrace()
            val message = e.cause?.message?: e.message
            return mapOf("status" to false, "message" to message)
        }
    }


    @RequestMapping("/news/callback")
    fun test(task_id: String, result: String): Any? {
        print("result:" + result + "\n")
        return hashMapOf(
                "task_id" to task_id,
                "message" to ""
        )
    }

    fun addIndex(id: String, title: String, content: String, type: Type) {
        val search = Search(id = id, title = title, content = title + content, type = type)
        apiSearchServiceI.save(search)
    }

    fun deleteIndex(id: String) {
        apiSearchServiceI.delete(id = id)
    }

    fun updateIndex(id: String, title: String, content: String) {
        val search = Search(id = id, title = title, content = content)
        apiSearchServiceI.get(id = id) ?: throw Exception("update failed")
        apiSearchServiceI.delete(id = id)
        apiSearchServiceI.save(search)
    }


    @Scheduled(fixedRate = 1000)
    fun t() {
        if (taskList.size > 0) {
            val taskId = taskList.removeAt(0)
            val task = taskServiceI.get(id = taskId)
            //val matching = task.matching
            val matching = 0.0f //获取所有匹配结果


            try {
                task.companyIds.split(",").forEach { companyId ->
                    val pagelist = getSearchPageList(task.type, companyId)

                    //recommendServiceI.deleteByCompanyId(companyId)
                    recommendServiceI.deleteByCompanyIdAndType(companyId = companyId, type = task.type)
                    pagelist.content.filter { it.score >= matching }.forEach { search ->
                        val recommend = Recommend(
                                companyId = companyId,
                                newsId = search.id.substring(1),
                                type = task.type,//search.type?:Type.news,
                                //type = search.type?:Type.news,
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
                        val pagelist = recommendServiceI.findByCompanyIdAndMatchingGreaterThanEqual(companyId = companyId, matching = task.matching, pageable = pageRequest)
                        val rt = pagelist.content.map { it ->
                            hashMapOf(
                                    "newsId" to it.newsId,
                                    "matching" to it.matching,
                                    "type" to it.type,
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

                var key = getKey(task.companyIds, task.matching, task.type)
                valueOps.jsonSet(key, task.id, redisCacheTimeout, TimeUnit.MILLISECONDS)

            } catch (e: Exception) {
                e.printStackTrace()
                task.status = TaskStatus.Fail //处理任务失败
                taskServiceI.save(task)
            }
        }
    }


    //获取搜索列表
    fun getSearchPageList(type: Type, companyId: String): Page<Search> {

        val companyName = companyServiceI.getdescribe(companyId, vDataitems = "DR1.ENTNAME") //企业名称
        var companyDescribe = companyServiceI.getdescribe(companyId, vDataitems = "DR1.OPSCOPE") + " " + companyName  //经营范围
        var companyIndustryco = companyServiceI.getdescribe(companyId, vDataitems = "DR1.INDUSTRYCO") + " " + companyName //行业名称


        //获取点击的关键字
        val clickLog = clickLogServiceI.findFirstByCompanyIdAndType(companyId = companyId, type = type)
        var clickKeyword = ""
        if(clickLog != null){
            val list  = HanLP.extractKeyword(clickLog.keywords, itemSearchKeywordSize)
            if(list.size>0){
                clickKeyword = list.joinToString(",")
            }

        }


        //分词, 关键词提取
        val describeKeywordList = HanLP.extractKeyword(companyDescribe, companyKeywordSize)
        companyDescribe = describeKeywordList.joinToString(",") +","+ clickKeyword

        val industrycoKeywordList = HanLP.extractKeyword(companyIndustryco, companyKeywordSize)
        companyIndustryco = industrycoKeywordList.joinToString(",") +","+ clickKeyword


        val pageRequest = PageRequest(0, 100)
/*
        val describePagelist = apiSearchServiceI.findByText(companyDescribe, pageRequest)
        val industryPagelist = apiSearchServiceI.findByText(companyIndustryco, pageRequest)*/

        val describePagelist = when (type) {
            Type.all -> apiSearchServiceI.findByText(companyDescribe, pageRequest)
            else -> apiSearchServiceI.findByKeywordsAndType(companyDescribe, type.name, pageRequest)
        }

        val industryPagelist = when (type) {
            Type.all -> apiSearchServiceI.findByText(companyIndustryco, pageRequest)
            else -> apiSearchServiceI.findByKeywordsAndType(companyIndustryco, type.name, pageRequest)
        }

        val describePageSize = if (describePagelist.content.size > 5) 5 else describePagelist.content.size
        val industryPageSize = if (industryPagelist.content.size > 5) 5 else industryPagelist.content.size

        var coredes: Float = 0f
        var coreindus: Float = 0f

        describePagelist.content.filterIndexed { index, search -> index < describePageSize }.forEach { coredes += it.score }
        industryPagelist.content.filterIndexed { index, search -> index < industryPageSize }.forEach { coreindus += it.score }

        coredes = if (describePageSize > 0) coredes / (1.0f * describePageSize) else 0.0f
        coreindus = if (industryPageSize > 0) coreindus / (1.0f * describePageSize) else 0.0f
        val pagelist = if (coredes > coreindus) describePagelist else industryPagelist

        return pagelist
    }


    //4.测试回调接口
    fun callbackTest(url: String): Boolean {
        try {
            val matching = Math.random()
            val oneRecord = hashMapOf(
                    "data" to listOf(
                            hashMapOf(
                                    "matching" to matching,
                                    "newsId" to "6",
                                    "type" to Type.news
                            )
                    ),
                    "companyId" to "123"
            )
            val httpParams = listOf(
                    Pair("task_id", "test"),
                    Pair("result", listOf(oneRecord).toJsonString())
            )
            val res = responseUrl(url, httpParams).jsonStringToObject<HashMap<String, Any>>()?.get("task_id") ?: ""
            if (res != "") {
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun responseUrl(url: String, httpParams: List<Pair<String, Any>>): String {
        return httpParams.postToURL(url)
    }
}

enum class Type(val remark: String) {
    policy("政策"), guide("指南"), news("新闻"), announce("公告"), publicity("公示"), all("所有类型")
}
