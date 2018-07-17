package com.heerkirov.ssm.controller

import com.heerkirov.ssm.controller.base.ApiController
import com.heerkirov.ssm.controller.base.ForbiddenException
import com.heerkirov.ssm.model.User
import com.heerkirov.ssm.service.AdminOrderService
import com.heerkirov.ssm.service.OrderService
import com.heerkirov.ssm.util.toMap
import com.heerkirov.ssm.util.toMapList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

@RequestMapping("/api/admin/order")
@RestController
class AdminOrderController(@Autowired private val request: HttpServletRequest,
                           @Autowired private val session: HttpSession,
                           @Autowired private val adminOrderService: AdminOrderService): ApiController() {
    private fun<T> ad(action: ()->T): T where T: Any {
        val user = session.getAttribute("user") as User?
        if(user == null || (!user.isAdmin.booleanValue())) throw ForbiddenException()
        else return action()
    }
    override fun request(): HttpServletRequest = request
    @GetMapping("")
    fun list() = ad {
        adminOrderService.list().toMapList()
    }
    @PostMapping("/check/{id}")
    fun checkSubmit(@PathVariable id: String) = ad {
        adminOrderService.changeStatus(id.toInt(), "running")
        mapOf("success" to true)
    }
    @PostMapping("/return/{id}")
    fun checkReturn(@PathVariable id: String) = ad {
        adminOrderService.changeStatus(id.toInt(), "returned")
        mapOf("success" to true)
    }
}