package com.task.ui.component.details

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.task.data.Resource
import com.task.data.dto.recipes.RecipesItem
import com.task.data.repository.recipe.RecipeRepositoryImpl
import com.task.domain.usecase.recipe.FavouriteParameter
import com.task.domain.usecase.recipe.FavouriteRecipeUserCase
import com.task.ui.base.BaseViewModel
import com.task.utils.wrapEspressoIdlingResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class DetailsViewModel @Inject constructor(
    private val recipeRepositoryImplSource: RecipeRepositoryImpl,
    private val favouriteRecipeUserCase: FavouriteRecipeUserCase
) : BaseViewModel() {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val recipePrivate = MutableLiveData<RecipesItem>()
    val recipeData: LiveData<RecipesItem> get() = recipePrivate

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val isFavouritePrivate = MutableLiveData<Boolean>()
    val isFavourite: LiveData<Boolean> get() = isFavouritePrivate

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _isError = MutableLiveData<Int>()
    val isError: LiveData<Int> get() = _isError

    fun initIntentData(recipe: RecipesItem) {
        recipePrivate.value = recipe
    }

    open fun addToFavourites() {
        viewModelScope.launch {
            _loading.value = true
            wrapEspressoIdlingResource {
                recipePrivate.value?.id?.let { it ->
                    val result = favouriteRecipeUserCase(FavouriteParameter(it))

                    when (result) {
                        is Resource.Success -> isFavouritePrivate.value = result.data ?: false
                        is Resource.DataError -> {
                            _isError.value = result.errorCode?.let {
                                it
                            }
                        }
                        is Resource.Loading -> _loading.value = true
                    }
                }
            }
        }
    }

    fun removeFromFavourites() {
        viewModelScope.launch {
            _loading.value = true
            wrapEspressoIdlingResource {
                recipePrivate.value?.id?.let { id ->
                    recipeRepositoryImplSource.removeFromFavourite(id).collect { isRemoved ->
                        when (isRemoved) {
                            is Resource.Success -> {
                                isRemoved.data?.let {
                                    isFavouritePrivate.value = it
                                }
                            }
                            is Resource.DataError -> {
                                isRemoved.data?.let { isFavouritePrivate.value = it }
                            }
                            is Resource.Loading -> {
                                isRemoved.data?.let { isFavouritePrivate.value = it }
                            }
                        }
                    }
                }
            }
        }
    }

    fun isFavourites() {
        viewModelScope.launch {
            _loading.value = true
            wrapEspressoIdlingResource {
                recipePrivate.value?.id?.let { id ->
                    recipeRepositoryImplSource.isFavourite(id).collect { isFavourites ->
                        isFavourites.data?.let { isFavouritePrivate.value = it }
                    }
                }
            }
        }
    }
}
