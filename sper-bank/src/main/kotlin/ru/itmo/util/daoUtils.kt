package ru.itmo.util

import io.r2dbc.spi.Row
import org.springframework.r2dbc.core.DatabaseClient

inline fun <reified T> Row.getNullable(name: String): T? = get(name, T::class.java)
inline fun <reified T> Row.getOrThrow(name: String): T = requireNotNull(getNullable(name)) { "$name is null" }

inline fun <reified T> DatabaseClient.GenericExecuteSpec.bindNullable(name: String, value: T?): DatabaseClient.GenericExecuteSpec =
    if (value != null) {
        bind(name, value)
    } else {
        bindNull(name, T::class.java)
    }
