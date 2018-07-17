package com.heerkirov.ssm.dao

import com.heerkirov.ssm.model.User
import org.apache.ibatis.annotations.Param
import org.springframework.stereotype.Component

interface UserDao{
    fun add(obj: User)
    fun updatePassword(@Param("id")id: String, @Param("password") password: String)
    fun getById(id: String): User?
    fun update(obj: User)
    fun listOfAll(): List<User>
}