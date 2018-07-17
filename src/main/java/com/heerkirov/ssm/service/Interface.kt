package com.heerkirov.ssm.service

import com.heerkirov.ssm.model.Order
import com.heerkirov.ssm.model.OrderProductRelation
import com.heerkirov.ssm.model.Product
import com.heerkirov.ssm.model.User
import org.springframework.stereotype.Service

@Service
interface UserService {
    fun addUser(id: String, name: String, password: String, isAdmin: Boolean, phone: String? = null, address: String? = null): User

    fun updatePassword(oldPassword: String, newPassword: String): Boolean

    fun login(id: String, password: String): User?

    fun logout(): Boolean

    fun computeSaltPassword(password: String, salt: String): String

    fun computeSalt(id: String): String

    fun info(): User?

    fun updateInfo(name: String? = null, phone: String? = null, address: String? = null): User?
}

@Service
interface ProductService {
    fun list(): List<Product>

    fun get(id: Int): Product?
}

@Service
interface OrderService {
    fun cartPutProduct(productId: Int): Order

    fun cartPutProducts(products: List<OrderProductRelation>): Order

    fun cartUpdateProduct(productId: Int, count: Int): Order

    fun cartRemoveProduct(productId: Int): Order

    fun cartSubmit(): Order?

    fun getCart(): Order

    fun list(): List<Order>

    fun get(id: Int): Order?

    fun complete(id: Int): Boolean

    fun returnIt(id: Int): Boolean
}

@Service
interface AdminOrderService {
    fun list(): List<Order>

    fun changeStatus(id: Int, status: String): Order
}
@Service
interface AdminProductService {
    fun addProduct(id: Int, name: String, price: Double, discount: Double, sale: Int, stock: Int, description: String, useful: Boolean): Product

    fun update(id: Int, name: String?, price: Double?, discount: Double?, sale: Int?, stock: Int?, description: String?, useful: Boolean?): Product?

    fun list(): List<Product>

    fun get(id: Int): Product?

    fun remove(id: Int): Boolean
}
@Service
interface AdminUserService {
    fun list(): List<User>

    fun get(id: String): User?

    fun updatePassword(id: String, newPassword: String): Boolean
}

