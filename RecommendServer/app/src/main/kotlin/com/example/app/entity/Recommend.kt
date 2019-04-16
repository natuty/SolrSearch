package com.example.app.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

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

        @Column()
        var content: String = "",

        @Column()
        var matching: Float = 0.0f
)