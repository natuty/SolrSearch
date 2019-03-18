package com.example.app.controller

import com.example.app.client.impl.DataClientServiceImpl
import com.example.app.entity.Search
import com.example.app.service.ApiSearchServiceI
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.Exception

@RestController
class CallBackController(
        val dataClientServiceImpl: DataClientServiceImpl,
        val apiSearchServiceI: ApiSearchServiceI
){

    @RequestMapping("/data")
    fun callback(id: String, event: Event, type: Type):Any? {
        try {
        val text = when(type){
            Type.policy ->dataClientServiceImpl.get_policy_text(id)
            Type.guide -> dataClientServiceImpl.get_guide_text(id)
        }
        val id = "" + type.ordinal + id // 区分文件类型

        when(event){
            Event.add -> addIndex(id = id, text = text, type = type)
            Event.update -> updateIndex(id = id, text = text, type = type)
            Event.delete -> deleteIndex(id = id, text = text, type = type)
        }
        }catch (e: Exception){
            e.printStackTrace()
            return hashMapOf("status" to false)
        }

        return hashMapOf("status" to true)
    }

    fun addIndex(id: String, text: String, type: Type){
        val search = Search(id = id, text = text, type = type.name)
        apiSearchServiceI.save(search)
    }

    fun deleteIndex(id: String, text: String, type: Type){
        apiSearchServiceI.delete(id = id)
    }

    fun updateIndex(id: String, text: String, type: Type){
        val search = Search(id = id, text = text, type = type.name)
        apiSearchServiceI.get(id = id)?: throw Exception()
        apiSearchServiceI.delete(id = id)
        apiSearchServiceI.save(search)
    }
}


enum class Type(val remark: String) {
    policy("政策文件"), guide("指南文件")
}

enum class Event(val remark: String) {
    delete("delete"), add("add"), update("update")
}
