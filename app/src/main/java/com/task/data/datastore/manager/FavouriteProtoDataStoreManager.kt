package com.task.data.datastore.manager

import android.content.Context
import com.task.data.Resource
import com.task.data.datastore.favourite.FavouriteProtoModel
import com.task.data.datastore.favourite.FavouriteProtoModelSerializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class FavouriteProtoDataStoreManager @Inject constructor(context: Context) :
    BaseProtoDataStoreManager<FavouriteProtoModel>() {

    private val favouriteDatastore =
        getDataStore(context, FavouriteProtoModelSerializer, "Favourite_Pref")

    /**
     * This will save Favourite object in `Favourite_Pref` file
     *
     * Building a dummy Favourite Proto object to store in database
     */
    override suspend fun <T : Any> save(value: T): Resource<Boolean> {
        val fp = FavouriteProtoModel.newBuilder()
            .addAllFavourites(value as Set<String>)
            .build()

        //calling the base class's save method to abstract the saving implementation
        save(fp, favouriteDatastore)
        return Resource.Success(true)
    }

    /**
     * Getting Favourite from `Favourite_Pref` file as proto object
     * Will get in stream of data (listening the changes)
     *
     * @return  Flow<FavouriteProto>
     */
    fun getFavouriteFlow() = getValueAsFlow(favouriteDatastore)

    override suspend fun <T : Any> get(): T {
        val favouriteProto = getValue(favouriteDatastore)
        return (favouriteProto?.let {
            Resource.Success(it.favouritesList.toSet())
        } ?: run {
            Resource.Success(setOf())
        }) as T
    }

    /**
     * Check the id is store or not
     */
    suspend fun isFavourite(id: String): Resource<Boolean> {
        val favouriteProto = get<Resource<Set<String>>>()
        favouriteProto.let {
            return Resource.Success(contains(id, it.data!!))
        }
    }

    /**
     * remove selected favourite from the FavouriteProto
     */
    override suspend fun <T : Any> remove(key: T): Resource<Boolean> {
        val favouriteProto = get<Resource<Set<String>>>()
        favouriteProto.data?.let {
            val set = it.toMutableSet()
            if (contains(key, it)) {
                set.remove(key as String)
            }
            Resource.Success(save(set))
        }
        return Resource.Success(false)
    }

    override suspend fun <T : Any, K : Any> contains(key: T, value: K): Boolean {
        val set = value as Set<*>
        return (set.contains(key))
    }

    /**
     * This will clear values of FavouriteProto object
     *
     * @param block  Callback with success/fail
     */
    override suspend fun clear(block: ((Boolean) -> Unit)?) {
        withContext(Dispatchers.IO) {
            try {
                favouriteDatastore.updateData { it.toBuilder().clear().build() }
                block?.let {
                    withContext(Dispatchers.Main) { it(true) }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                block?.let {
                    withContext(Dispatchers.Main) { it(false) }
                }
            }

        }
    }
}