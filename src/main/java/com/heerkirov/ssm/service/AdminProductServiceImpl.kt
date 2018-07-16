package com.heerkirov.ssm.service

import com.heerkirov.ssm.dao.ProductDao
import com.heerkirov.ssm.model.Product
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class AdminProductServiceImpl(@Autowired private val productDao: ProductDao): AdminProductService {
    override fun addProduct(id: Int, name: String, price: Double, discount: Double, sale: Int, stock: Int, description: String, useful: Boolean): Product {
        val product = Product(Integer(id), name, java.lang.Double(price), java.lang.Double(discount), Integer(sale), Integer(stock), description, java.lang.Boolean(useful))
        product.id = Integer(productDao.add(product))
        return product
    }

    override fun update(id: Int, name: String?, price: Double?, discount: Double?, sale: Int?, stock: Int?, description: String?, useful: Boolean?): Product? {
        val product = productDao.getByIdOfAll(id)
        if(product != null) {
            if(name != null) product.name = name
            if(price != null) product.price = java.lang.Double(price)
            if(discount != null) product.discount = java.lang.Double(discount)
            if(sale != null) product.sale = Integer(sale)
            if(stock != null) product.stock = Integer(stock)
            if(description != null) product.description = description
            if(useful != null) product.useful = java.lang.Boolean(useful)
            productDao.update(product)
        }
        return product
    }

    override fun list(): List<Product> {
        return productDao.listOfAll()
    }

    override fun get(id: Int): Product? {
        return productDao.getByIdOfAll(id)
    }

    override fun remove(id: Int): Boolean {
        return if(productDao.getByIdOfAll(id) != null) {
            productDao.remove(id)
            true
        }else false
    }
}