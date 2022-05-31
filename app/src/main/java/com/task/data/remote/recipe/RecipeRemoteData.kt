package com.task.data.remote.recipe

import com.task.data.Resource
import com.task.data.dto.recipes.Recipes
import com.task.data.dto.recipes.RecipesItem
import com.task.data.remote.RemoteData
import com.task.data.remote.ServiceGenerator
import com.task.data.remote.service.RecipesService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class RecipeRemoteData @Inject constructor(
    private val serviceGenerator: ServiceGenerator,
    private val remoteData: RemoteData,
    private val ioDispatcher: CoroutineContext,
): RecipeRemoteDataSource {

    override suspend fun requestRecipes(): Flow<Resource<Recipes>> {
        val recipesService = serviceGenerator.createService(RecipesService::class.java)
        val result = when (val response = remoteData.processCall(recipesService::fetchRecipes)) {
            is List<*> -> {
                Resource.Success(data = Recipes(response as ArrayList<RecipesItem>))
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
        return flow {
            emit(result)
        }.flowOn(ioDispatcher)
    }
}