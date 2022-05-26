package com.task.data.repository.recipe

import com.task.data.Resource
import com.task.data.dto.recipes.Recipes
import kotlinx.coroutines.flow.Flow


interface RecipeRepository {
    suspend fun requestRecipes(): Flow<Resource<Recipes>>
    suspend fun addToFavourite(id: String): Resource<Boolean>
    suspend fun removeFromFavourite(id: String): Flow<Resource<Boolean>>
    suspend fun isFavourite(id: String): Flow<Resource<Boolean>>
}
