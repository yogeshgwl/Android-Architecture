package com.task.data.repository.recipe

import com.task.data.Resource
import com.task.data.dto.recipes.Recipes
import com.task.data.local.recipe.RecipeLocalDataSource
import com.task.data.remote.recipe.RecipeRemoteData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val recipeRemoteData: RecipeRemoteData,
    private val recipeLocalDataSource: RecipeLocalDataSource,
) : RecipeRepository {

    override suspend fun requestRecipes(): Flow<Resource<Recipes>> {
        return recipeRemoteData.requestRecipes()
    }

    override suspend fun addToFavourite(id: String): Resource<Boolean> {
        return recipeLocalDataSource.addToFavourite(id)
    }

    override suspend fun removeFromFavourite(id: String): Flow<Resource<Boolean>> {
        return recipeLocalDataSource.remove(id)
    }

    override suspend fun isFavourite(id: String): Flow<Resource<Boolean>> {
        return recipeLocalDataSource.isFavourite(id)
    }
}
