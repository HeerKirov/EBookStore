package com.heerkirov.ssm.service

import com.heerkirov.ssm.dao.OrderDao
import com.heerkirov.ssm.dao.OrderProductDao
import com.heerkirov.ssm.dao.ProductDao
import com.heerkirov.ssm.model.Order
import com.heerkirov.ssm.model.OrderProductRelation
import com.heerkirov.ssm.model.Product
import com.heerkirov.ssm.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.sql.Timestamp
import java.time.LocalDateTime
import javax.servlet.http.HttpSession

@Component
class OrderServiceImpl(@Autowired private val orderDao: OrderDao,
                       @Autowired private val productDao: ProductDao,
                       @Autowired private val orderProductDao: OrderProductDao,
                       @Autowired private val session: HttpSession) : OrderService {
    private fun createCart(): Order {
        val user = session.getAttribute("user")!! as User
        var cart = orderDao.cartByUser(user.id)
        if(cart == null) {
            orderDao.add(user.id, "cart")
            cart = orderDao.cartByUser(user.id)
        }
        return cart!!
    }
    private fun isLogin(): Boolean {
        return session.getAttribute("user") != null
    }
    private fun unauthorizedCart(): Order {
        var cart = session.getAttribute("cart") as Order?
        if(cart == null) {
            cart = Order(status = "cart")
            session.setAttribute("cart", cart)
        }
        return cart
    }
    override fun cartPutProduct(productId: Int): Order {
        if(isLogin()) {
            val cart = createCart()
            val result = orderProductDao.get(cart.id.toInt(), productId)
            if(result != null) {
                orderDao.updateProduct(cart.id.toInt(), productId, result.count.toInt() + 1)
            }else{
                orderDao.addProduct(cart.id.toInt(), productId, 1)
            }
            return createCart()
        }else{
            val cart = unauthorizedCart()
            val result = cart.products.firstOrNull { it.product!!.id.toInt() == productId }
            if(result != null) {
                result.count = java.lang.Integer(result.count.toInt() + 1)
            }else{
                cart.products = cart.products.toMutableList().also { it.add(OrderProductRelation(
                        count = java.lang.Integer(1),
                        orderId = java.lang.Integer(0),
                        product = productDao.getById(productId)!!
                )) }
            }
            return cart
        }
    }

    override fun cartPutProducts(products: List<OrderProductRelation>): Order {
        //该函数用于auth认证后批量加入购物车，不设计未登录模式。
        val cart = createCart()
        for (op in products) {
            val result = orderProductDao.get(cart.id.toInt(), op.product!!.id.toInt())
            if(result != null) orderDao.updateProduct(cart.id.toInt(), op.product!!.id.toInt(), result.count.toInt() + op.count.toInt())
            else orderDao.addProduct(cart.id.toInt(), op.product!!.id.toInt(), op.count.toInt())
        }
        return createCart()
    }

    override fun cartUpdateProduct(productId: Int, count: Int): Order {
        if(isLogin()) {
            val cart = createCart()
            val result = orderProductDao.get(cart.id.toInt(), productId)
            if(result != null) orderDao.updateProduct(cart.id.toInt(), productId, count)
            else orderDao.addProduct(cart.id.toInt(), productId, count)
            return createCart()
        }else {
            val cart = unauthorizedCart()
            val result = cart.products.firstOrNull { it.product!!.id.toInt() == productId }
            if(result != null) result.count = java.lang.Integer(count)
            else cart.products = cart.products.toMutableList().also { it.add(OrderProductRelation(
                    count = java.lang.Integer(count),
                    orderId = java.lang.Integer(0),
                    product = productDao.getById(productId)!!
            )) }
            return cart
        }
    }

    override fun cartRemoveProduct(productId: Int): Order {
        if(isLogin()) {
            val cart = createCart()
            val result = orderProductDao.get(cart.id.toInt(), productId)
            if(result != null) orderDao.removeProduct(cart.id.toInt(), productId)
            return createCart()
        }else{
            val cart = unauthorizedCart()
            val result = cart.products.firstOrNull { it.product!!.id.toInt() == productId }
            if(result != null) cart.products = cart.products.toMutableList().also { it.removeIf { it.product!!.id.toInt() == productId } }
            return cart
        }
    }

    override fun cartSubmit(): Order? {
        //作用：将cart转换为submitted，赋予create time，计算总价。
        //购物车内连接的商品全部检查库存。
        //如果购物车为空，不会做出动作，并返回NULL。
        val cart = try{createCart()}catch(e: NullPointerException) {throw NotLoginException()}
        if(cart.products.isEmpty()) throw EmptyCartException()
        val lack: ArrayList<Product> = ArrayList()
        for (op in cart.products) {
            if(op.product!!.stock < op.count.toInt()) lack.add(op.product!!)
        }
        if(lack.isNotEmpty()) throw LackStockException(lack)
        cart.status = "submitted"
        cart.createTime = Timestamp.valueOf(LocalDateTime.now())
        var sum = 0.0
        cart.products.forEach { op ->
            sum += op.product!!.price.toDouble() * op.product!!.discount.toDouble()
        }
        cart.price = java.lang.Double(sum)
        orderDao.update(cart)
        for (op in cart.products) {
            op.product!!.sale = java.lang.Integer(op.product!!.sale.toInt() + op.count.toInt())
            op.product!!.stock = java.lang.Integer(op.product!!.stock.toInt() - op.count.toInt())
            productDao.update(op.product!!)
        }
        return cart
    }

    override fun getCart(): Order {
        return if(isLogin())createCart()
        else unauthorizedCart()
    }

    override fun list(): List<Order> {
        val user = session.getAttribute("user")!! as User
        return orderDao.listByUser(user.id)
    }

    override fun get(id: Int): Order? {
        val user = session.getAttribute("user")!! as User
        return orderDao.getByIdAndUser(user.id, id)
    }

    override fun complete(id: Int): Boolean {
        val user = session.getAttribute("user")!! as User
        val order = orderDao.getByIdAndUser(user.id, id)!!
        if(order.status == "running") {
            order.status = "complete"
            orderDao.update(order)
            return true
        }else{
            return false
        }
    }

    override fun returnIt(id: Int): Boolean {
        val user = session.getAttribute("user")!! as User
        val order = orderDao.getByIdAndUser(user.id, id)!!
        if(order.status == "complete") {
            order.status = "returning"
            orderDao.update(order)
            return true
        }else{
            return false
        }
    }
}