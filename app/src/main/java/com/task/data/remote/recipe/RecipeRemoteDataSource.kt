package com.task.data.remote.recipe

import com.task.data.Resource
import com.task.data.dto.recipes.Recipes
import kotlinx.coroutines.flow.Flow

interface RecipeRemoteDataSource {
    suspend fun requestRecipes(): Flow<Resource<Recipes>>
}
