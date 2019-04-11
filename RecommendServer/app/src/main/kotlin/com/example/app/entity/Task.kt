package com.example.app.entity

import javax.persistence.*

@Entity
data class Task(
        @Id
        @GeneratedValue
        var id: Long = 0,

        @Column()
        var companyIds: String ="",

        @Column()
        var matching: Float = 0.0f ,

        @Column()
        var isCompleted: Boolean = false,

        @Column()
        var callbackUrl: String = ""

)