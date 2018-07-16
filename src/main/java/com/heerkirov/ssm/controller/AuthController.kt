package com.heerkirov.ssm.controller

import com.heerkirov.ssm.controller.base.ApiController
import com.heerkirov.ssm.controller.base.BadRequestException
import com.heerkirov.ssm.controller.base.ForbiddenException
import com.heerkirov.ssm.controller.base.HttpKeyword
import com.heerkirov.ssm.dao.UserDao
import com.heerkirov.ssm.model.Order
import com.heerkirov.ssm.model.User
import com.heerkirov.ssm.service.OrderService
import com.heerkirov.ssm.service.UserService
import com.heerkirov.ssm.util.AuthQuantityCheck
import com.heerkirov.ssm.util.toMap
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

@RequestMapping("/api/auth")
@Controller
class AuthController(@Autowired private val userService: UserService,
                     @Autowired private val orderService: OrderService,
                     @Autowired private val request: HttpServletRequest,
                     @Autowired private val authQuantityCheck: AuthQuantityCheck,
                     @Autowired private val session: HttpSession,
                     @Autowired private val httpServletRequest: HttpServletRequest): ApiController() {
    override fun request(): HttpServletRequest = request

    private fun getRequestIp(): String {
        var result = httpServletRequest.getHeader("X-FORWARDED-FOR")
        if(result.isNullOrEmpty()) result = httpServletRequest.remoteAddr
        return result
    }

    private fun translateCart() {
        //将未登录时的购物车记录转移到登陆购物车内。
        val cart = session.getAttribute("cart") as Order?
        if(cart != null) {
            orderService.cartPutProducts(cart.products)
            session.removeAttribute("cart")
        }
    }

    @PostMapping("/login") @ResponseBody
    fun login(): Any {
        val ip = getRequestIp()
        if(authQuantityCheck.tryAuthenticate(ip)){
            val content = contentBodyObject()?:throw BadRequestException(keyword = HttpKeyword.NO_ENOUGH_INFORMATION)
            val id = content["id"]?:throw BadRequestException(keyword = HttpKeyword.NO_ENOUGH_INFORMATION)
            val pw = content["pw"]?:throw BadRequestException(keyword = HttpKeyword.NO_ENOUGH_INFORMATION)
            val result = userService.login(id.toString(), pw.toString())
            if(result != null) {
                authQuantityCheck.clearRecord(ip)
                translateCart()
            }
            return mapOf("success" to (result != null))
        }else{
            throw ForbiddenException()
        }

    }
    @PostMapping("/logout") @ResponseBody @ResponseStatus(HttpStatus.NO_CONTENT)
    fun logout(): Any {
        userService.logout()
        return emptyMap<String, Any>()
    }
    @PostMapping("/register") @ResponseBody @ResponseStatus(HttpStatus.CREATED)
    fun register(): Any {
        val content = contentBodyObject()?:throw BadRequestException(keyword = HttpKeyword.NO_ENOUGH_INFORMATION)
        val id = content["id"]?:throw BadRequestException(keyword = HttpKeyword.NO_ENOUGH_INFORMATION)
        val name = content["name"]?:throw BadRequestException(keyword = HttpKeyword.NO_ENOUGH_INFORMATION)
        val pw = content["pw"]?:throw BadRequestException(keyword = HttpKeyword.NO_ENOUGH_INFORMATION)
        val phone = content["phone"]?.toString()
        val address = content["address"]?.toString()
        val result = userService.addUser(id.toString(), name.toString(), pw.toString(), false, phone, address)
        userService.login(result.id, result.password)
        return result.toMap(include = setOf("id", "name", "isAdmin"))
    }
    @GetMapping("/myInfo") @ResponseBody
    fun myInfo(): Any {
        return userService.info()!!.toMap(exclude = setOf("password", "isAdmin"))
    }
    @RequestMapping("/myInfo", method = [RequestMethod.PATCH, RequestMethod.PUT]) @ResponseBody
    fun updateMyInfo(): Any {
        val content = contentBodyObject()?:throw BadRequestException(keyword = HttpKeyword.NO_ENOUGH_INFORMATION)

        val name = content["name"]?.toString()
        val address = content["address"]?.toString()
        val phone = content["phone"]?.toString()

        return userService.updateInfo(name, phone, address)!!.toMap(exclude = setOf("password", "isAdmin"))
    }
    @RequestMapping("/changePassword", method = [RequestMethod.PATCH, RequestMethod.PUT]) @ResponseBody
    fun updatePassword(): Any {
        val content = contentBodyObject()?:throw BadRequestException(keyword = HttpKeyword.NO_ENOUGH_INFORMATION)
        val old = content["oldPassword"]?:throw BadRequestException(keyword = HttpKeyword.NO_ENOUGH_INFORMATION)
        val new = content["newPassword"]?:throw BadRequestException(keyword = HttpKeyword.NO_ENOUGH_INFORMATION)
        val result = userService.updatePassword(old.toString(), new.toString())
        return mapOf("success" to result)
    }
}