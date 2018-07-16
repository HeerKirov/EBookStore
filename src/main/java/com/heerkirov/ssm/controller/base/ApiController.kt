package com.heerkirov.ssm.controller.base

import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONException
import com.alibaba.fastjson.JSONObject
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import java.sql.SQLException
import javax.servlet.http.HttpServletRequest

abstract class ApiController {
    abstract fun request(): HttpServletRequest

    //快速获取content构成的json结构的函数。
    protected fun contentBody(): Any? {
        val attr = request().getParameter("json")
        val text = attr?.toString()?:request().reader.readText()
        return if(text.isNotBlank()) {
            try{
                JSONObject.toJSON(text)
            }catch (_: JSONException){
                null
            }
        }else{
            null
        }
    }
    protected fun contentBodyObject(): JSONObject? {
        val attr = request().getParameter("json")
        val text = attr?.toString()?:request().reader.readText()

        return if(text.isNotBlank()) {
            try {
                JSONObject.parseObject(text)
            }catch (_: JSONException){
                null
            }
        }else{
            null
        }
    }
    protected fun contentBodyArray(): JSONArray? {
        val attr = request().getParameter("json")
        val text = attr?.toString()?:request().reader.readText()
        return if(text.isNotBlank()) {
            try {
                JSONObject.parseArray(text)
            }catch (_: JSONException){
                null
            }
        }else{
            null
        }
    }

    //用于快速抛出异常的内部函数。
    private fun<EX> standErrorView(e: EX): Any where EX: HttpStatusException {
        return mapOf("error" to e.message, "keyword" to e.keyword.toKeyword())
    }

    //所有方法通用的404映射。
    @RequestMapping("/*")
    fun notFound(): Any = throw NotFoundException()
    //400
    @ExceptionHandler(/*新条目修改*/BadRequestException::class)
    @ResponseStatus(/*新条目修改*/HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun /*新条目修改*/errorBadRequest(e: HttpStatusException) = standErrorView(e)
    //403
    @ExceptionHandler(/*新条目修改*/ForbiddenException::class)
    @ResponseStatus(/*新条目修改*/HttpStatus.FORBIDDEN)
    @ResponseBody
    fun /*新条目修改*/errorForbidden(e: HttpStatusException) = standErrorView(e)
    //404
    @ExceptionHandler(/*新条目修改*/NotFoundException::class)
    @ResponseStatus(/*新条目修改*/HttpStatus.NOT_FOUND)
    @ResponseBody
    fun /*新条目修改*/errorNotFound(e: HttpStatusException) = standErrorView(e)
    //405
    @ExceptionHandler(/*新条目修改*/MethodNotAllowedException::class)
    @ResponseStatus(/*新条目修改*/HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    fun /*新条目修改*/errorMethodNotAllowed(e: HttpStatusException) = standErrorView(e)
    //数据库错误
    @ExceptionHandler(SQLException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun errorJdbc(e: SQLException): Any {
        return mapOf("error" to e.errorCode, "message" to e.localizedMessage)
    }
    @ExceptionHandler(NullPointerException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    fun errorNull(e: NullPointerException): Any {
        return mapOf("error" to "Not Found", "message" to e.localizedMessage)
    }
    @ExceptionHandler(KotlinNullPointerException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    fun errorNull(e: KotlinNullPointerException): Any {
        return mapOf("error" to "Not Found", "message" to e.localizedMessage)
    }

}