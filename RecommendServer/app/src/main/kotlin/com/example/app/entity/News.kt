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

        @Column()
        var content: String = ""
)