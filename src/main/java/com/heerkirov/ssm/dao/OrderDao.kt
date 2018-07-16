package com.heerkirov.ssm.dao

import com.heerkirov.ssm.model.Order
import com.heerkirov.ssm.model.OrderProductRelation
import org.apache.ibatis.annotations.Param

interface OrderDao {
    /**
     * 查询某用户手下的所有订单
     */
    fun listByUser(userId: String): List<Order>

    /**
     * 查询某用户的某id的订单
     */
    fun getByIdAndUser(@Param("userId")userId: String, @Param("id")id: Int): Order?

    /**
     * 添加一个新的空订单
     */
    fun add(@Param("userId")userId: String, @Param("status")status: String)

    /**
     * 更新某订单状态
     */
    fun update(order: Order)

    /**
     * 获得某用户的购物车
     */
    fun cartByUser(userId: String): Order?

    /**
     * 像购物车内添加指定数量的商品
     */
    fun addProduct(@Param("orderId")orderId: Int, @Param("productId")productId: Int, @Param("count")count: Int = 1)

    /**
     * 更新购物车内指定商品的数量
     */
    fun updateProduct(@Param("orderId")orderId: Int, @Param("productId")productId: Int, @Param("count")count: Int = 1)

    /**
     * 移除购物车内的某商品
     */
    fun removeProduct(@Param("orderId")orderId: Int, @Param("productId")productId: Int)

    /**
     * 【管理员】获得全部订单
     */
    fun listOfAll(): List<Order>

    fun get(id: Int): Order?
}