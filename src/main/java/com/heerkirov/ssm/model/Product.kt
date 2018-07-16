package com.heerkirov.ssm.model

class Product(
    var id: java.lang.Integer = Integer(0),
    var name: String = "",
    var price: java.lang.Double = java.lang.Double(0.0),
    var discount: java.lang.Double = java.lang.Double(0.0),
    var sale: java.lang.Integer = Integer(0),
    var stock: java.lang.Integer = Integer(0),
    var description: String = "",
    var useful: java.lang.Boolean = java.lang.Boolean(true)
)