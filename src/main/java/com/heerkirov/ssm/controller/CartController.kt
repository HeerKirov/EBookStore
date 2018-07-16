package com.heerkirov.ssm.controller

import com.heerkirov.ssm.controller.base.ApiController
import com.heerkirov.ssm.controller.base.BadRequestException
import com.heerkirov.ssm.controller.base.HttpKeyword
import com.heerkirov.ssm.dao.OrderProductDao
import com.heerkirov.ssm.model.OrderProductRelation
import com.heerkirov.ssm.model.Product
import com.heerkirov.ssm.service.EmptyCartException
import com.heerkirov.ssm.service.LackStockException
import com.heerkirov.ssm.service.NotLoginException
import com.heerkirov.ssm.service.OrderService
import com.heerkirov.ssm.util.toMap
import com.heerkirov.ssm.util.toMapList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession

@RequestMapping("/api/cart")
@Controller
class CartController(@Autowired private val request: HttpServletRequest,
                     @Autowired private val session: HttpSession,
                     @Autowired private val orderService: OrderService): ApiController() {
    override fun request(): HttpServletRequest = request

    @GetMapping("") @ResponseBody
    fun get(): Any {
        val result = orderService.getCart()
        return result.toMap(custom = mapOf("products" to { products ->
            (products as List<OrderProductRelation>).map { op ->
                TreeMap<String, Any?>(op.product!!.toMap()).also { it["count"] = op.count }
            }
        }))
    }

    @PostMapping("/products") @ResponseBody
    fun addProduct(): Any {
        val content = contentBodyObject()?:throw BadRequestException(keyword = HttpKeyword.NO_ENOUGH_INFORMATION)
        val id = content["productId"]?:throw BadRequestException(keyword = HttpKeyword.NO_ENOUGH_INFORMATION)
        val result = orderService.cartPutProduct(id.toString().toInt())
        return result.toMap(custom = mapOf("products" to { products ->
            (products as List<OrderProductRelation>).map { op ->
                TreeMap<String, Any?>(op.product!!.toMap()).also { it["count"] = op.count }
            }
        }))
    }
    @RequestMapping("/products", method = [RequestMethod.PATCH, RequestMethod.PUT]) @ResponseBody
    fun updateProduct(): Any {
        val content = contentBodyObject()?:throw BadRequestException(keyword = HttpKeyword.NO_ENOUGH_INFORMATION)
        val id = content["productId"]?:throw BadRequestException(keyword = HttpKeyword.NO_ENOUGH_INFORMATION)
        val count = content["count"]?:throw BadRequestException(keyword = HttpKeyword.NO_ENOUGH_INFORMATION)
        val result = orderService.cartUpdateProduct(id.toString().toInt(), count.toString().toInt())
        return result.toMap(custom = mapOf("products" to { products ->
            (products as List<OrderProductRelation>).map { op ->
                TreeMap<String, Any?>(op.product!!.toMap()).also { it["count"] = op.count }
            }
        }))
    }
    @DeleteMapping("/products") @ResponseBody
    fun removeProduct(): Any {
        val content = contentBodyObject()?:throw BadRequestException(keyword = HttpKeyword.NO_ENOUGH_INFORMATION)
        val id = content["productId"]?:throw BadRequestException(keyword = HttpKeyword.NO_ENOUGH_INFORMATION)
        val result = orderService.cartRemoveProduct(id.toString().toInt())
        return result.toMap(custom = mapOf("products" to { products ->
            (products as List<OrderProductRelation>).map { op ->
                TreeMap<String, Any?>(op.product!!.toMap()).also { it["count"] = op.count }
            }
        }))
    }
    @PostMapping("/submit") @ResponseBody
    fun submit(): Any {
        try{
            val result = orderService.cartSubmit()
            return mapOf("success" to (result != null))
        }catch (e: EmptyCartException) {
            return mapOf("success" to false, "keyword" to "EMPTY_CART", "error" to "Cart is empty.")
        }catch(e: LackStockException) {
            return mapOf("success" to false, "keyword" to "LACK_STOCK", "list" to e.toNameList(), "error" to "Some products are lack.")
        }catch(e: NotLoginException) {
            return mapOf("success" to false, "keyword" to "NOT_LOGIN", "error" to "You need log in.")
        }
    }

}