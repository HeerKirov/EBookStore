package com.heerkirov.ssm.service

import com.heerkirov.ssm.dao.OrderDao
import com.heerkirov.ssm.model.Order
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class AdminOrderServiceImpl(@Autowired private val orderDao: OrderDao) : AdminOrderService {
    override fun list(): List<Order> {
        return orderDao.listOfAll()
    }

    override fun changeStatus(id: Int, status: String): Order {
        val order = orderDao.get(id)!!
        if((order.status == "submitted" && status == "running")||
                (order.status == "returning" && status == "returned")) {
            order.status = status
            orderDao.update(order)
        }
        return order
    }
}