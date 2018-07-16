package com.heerkirov.ssm.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpSession

@Controller
class WebController(@Autowired private val session: HttpSession) {
    @RequestMapping("/")
    fun defaultRedirect(): String = "redirect:/list"

    @RequestMapping("/list")
    fun listPage(): String {
        return "list"
    }
    @RequestMapping("/cart")
    fun cartPage(): String {
        return "cart"
    }
    @RequestMapping("/order")
    fun orderPage(): String {
        return if(session.getAttribute("user") != null) "order"
        else "redirect:/login"
    }
    @RequestMapping("/login")
    fun loginPage(): String {
        return if(session.getAttribute("user") != null) "redirect:/list"
        else "login"
    }
    @RequestMapping("/register")
    fun registerPage(): String {
        return if(session.getAttribute("user") != null) "redirect:/list"
        else "register"
    }
    @RequestMapping("/myInfo")
    fun myInfoPage(): String {
        return if(session.getAttribute("user") != null) "info"
        else "redirect:/login"
    }
}