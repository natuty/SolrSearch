package com.example.app.entity

import javax.persistence.*

@Entity
data class News(
        @Id
        @GeneratedValue
        var id: Long = 0,

        @Column()
        var name: String = "",

        @Column()
        var newsId: String = "",

        @Column()
        var title: String = "",

        @Column(columnDefinition = "LONGBLOB")
        var content: ByteArray = byteArrayOf()

)