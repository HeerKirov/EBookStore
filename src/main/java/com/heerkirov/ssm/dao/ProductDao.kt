package com.heerkirov.ssm.dao

import com.heerkirov.ssm.model.Product

interface ProductDao {
    fun list(): List<Product>
    fun getById(id: Int): Product?
    fun listOfAll(): List<Product>
    fun getByIdOfAll(id: Int): Product?
    fun add(product: Product): Int
    fun update(product: Product)
    fun remove(id: Int)
}