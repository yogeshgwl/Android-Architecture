package com.task.data.local.recipe

import com.task.data.Resource
import kotlinx.coroutines.flow.Flow

interface RecipeLocalData {
    suspend fun save(sets: MutableSet<String>): Resource<Boolean>
    suspend fun update(sets: MutableSet<String>): Resource<Boolean>
    suspend fun remove(id: String): Flow<Resource<Boolean>>
    suspend fun get(): Resource<Set<String>>
    suspend fun addToFavourite(id: String): Resource<Boolean>
    suspend fun isFavourite(id: String): Flow<Resource<Boolean>>
}