package com.task.domain.usecase.recipe

import com.task.data.Resource
import com.task.data.repository.recipe.RecipeRepository
import com.task.usecase.UseCase
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class FavouriteRecipeUserCase @Inject constructor(
    private val recipeRepository: RecipeRepository,
    ioDispatcher: CoroutineContext,
) : UseCase<FavouriteParameter, Boolean>(ioDispatcher) {

    override suspend fun execute(parameters: FavouriteParameter): Boolean {
        val result = recipeRepository.addToFavourite(parameters.id)
        when (result) {
            is Resource.Success -> {
                result.data
            }
            is Resource.DataError -> result.errorCode?.let { error ->
                Resource.DataError<Boolean>(
                    error
                )
            }
            is Resource.Loading -> throw IllegalStateException()
        }
        return result.data ?: false
    }
}

data class FavouriteParameter(
    val id: String,
)