package com.heerkirov.ssm.model

import java.lang.*

class User(
    var id: String = "",
    var name: String = "",
    var password: String = "",
    var salt: String = "",
    var isAdmin: java.lang.Boolean = Boolean(false),
    var phone: String? = null,
    var address: String? = null
)
//let user = User()