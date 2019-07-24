package com.example.app.entity

import com.example.app.controller.Type
import java.util.*
import javax.persistence.*

//新闻资讯点击记录
@Entity
data class ClickLog(
        @Id
        @GeneratedValue
        var id: Long = 0,

        //企业id
        @Column()
        var companyId: String = "",

        //新闻id列表
        @Column()
        var newsIds: String = "",

        //类型
        @Column()
        var type: Type = Type.news,

        //提取的关键字, 逗号分隔
        @Column()
        var keywords: String = "",

        //时间
        @Column()
        var time: Date = Date()

)