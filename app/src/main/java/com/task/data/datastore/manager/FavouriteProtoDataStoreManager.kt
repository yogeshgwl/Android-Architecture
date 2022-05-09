package com.task.data.datastore.manager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.task.data.Resource
import com.task.data.datastore.favourite.FavouriteProtoModel
import com.task.data.datastore.favourite.FavouriteProtoModelSerializer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class FavouriteProtoDataStoreManager @Inject constructor(@ApplicationContext context: Context) : BaseProtoDataStoreManager() {

    //DataStore reference which is used to manipulate object from database

    private val favouriteDatastore: DataStore<FavouriteProtoModel> = DataStoreFactory.create(
        serializer = FavouriteProtoModelSerializer,
        produceFile = {context.dataStoreFile("Favourite_Pref")}
    )

    /**
     * This will save Favourite object in `Favourite_Pref` file
     *
     * Building a dummy Favourite Proto object to store in database
     */
    suspend fun saveFavourite(ids: Set<String>) : Resource<Boolean> {
        val fp =  FavouriteProtoModel.newBuilder()
            .addAllFavourites(ids)
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

    /**
     * Get all favourites
     */
    suspend fun getFavourite(): Resource<Set<String>> {
        val favouriteProto = getValue(favouriteDatastore)
        return (favouriteProto?.let {
            Resource.Success(it.favouritesList.toSet())
        } ?: run {
            Resource.Success(setOf())
        })
    }

    /**
     * Check the id is store or not
     */
    suspend fun isFavourite(id:String): Resource<Boolean> {
        val favouriteProto = getFavourite()
        favouriteProto?.let {
            return Resource.Success(it.data!!.contains(id))
        }
        return Resource.Success(false)
    }

    /**
     * remove selected favourite from the FavouriteProto
     */
    suspend fun removeFromFavourites(id: String): Resource<Boolean> {
        val favouriteProto = getFavourite()
        favouriteProto.data?.let {
            val set = it.toMutableSet()
            if(set.contains(id)){
               set.remove(id)
            }
            Resource.Success(saveFavourite(set))
        }
       return Resource.Success(false)
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