package com.heerkirov.ssm.service

import com.heerkirov.ssm.dao.UserDao
import com.heerkirov.ssm.model.User
import com.heerkirov.ssm.util.Salt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.servlet.http.HttpSession
import org.apache.commons.codec.digest.DigestUtils
import java.lang.StringBuilder
import java.util.*

@Component
class UserServiceImpl(@Autowired private val session: HttpSession,
                      @Autowired private val userDao: UserDao,
                      @Autowired private val saltUtil: Salt) : UserService {
    override fun addUser(id: String, name: String, password: String, isAdmin: Boolean, phone: String?, address: String?): User {
        val salt = computeSalt(id)
        val user = User(id, name, computeSaltPassword(password, salt), salt, java.lang.Boolean(isAdmin), phone, address)
        userDao.add(user)
        return user
    }

    override fun updatePassword(oldPassword: String, newPassword: String): Boolean {
        val user = session.getAttribute("user") as User?
        return if(user != null && computeSaltPassword(oldPassword, user.salt) == user.password) {
            user.password = computeSaltPassword(newPassword, user.salt)
            userDao.updatePassword(user.id, user.password)
            true
        }else false
    }

    override fun login(id: String, password: String): User? {
        val user = userDao.getById(id)
        return if(user != null && computeSaltPassword(password, user.salt) == user.password) {
            session.setAttribute("user", user)
            user
        }else null
    }

    override fun logout(): Boolean {
        val result = session.getAttribute("user")
        return if(result != null) {
            session.removeAttribute("user")
            true
        }else false
    }

    override fun info(): User? {
        val id = (session.getAttribute("user") as User?)?.id?:return null
        return userDao.getById(id)
    }

    override fun updateInfo(name: String?, phone: String?, address: String?): User? {
        val id = (session.getAttribute("user") as User?)?.id?:return null
        val user = userDao.getById(id)?:return null
        if(name != null)user.name = name
        if(phone != null)user.phone = phone
        if(address != null) user.address = address
        userDao.update(user)
        return user
    }

    override fun computeSaltPassword(password: String, salt: String): String {
        return DigestUtils.sha384Hex("$salt^${password.reversed()}.${saltUtil.salt}")
    }

    override fun computeSalt(id: String): String {
        val ret = StringBuilder("${id.hashCode()}-SALT-")
        val rand = Random()
        for(i in 0..12) ret.append(rand.nextInt(128).toChar())
        return ret.toString()
    }
}