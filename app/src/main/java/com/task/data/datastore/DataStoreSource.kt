package com.task.data.datastore

import com.task.data.Resource

interface DataStoreSource {
    suspend fun <T : Any> get(): T

    suspend fun <T : Any> save(value: T): Resource<Boolean>

    suspend fun <T : Any, K : Any> contains(key: T, value: K): Boolean

    suspend fun <T : Any> remove(key: T): Resource<Boolean>

}