package com.example.app.entity

import com.example.app.controller.Type
import java.sql.Blob
import java.util.*
import javax.persistence.*

@Entity
data class Recommend(
        @Id
        @GeneratedValue
        var id: Long = 0,

        @Column()
        var companyId: String = "",

        @Column()
        var newsId: String = "",

        @Column()
        var title: String = "",

        @Column(columnDefinition="LONGBLOB")
        var content: ByteArray = byteArrayOf(),

        @Column()
        var matching: Float = 0.0f,

        @Column()
        var time: Date = Date(),

        @Column()
        var type: Type = Type.news
)