package com.heerkirov.ssm.service

import com.heerkirov.ssm.dao.UserDao
import com.heerkirov.ssm.model.User
import com.heerkirov.ssm.util.Salt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class AdminUserServiceImpl(@Autowired private val userDao: UserDao,
                           @Autowired private val saltUtil: Salt,
                           @Autowired private val userService: UserService) : AdminUserService {
    override fun list(): List<User> {
        return userDao.listOfAll()
    }

    override fun get(id: String): User? {
        return userDao.getById(id)
    }

    override fun updatePassword(id: String, newPassword: String): Boolean {
        val user = userDao.getById(id)?:return false
        userDao.updatePassword(id, userService.computeSaltPassword(newPassword, user.salt))
        return true
    }
}