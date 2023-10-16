package com.task.data.local.recipe

import com.task.data.Resource
import com.task.data.datastore.manager.FavouriteProtoDataStoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class RecipeLocalDataSource @Inject constructor(
    private val favouriteProtoDataStoreManager: FavouriteProtoDataStoreManager,
    private val ioDispatcher: CoroutineContext,
) : RecipeLocalData {
    override suspend fun save(sets: MutableSet<String>): Resource<Boolean> {
        return favouriteProtoDataStoreManager.save(sets)
    }

    override suspend fun update(sets: MutableSet<String>): Resource<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun remove(id: String): Flow<Resource<Boolean>> {
        return flow {
            emit(favouriteProtoDataStoreManager.remove(id))
        }.flowOn(ioDispatcher)
    }

    override suspend fun get(): Resource<Set<String>> {
        return favouriteProtoDataStoreManager.get()
    }

    override suspend fun isFavourite(id: String): Flow<Resource<Boolean>> {
        return flow {
            emit(favouriteProtoDataStoreManager.isFavourite(id))
        }.flowOn(ioDispatcher)
    }

    override suspend fun addToFavourite(id: String): Resource<Boolean> {
        return get().let { it ->
            val result = it.data?.toMutableSet()?.let { set ->
                val isAdded = set.add(id)
                if (isAdded) {
                    save(set)
                    //                        Resource.DataError(401)
                } else {
                    Resource.Success(false)
                }
            }
            return@let result ?: Resource.DataError(401)
        }

    }

}