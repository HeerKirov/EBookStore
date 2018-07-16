package com.heerkirov.ssm.model

import java.sql.Timestamp
import java.util.*
import kotlin.collections.ArrayList

/** status状态：
 * cart 充当购物车
 * submitted 已经提交订单，等待发货
 * running 正在发货状态，等待收货
 * completed 订单已完成
 */

class Order(
        var id: java.lang.Integer = Integer(0),
        var createTime: Timestamp? = null,
        var status: String = "cart",
        var products: List<OrderProductRelation> = ArrayList<OrderProductRelation>(),
        var price: java.lang.Double = java.lang.Double(0.0),
        var userId: String = ""
)

class OrderProductRelation(
        var id: java.lang.Integer = Integer(0),
        var count: java.lang.Integer = Integer(0),
        var orderId: java.lang.Integer = Integer(0),
        var product: Product? = null
)