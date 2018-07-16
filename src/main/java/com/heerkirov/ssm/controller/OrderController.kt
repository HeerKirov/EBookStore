package com.heerkirov.ssm.controller

import com.heerkirov.ssm.controller.base.ApiController
import com.heerkirov.ssm.dao.OrderProductDao
import com.heerkirov.ssm.model.OrderProductRelation
import com.heerkirov.ssm.model.Product
import com.heerkirov.ssm.service.OrderService
import com.heerkirov.ssm.util.toMap
import com.heerkirov.ssm.util.toMapList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

@RequestMapping("/api/order")
@Controller
class OrderController(@Autowired private val request: HttpServletRequest,
                      @Autowired private val session: HttpSession,
                      @Autowired private val orderService: OrderService): ApiController() {
    override fun request(): HttpServletRequest = request

    @GetMapping("") @ResponseBody
    fun list(): Any {
        val result = orderService.list()
        return result.map { order ->
            order.toMap(custom = mapOf("products" to { products ->
                (products as List<OrderProductRelation>).map { op ->
                    TreeMap<String, Any?>(op.product!!.toMap()).also { it["count"] = op.count }
                }
            }))
        }
    }
    @GetMapping("/{id}") @ResponseBody
    fun retrieve(@PathVariable id: String): Any {
        val result = orderService.get(id.toInt())!!
        return result.toMap(custom = mapOf("products" to { products ->
            (products as List<OrderProductRelation>).map { op ->
                TreeMap<String, Any?>(op.product!!.toMap()).also { it["count"] = op.count }
            }
        }))
    }
    @PostMapping("/{id}/complete") @ResponseBody
    fun complete(@PathVariable id: String): Any {
        val result = orderService.complete(id.toInt())
        return mapOf("success" to result)
    }

}