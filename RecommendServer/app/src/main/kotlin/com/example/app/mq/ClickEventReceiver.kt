package com.example.app.mq

import com.example.app.entity.Search
import com.example.app.service.ApiSearchServiceI
import com.example.app.service.ClickLogServiceI
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.lang.Exception
import java.security.KeyRep
import com.example.app.controller.Type
import com.example.app.entity.ClickLog
import com.hankcs.hanlp.HanLP
import org.springframework.boot.context.properties.ConfigurationProperties

@Component
@Order(1000)
@ConfigurationProperties(prefix = "recommend.click")
class ClickEventReceiver(
        val apiSearchServiceI: ApiSearchServiceI,
        val clickLogServiceI: ClickLogServiceI
) : AbstractReceiver() {

    var itemLogSize = 10
    var itemEachKeywordSize = 10
    var itemAllKeywordSize = 100

    @RabbitListener(queues = arrayOf("news.click"))
    override fun onReceive(msg: String, message: Message) {
        onMessage(msg = msg, message = message)
    }

    override fun onClick(companyId: String, newsId: String, type: Type) {

        try {

            val newsId = "" + type.ordinal + newsId
            println("newsId:"+newsId)
            val search = apiSearchServiceI.get(newsId) ?: throw Exception("not exist!")

            //分词, 关键词提取
            val describeKeywordList = HanLP.extractKeyword(search.content, itemEachKeywordSize)
            var keywords = describeKeywordList.joinToString(",")

            var clickLog = clickLogServiceI.findFirstByCompanyIdAndType(companyId = companyId, type = type)

            if (clickLog == null) {
                clickLog = clickLogServiceI.save(ClickLog(companyId = companyId, newsIds = newsId, type = type, keywords = keywords))
            } else {
                var keywordList = clickLog.keywords.split(",")
                if (keywordList.size >= itemAllKeywordSize) {
                    keywordList = keywordList.subList(0, itemAllKeywordSize - itemEachKeywordSize - 1)
                }

                keywords = keywords + "," + keywordList.joinToString(",")

                var newsIdList = clickLog.newsIds.split(',')
                val listSize = newsIdList.size
                if(newsIdList.contains(newsId)){
                    return
                }

                var newsIds = if (listSize >= itemLogSize) {
                    newsIdList.subList(0, itemLogSize - 2).joinToString(",")
                } else {
                    newsId + "," + clickLog.newsIds
                }

                clickLog.newsIds = newsIds
                clickLog.keywords = keywords
                clickLogServiceI.save(clickLog)
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}