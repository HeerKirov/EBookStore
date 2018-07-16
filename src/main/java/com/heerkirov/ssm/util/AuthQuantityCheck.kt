package com.heerkirov.ssm.util

import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.util.*

@Component
class AuthQuantityCheck{
    companion object {
        const val deltaTimeMillis: Int = 1000 * 60
        const val maxTryTime: Int = 5
    }

    private val record: TreeMap<String, Item> = TreeMap()
    private class Item(var count: Int, var lastTry: Calendar)

    /**
     * 尝试做登录询问。返回true表示准许进行登陆。
     */
    fun tryAuthenticate(ip: String): Boolean {
        if(record.containsKey(ip)) {
            val item = record[ip]!!
            //在有记录的情况下，进行更复杂的判断。
            //如果记录时间与当前时间的差值大于delta，就将次数重置为1，重置时间，并视作重新插入
            //如果小于，判断次数是否多余max，多于的话，返回false
            //如果不多于，重置时间并次数+1
            val now = Calendar.getInstance()
            val minus = now.timeInMillis - item.lastTry.timeInMillis
            return if(minus >= deltaTimeMillis) {
                item.count = 1
                item.lastTry = now
                true
            }else if(item.count >= maxTryTime) {
                false
            }else{
                item.count += 1
                item.lastTry = now
                true
            }
        }else{
            //在没有记录的情况下，添加一条次数为1，时间为now的记录。
            record[ip] = Item(1, Calendar.getInstance())
            return true
        }
    }

    /**
     * 清除一条ip的登陆记录。用于登陆验证成功时。
     */
    fun clearRecord(ip: String): Boolean {
        return record.remove(ip) != null
    }
}