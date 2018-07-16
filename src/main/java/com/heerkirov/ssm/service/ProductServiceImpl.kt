package com.heerkirov.ssm.service

import com.heerkirov.ssm.dao.ProductDao
import com.heerkirov.ssm.model.Product
import com.sun.org.apache.xpath.internal.operations.Bool
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ProductServiceImpl(@Autowired private val productDao: ProductDao) : ProductService {

    override fun list(): List<Product> {
        return productDao.list()
    }

    override fun get(id: Int): Product? {
        return productDao.getById(id)
    }

}