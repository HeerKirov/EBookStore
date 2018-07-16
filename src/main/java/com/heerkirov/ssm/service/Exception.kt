package com.heerkirov.ssm.service

import com.heerkirov.ssm.model.Product

class EmptyCartException(): RuntimeException()

class LackStockException(val lack: List<Product>): RuntimeException() {
    fun toNameList(): List<String> = lack.map { it.name }
}

class NotLoginException(): RuntimeException()