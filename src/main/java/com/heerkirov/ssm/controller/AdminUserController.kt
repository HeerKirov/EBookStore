package com.heerkirov.ssm.controller

import com.heerkirov.ssm.controller.base.ApiController
import com.heerkirov.ssm.controller.base.BadRequestException
import com.heerkirov.ssm.controller.base.HttpKeyword
import com.heerkirov.ssm.service.AdminUserService
import com.heerkirov.ssm.util.toMap
import com.heerkirov.ssm.util.toMapList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

@RestController
@RequestMapping("/api/admin/user")
class AdminUserController(@Autowired private val request: HttpServletRequest,
                          @Autowired private val session: HttpSession,
                          @Autowired private val adminUserService: AdminUserService): ApiController() {
    override fun request(): HttpServletRequest = request
    @GetMapping("")
    fun list(): Any {
        return adminUserService.list().toMapList(exclude = setOf("password"))
    }
    @GetMapping("/{id}")
    fun retrieve(@PathVariable id: String): Any {
        return adminUserService.get(id)!!.toMap(exclude = setOf("password"))
    }
    @PostMapping("/{id}/setPassword")
    fun updatePassword(@PathVariable id: String): Any {
        val content = contentBodyObject()?:throw BadRequestException(keyword = HttpKeyword.NO_ENOUGH_INFORMATION)
        val password = content["password"]?:throw BadRequestException(keyword = HttpKeyword.NO_ENOUGH_INFORMATION)
        val result = adminUserService.updatePassword(id, password.toString())
        return mapOf("success" to result)
    }
}