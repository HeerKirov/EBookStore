package com.heerkirov.ssm.controller

import com.heerkirov.ssm.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

@RequestMapping("/admin")
@Controller
class AdminWebController(@Autowired private val session: HttpSession,
                         @Autowired private val httpServletRequest: HttpServletRequest) {
    private fun ad(action: ()->String): String {
        val user = session.getAttribute("user") as User?
        return if(user == null || (!user.isAdmin.booleanValue())) "redirect:/"
        else action()
    }
    /**管理内容包括：
     * 商品管理：
     *      添加商品，修改商品信息
     *      删除商品
     * 订单管理：
     *      确认为submitted的订单发货
     *      查看所有订单
     */
    @RequestMapping("/product/list")
    fun productListPage() = ad{ "admin/productList" }
    @RequestMapping("/product/add")
    fun productAddPage() = ad{ "admin/productAdd" }
    @RequestMapping("/product/detail/{id}")
    fun productDetailPage(@PathVariable id: String) = ad{
        httpServletRequest.setAttribute("id", id.toInt())
        "admin/productDetail"
    }
    @RequestMapping("/order/list")
    fun orderListPage() = ad{ "admin/orderList" }

    @RequestMapping("")
    fun default() = ad { "redirect:/admin/order/list" }
}