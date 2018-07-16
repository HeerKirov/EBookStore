package com.heerkirov.ssm.util

import java.util.*
import kotlin.reflect.full.memberProperties

fun Any.toMap(include: Set<String> = emptySet(), exclude: Set<String> = emptySet(), custom: Map<String, (Any?)->Any?>? = null): Map<String, Any?> {
    val clazz = this::class
    val ret = TreeMap<String, Any?>()
    clazz.memberProperties.forEach {
        if(it.name !in exclude && ((it.name in include && include.isNotEmpty())||include.isEmpty())) {
            val value = it.call(this)
            if(custom != null && custom.containsKey(it.name)) ret[it.name] = custom[it.name]?.invoke(value)
            else ret[it.name] = value
        }
    }
    return ret
}

fun<T> List<T>.toMapList(include: Set<String> = emptySet(), exclude: Set<String> = emptySet()): List<Map<String, Any?>> where T: Any {
    return this.map { it.toMap(include, exclude) }
}