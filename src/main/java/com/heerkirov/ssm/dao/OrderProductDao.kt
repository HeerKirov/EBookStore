package com.heerkirov.ssm.dao

import com.heerkirov.ssm.model.OrderProductRelation
import org.apache.ibatis.annotations.Param

interface OrderProductDao {
    fun listByOrderId(orderId: Int): List<OrderProductRelation>

    fun get(@Param("orderId")orderId: Int, @Param("productId")productId: Int): OrderProductRelation?
}