package com.example.app.entity

import com.example.app.controller.Type
import javax.persistence.*

@Entity
data class Task(
        @Id
        @GeneratedValue
        var id: Long = 0,

        @Column()
        var companyIds: String ="",

        @Column()
        var matching: Float = 0.0f,

        @Column()
        var callbackUrl: String = "",

        @Column()
        var status: TaskStatus = TaskStatus.UnCompleted,

        @Column()
        var type: Type = Type.all

        )

enum class TaskStatus(val remark: String) {
        Completed("完成"),
        Fail("失败"),
        UnCompleted("未完成")
}